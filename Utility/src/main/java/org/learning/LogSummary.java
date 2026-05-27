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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogSummary {

    public static void main(String[] args) throws Exception {

        Path inputFile = Path.of(
                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
        );

        Path outputFile = Path.of(
                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/summary.txt"
        );

        String content = Files.readString(inputFile);

        Pattern logPattern = Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2}T\\S+).*?Payload:\\s*(<\\?xml.*?</Message>|<Message.*?</Message>)",
                Pattern.DOTALL
        );

        Pattern msgIdPattern =
                Pattern.compile("msgId=\"([^\"]+)\"");

        Pattern pingIdPattern =
                Pattern.compile("pingId=\"([^\"]+)\"");

        Pattern waStatusPattern =
                Pattern.compile(
                        "<WaStatusEvent[^>]*xsi:type=\"([^\"]+)\""
                );

        Pattern ackEventPattern =
                Pattern.compile(
                        "<AckEvent[^>]*xsi:type=\"([^\"]+)\""
                );

        Pattern activityPattern =
                Pattern.compile(
                        "<Activity[^>]*xsi:type=\"([^\"]+)\"[^>]*action=\"([^\"]+)\""
                );
        Pattern cheStatusPattern =
                Pattern.compile(
                        "<CheStatusEvent[^>]*xsi:type=\"([^\"]+)\""
                );

        Pattern operatorStatePattern =
                Pattern.compile(
                        "operatorState=\"([^\"]+)\""
                );

        Pattern cheNamePattern =
                Pattern.compile(
                        "<Che[^>]*name=\"([^\"]+)\""
                );
        Matcher matcher = logPattern.matcher(content);

        StringBuilder output = new StringBuilder();

        /*
         * Header
         */
        output.append(
                "Source|Timestamp|MsgId|PingId|CheName|EventType|TimeTaken_ms\n"
        );

        /*
         * Store request timestamps
         */
        Map<String, Instant> requestTimeMap =
                new HashMap<>();

        while (matcher.find()) {

            String timestamp = matcher.group(1);

            String payload = matcher.group(2).trim();

            /*
             * Identify source
             */
            String source =
                    payload.startsWith("<?xml")
                            ? "BMS"
                            : "AIL";

            /*
             * Extract msgId
             */
            String msgId = "";

            Matcher msgIdMatcher =
                    msgIdPattern.matcher(payload);

            if (msgIdMatcher.find()) {

                msgId = msgIdMatcher.group(1);
            }

            /*
             * Extract pingId
             */
            String pingId = "";

            Matcher pingIdMatcher =
                    pingIdPattern.matcher(payload);

            if (pingIdMatcher.find()) {

                pingId = pingIdMatcher.group(1);
            }

            /*
             * Extract event type
             */
            String eventType = "";

            Matcher cheStatusMatcher =
                    cheStatusPattern.matcher(payload);

            Matcher operatorStateMatcher =
                    operatorStatePattern.matcher(payload);

            Matcher waMatcher =
                    waStatusPattern.matcher(payload);

            Matcher ackMatcher =
                    ackEventPattern.matcher(payload);

            Matcher activityMatcher =
                    activityPattern.matcher(payload);

            /*
             * CheStatusEvent
             */
            if (cheStatusMatcher.find()) {

                eventType = cheStatusMatcher.group(1);

                if (operatorStateMatcher.find()) {

                    eventType =
                            eventType
                                    + "("
                                    + operatorStateMatcher.group(1)
                                    + ")";
                }
            }

            /*
             * WaStatusEvent
             */
            else if (waMatcher.find()) {

                eventType = waMatcher.group(1);
            }

            /*
             * AckEvent
             */
            else if (ackMatcher.find()) {

                eventType = ackMatcher.group(1);
            }

            /*
             * Activity
             */
            else if (activityMatcher.find()) {

                eventType =
                        activityMatcher.group(1)
                                + ":"
                                + activityMatcher.group(2);
            }

            /*
             * Calculate time taken
             */
            Long timeTaken = null;

            Instant currentTime =
                    Instant.parse(timestamp);

            if (!pingId.isEmpty()) {

                Instant requestTime =
                        requestTimeMap.get(pingId);

                if (requestTime != null) {

                    timeTaken =
                            Duration.between(
                                    requestTime,
                                    currentTime
                            ).toMillis();
                }
            }

            /*
             * Store current msgId timestamp
             */
            if (!msgId.isEmpty()) {

                requestTimeMap.put(
                        msgId,
                        currentTime
                );
            }
            String cheName = "";

            Matcher cheNameMatcher =
                    cheNamePattern.matcher(payload);

            if (cheNameMatcher.find()) {

                cheName = cheNameMatcher.group(1);
            }

            /*
             * Write row
             */
            output.append(source)
                    .append("|")
                    .append(timestamp)
                    .append("|")
                    .append(msgId)
                    .append("|")
                    .append(pingId)
                    .append("|")
                    .append(cheName)
                    .append("|")
                    .append(eventType)
                    .append("|")
                    .append(timeTaken != null
                            ? timeTaken
                            : "")
                    .append("\n");
        }

        Files.createDirectories(
                outputFile.getParent()
        );

        Files.writeString(
                outputFile,
                output.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        System.out.println(
                "Formatted file generated: "
                        + outputFile
        );
    }
}