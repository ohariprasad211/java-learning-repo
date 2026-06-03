package org.learning;

import net.apmoller.crb.apmt.microservices.ail.model.apmt.xml.*;
import net.apmoller.crb.apmt.microservices.ail.utility.CommonUtility;
import net.apmoller.crb.apmt.microservices.ail.utility.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class LogProcessor {
    public static void main(String[] args) throws IOException {
        /**
         * Query to extract the relevant log lines from Grafana: Xml-mq adapter logs contain both BMS and AIL messages.
         * To filter and format these logs for better readability, you can use the following Grafana query:
         * Grafana Query:  |~ `receiveRTGMessage|Payload: <Message`|~`R101|System`
         * |~ `receiveRTGMessage|Payload: <Message|System`|~`R101|R88|R102|System`
         * |~ `receiveRTGMessage|Payload: <Message|System`
         * |= `R101`|~`receiveRTGMessage|Received ECN4 message with Key:|Payload: <Message`|= `20260524094825192`
         */
        Path inputFile = Path.of("C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
//                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
        );
        Path outputFile = Path.of("C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/summary.txt"
//                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/formatted-output.txt"
        );
        Path excelFile = Path.of(
//                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/summary_report.xlsx"
                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/summary_report.xlsx"
        );
        String content = Files.readString(inputFile);

        /*
         * Extract log timestamp + payload together
         */
        Pattern pattern = Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2}T\\S+).*?Payload:\\s*(AIL|ECS)\\s+Message:\\s*(<\\?xml.*?</Message>|<Message.*?</Message>)",
                Pattern.DOTALL
        );
        /**
         * Pattern matching to get Che name for
         * WaListUpdatedResponse
         * WaCancelledResponse
         */
        Pattern cheNamePattern = Pattern.compile("<Che[^>]*name=\"([^\"]+)\"");

        Matcher matcher = pattern.matcher(content);

        /*
         * Store request time using msgId
         */
        Map<String, Instant> msgTimeMap = new HashMap<>();

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Logs");

        int rowNum = 0;

        Row header = sheet.createRow(rowNum++);

        String[] columns = {
                "Source",
                "Timestamp",
                "MsgId",
                "PingId",
                "CheName",
                "EventType",
                "Action",
                "JobCount",
                "Container",
                "FromPosition",
                "ToPosition",
                "TimeTaken_ms"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);
        }

        while (matcher.find()) {

            String logTimestamp = matcher.group(1);
            String sourceType = matcher.group(2);
            String payload = matcher.group(3).trim();
            boolean isBms = "ECS".equals(sourceType);

            /*
             * Source
             */
            String source = sourceType;

            Message xmlMessage = Utils.getXmlObject(payload, Message.class);

            String msgId = nonNull(xmlMessage.getMsgId()) ? xmlMessage.getMsgId() : "";
            String pingId = nonNull(xmlMessage.getPingId()) ? xmlMessage.getPingId() : "";

            String che = "";
            Integer jobCount = null;
            String fromPosition = "";
            String toPosition = "";
            String eventType = "";
            String action = "";
            String container = "";
            /*
             * WaStatusEvent
             */
            if (xmlMessage.getWaStatusEvent() instanceof WaListUpdated wl) {
                eventType = "WaListUpdated";
                che = wl.getChe().getName();
                jobCount = nonNull(wl.getJobList()) ? wl.getJobList().get(0).getJob().size() : null;
            }

            if (xmlMessage.getWaStatusEvent() instanceof WaCreated wa) {
                eventType = "WaCreated";
                che = wa.getChe().getName();
                if (nonNull(wa.getJob()) && nonNull(wa.getJob().getPickTarget()) && !wa.getJob().getPickTarget().isEmpty()) {
                    container= wa.getJob().getPickTarget().get(0).getContainer().getIsoId();
                    fromPosition = getPositionData(wa.getJob().getPickTarget().get(0).getPosition());
                }

                if (nonNull(wa.getJob()) && nonNull(wa.getJob().getPlaceTarget()) && !wa.getJob().getPlaceTarget().isEmpty()) {
                    toPosition = getPositionData(wa.getJob().getPlaceTarget().get(0).getPosition());
                }
            }

            if (xmlMessage.getWaStatusEvent() instanceof WaUpdated waUpdated) {
                eventType = "WaUpdated";
                che = waUpdated.getChe().getName();
            }

            if (xmlMessage.getWaStatusEvent() instanceof WaAccepted waAccepted) {
                eventType = "WaAccepted";
                che = waAccepted.getChe().getName();
                container= waAccepted.getContainer().get(0).getIsoId();
            }

            if (xmlMessage.getWaStatusEvent() instanceof WaCancelled waCancelled) {
                eventType = "WaCancelled";
                che = waCancelled.getChe().getName();
            }

            if (xmlMessage.getWaStatusEvent() instanceof WaAborted waAborted) {
                eventType = "WaAborted";
                che = waAborted.getChe().getName();
            }

            if (xmlMessage.getWaStatusEvent() instanceof WaSuspended waSuspended) {
                eventType = "WaSuspended";
                che = waSuspended.getChe().getName();
            }

            /*
             * AckEvent
             */
            if (xmlMessage.getAckEvent() instanceof WaCreatedResponse response) {
                eventType = "WaCreatedResponse";
                che = response.getChe().getName();
            }

            if (xmlMessage.getAckEvent() instanceof CheActivityResponse response) {
                eventType = "CheActivityResponse";
                che = response.getChe().getName();
            }

            if (xmlMessage.getAckEvent() instanceof OpStateUpdatedResponse response) {
                eventType = "OpStateUpdatedResponse";
                che = response.getChe().getName();
                if (nonNull(response.getChe().getOperatorState())) {
                    action = response.getChe().getOperatorState().toString();
                }
            }

            if (xmlMessage.getAckEvent() instanceof WaAcceptedResponse response) {
                eventType = "WaAcceptedResponse";
                che = response.getChe().getName();
            }

            if (xmlMessage.getAckEvent() instanceof WaListUpdatedResponse response) {
                eventType = "WaListUpdatedResponse";
//                che = response.getChe().getName(); TODO:
                Matcher cheMatcher = cheNamePattern.matcher(payload);
                if (cheMatcher.find()) {
                    che = cheMatcher.group(1);
                }
            }

            if (xmlMessage.getAckEvent() instanceof WaUpdatedResponse response) {
                eventType = "WaUpdatedResponse";
                che = response.getChe().getName();
            }
            if (xmlMessage.getAckEvent() instanceof WaCancelledResponse response) {
                eventType = "WaCancelledResponse";
//                che = response.getChe().getName(); TODO:
                Matcher cheMatcher = cheNamePattern.matcher(payload);
                if (cheMatcher.find()) {
                    che = cheMatcher.group(1);
                }
            }
            if (xmlMessage.getAckEvent() instanceof WaAbortedResponse response) {
                eventType = "WaAbortedResponse";
                che = response.getChe().getName();
            }
            if (xmlMessage.getAckEvent() instanceof WaSuspendedResponse response) {
                eventType = "WaSuspendedResponse";
//                che = response.getChe().getName(); TODO:
                Matcher cheMatcher = cheNamePattern.matcher(payload);
                if (cheMatcher.find()) {
                    che = cheMatcher.group(1);
                }
            }
            /*
             * CheActivityEvent
             */
            if (nonNull(xmlMessage.getCheActivityEvent())) {
                var activity = xmlMessage.getCheActivityEvent();
                che = activity.getChe().getName();
                if (activity.getActivity() instanceof CheDriveActivity driveActivity) {
                    eventType = "CheDriveActivity";
                    if (nonNull(driveActivity.getAction())) {
                        action = driveActivity.getAction().toString();
                    }
                    if (nonNull(driveActivity.getLocation())) {
                        fromPosition = getPositionData(driveActivity.getLocation());
                    }
                }

                if (activity.getActivity() instanceof ContainerActivity containerActivity) {
                    eventType = "ContainerActivity";
                    if (nonNull(containerActivity.getAction())) {
                        action = containerActivity.getAction().value();
                        if(action.equalsIgnoreCase("picksingle")){
                            fromPosition = getPositionData(containerActivity.getPickTarget().get(0).getPosition());
                        }
                        if(action.equalsIgnoreCase("placesingle")){
                            toPosition = getPositionData(containerActivity.getPlaceTarget().get(0).getPosition());
                        }
                    }
                }
            }

            /*
             * CheStatusEvent
             */
            if (xmlMessage.getCheStatusEvent() instanceof OpStateUpdated opStateUpdated) {
                eventType = "OpStateUpdated";
                che = opStateUpdated.getChe().getName();
                if (nonNull(opStateUpdated.getChe().getOperatorState())) {
                    action = opStateUpdated.getChe().getOperatorState().toString();
                }
            }

            /*
             * SystemEvent
             */
            if (xmlMessage.getSystemEvent() instanceof SystemException) {
                eventType = "SystemException";
            }

            /*
             * Calculate Time Taken
             */
            Long timeTaken = null;
            Instant currentTime = Instant.parse(logTimestamp);
            /*
             * Response message
             */
            if (!pingId.isEmpty()) {
                Instant requestTime = msgTimeMap.get(pingId);
                if (requestTime != null) {
                    timeTaken = Duration.between(requestTime, currentTime).toMillis();
                }
            }

            /*
             * Store request timestamp
             */
            if (!msgId.isEmpty()) {
                msgTimeMap.put(msgId, currentTime);
            }

            /*
             * Write Excel Row
             */
            Row row = sheet.createRow(rowNum++);

            int col = 0;

            row.createCell(col++).setCellValue(source);
            row.createCell(col++).setCellValue(logTimestamp);
            row.createCell(col++).setCellValue(msgId);
            row.createCell(col++).setCellValue(pingId);
            row.createCell(col++).setCellValue(che);
            row.createCell(col++).setCellValue(eventType);
            row.createCell(col++).setCellValue(action);
            row.createCell(col++).setCellValue(jobCount != null ? String.valueOf(jobCount) : "");
            row.createCell(col++).setCellValue(container);
            row.createCell(col++).setCellValue(fromPosition);
            row.createCell(col++).setCellValue(toPosition);
            if (timeTaken != null) {
                row.createCell(col).setCellValue(timeTaken);
            } else {
                row.createCell(col).setCellValue("");
            }
        }
        /**
         * autoSize
         */
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        Files.createDirectories(outputFile.getParent());

        Files.createDirectories(
                excelFile.getParent()
        );

        try (FileOutputStream fos = new FileOutputStream(excelFile.toFile())) {
            workbook.write(fos);
        }
        workbook.close();
        System.out.println("Excel file generated: " + excelFile);
    }

    static String getPositionData(Location position) {
        if (position instanceof YardSlot yardSlot) {
            return "YardSlot: " + yardSlot.getBlock() + yardSlot.getRow() + yardSlot.getCol() + "." + yardSlot.getTier();
        }
        if (position instanceof TruckSlot truckSlot) {
            var pos = nonNull(truckSlot.getPosOnChassis()) ? "(" + truckSlot.getPosOnChassis() + ")" : null;
            return "TruckSlot: " + truckSlot.getTruckId() + pos;

        }
        if (position instanceof YardSection yardSection) {
            return "YardSection: " + yardSection.getBlock() + yardSection.getRow();
        }
        if (position instanceof QcTarget qcTarget) {
            return "QcTarget: " + qcTarget.getQcId();
        }
        if(position instanceof CheSpreader){
            return "CheSpreader: ";
        }
        if (position instanceof UnspecifiedPosition) {
            return "UnspecifiedPosition";
        }
        return "Position Not Handled";
    }
}

/** Defect Che object missing
 * <?xml version="1.0" encoding="utf-8"?>
 * <Message xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" msgId="20260526033807257" timeStamp="2026-05-26T03:38:07.2576504+03:00" pong="true" pingId="-2929285">
 * 	<AckEvent xsi:type="WaListUpdatedResponse" status="OK">
 * 		<AckMessage code=""/>
 * 		<Che name="R105" equipType="Rtg"/>
 * 	</AckEvent>
 * </Message>
 * {
 *   "type": "jobListUpdateAcknowledged",
 *   "message": {
 *     "msgType": "WaListUpdatedResponse",
 *     "msgId": "20260526033807257",
 *     "timestamp": "2026-05-26T03:38:07.2576504+03:00",
 *     "correlationKey": "20260526033807257"
 *   },
 *   "status": "Ok",
 *   "ackMessage": {
 *     "code": "",
 *     "value": ""
 *   }
 * }
 *
 */
