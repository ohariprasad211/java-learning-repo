package org.learning;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogFormatter {

    public static void main(String[] args) throws Exception {
        /**
         * Query to extract the relevant log lines from Grafana: Xml-mq adapter logs contain both BMS and AIL messages.
         * To filter and format these logs for better readability, you can use the following Grafana query:
         * Grafana Query:  |~ `receiveRTGMessage|Payload: <Message`|~`R101|System`
         * |~ `receiveRTGMessage|Payload: <Message|System`
         * |= `R101`|~`receiveRTGMessage|Received ECN4 message with Key:|Payload: <Message`|= `20260524094825192`
         */
        Path inputFile = Path.of(
                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
//                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
        );
        Path outputFile = Path.of(
                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/formatted-output.txt"
//                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/formatted-output.txt"
        );

        String content = Files.readString(inputFile);

        Pattern pattern = Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2}T\\S+).*?Payload:\\s*(AIL|ECS)\\s+Message:\\s*(<\\?xml.*?</Message>|<Message.*?</Message>)",
                Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(content);

        StringBuilder output = new StringBuilder();
        List<SummaryEntry> summaryEntries = new ArrayList<>();
        Map<String, String> requestTimeMap = new HashMap<>();
        Map<String, String> ailRequestMap = new HashMap<>();

        Pattern msgIdPattern = Pattern.compile("msgId=\"([^\"]+)\"");
        Pattern pingIdPattern = Pattern.compile("pingId=\"([^\"]+)\"");

        while (matcher.find()) {

            String timestamp = matcher.group(1);
            String sourceType = matcher.group(2);
            String payload = matcher.group(3).trim();
            boolean isBms = "ECS".equals(sourceType);
            String type = sourceType + " Message";

            String formattedXml = formatXml(payload);

            Matcher msgIdMatcher = msgIdPattern.matcher(payload);
            Matcher pingIdMatcher = pingIdPattern.matcher(payload);

            String msgId = "";
            String pingId = "";

            if (msgIdMatcher.find()) {
                msgId = msgIdMatcher.group(1);
            }

            if (pingIdMatcher.find()) {
                pingId = pingIdMatcher.group(1);
            }

            Long timeTaken = null;

            Instant currentTime = Instant.parse(timestamp);

            /*
             * Store AIL outgoing messages
             */
            if (!isBms && !msgId.isEmpty()) {

                ailRequestMap.put(msgId, timestamp);
            }

            /*
             * Normal AIL response to BMS request
             */
            if (!isBms && !msgId.isEmpty()) {

                String requestTimestamp = requestTimeMap.get(msgId);

                if (requestTimestamp != null) {

                    Instant requestTime = Instant.parse(requestTimestamp);

                    timeTaken = Duration
                            .between(requestTime, currentTime)
                            .toMillis();
                }
            }

            /*
             * BMS pong response to AIL request
             */
            if (isBms && !pingId.isEmpty()) {

                String ailRequestTimestamp = ailRequestMap.get(pingId);

                if (ailRequestTimestamp != null) {

                    Instant ailRequestTime =
                            Instant.parse(ailRequestTimestamp);

                    timeTaken = Duration
                            .between(ailRequestTime, currentTime)
                            .toMillis();
                }
            }
            if (timeTaken != null) {

                summaryEntries.add(
                        new SummaryEntry(
                                timestamp,
                                type,
                                msgId,
                                pingId,
                                timeTaken
                        )
                );
            }

            /*
             * Store incoming BMS requests
             */
            if (isBms && !msgId.isEmpty()) {

                requestTimeMap.put(msgId, timestamp);
            }

            output.append(timestamp)
                    .append(" : ")
                    .append(type);

            if (!msgId.isEmpty()) {

                output.append("\nMsgId : ")
                        .append(msgId);
            }

            if (!pingId.isEmpty()) {

                output.append("\nPingId : ")
                        .append(pingId);
            }

            if (timeTaken != null) {

                output.append("\nTime Taken : ")
                        .append(timeTaken)
                        .append(" ms");
            }

            output.append("\n")
                    .append(formattedXml)
                    .append("\n");
        }
        output.append("\n\n================ SUMMARY ================\n\n");

        summaryEntries.stream()
                .sorted((a, b) -> Long.compare(b.timeTaken, a.timeTaken))
                .forEach(entry -> {

                    output.append(entry.timestamp)
                            .append(" : ")
                            .append(entry.type);

                    if (!entry.msgId.isEmpty()) {

                        output.append(" : MsgId : ")
                                .append(entry.msgId);
                    }

                    if (!entry.pingId.isEmpty()) {

                        output.append(" PingId : ")
                                .append(entry.pingId);
                    }

                    output.append(" : Time Taken : ")
                            .append(entry.timeTaken)
                            .append(" ms\n");
                });
        output.append("\n\n================ AVERAGE TIME ================\n\n");

        long bmsCount = summaryEntries.stream()
                .filter(entry ->
                        "ECS Message".equals(entry.type)
                                && entry.timeTaken != null)
                .count();

        double bmsAverage = summaryEntries.stream()
                .filter(entry ->
                        "ECS Message".equals(entry.type)
                                && entry.timeTaken != null)
                .mapToLong(entry -> entry.timeTaken)
                .average()
                .orElse(0);

        long ailCount = summaryEntries.stream()
                .filter(entry ->
                        "AIL Message".equals(entry.type)
                                && entry.timeTaken != null)
                .count();

        double ailAverage = summaryEntries.stream()
                .filter(entry ->
                        "AIL Message".equals(entry.type)
                                && entry.timeTaken != null)
                .mapToLong(entry -> entry.timeTaken)
                .average()
                .orElse(0);

        output.append("ECS Message Count : ")
                .append(bmsCount)
                .append("\n");

        output.append("ECS Message Average Time Taken : ")
                .append(String.format("%.2f", bmsAverage))
                .append(" ms\n\n");

        output.append("AIL Message Count : ")
                .append(ailCount)
                .append("\n");

        output.append("AIL Message Average Time Taken : ")
                .append(String.format("%.2f", ailAverage))
                .append(" ms\n");
        Files.createDirectories(outputFile.getParent());

        Files.writeString(
                outputFile,
                output.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        System.out.println("Done");
    }

    private static String formatXml(String xml) {

        try {

            boolean hasDeclaration = xml.startsWith("<?xml");

            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount",
                    "4"
            );

            if (hasDeclaration) {
                transformer.setOutputProperty(
                        OutputKeys.OMIT_XML_DECLARATION,
                        "no"
                );
            } else {
                transformer.setOutputProperty(
                        OutputKeys.OMIT_XML_DECLARATION,
                        "yes"
                );
            }

            StringWriter writer = new StringWriter();

            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(writer)
            );

            return writer.toString();

        } catch (Exception e) {
            return xml;
        }
    }
}
class SummaryEntry {

    String timestamp;
    String type;
    String msgId;
    String pingId;
    Long timeTaken;

    public SummaryEntry(
            String timestamp,
            String type,
            String msgId,
            String pingId,
            Long timeTaken
    ) {

        this.timestamp = timestamp;
        this.type = type;
        this.msgId = msgId;
        this.pingId = pingId;
        this.timeTaken = timeTaken;
    }
}