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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFormatter {

    public static void main(String[] args) throws Exception {
        /**
         * Grafana Query:  |~ `receiveRTGMessage|Payload: <Message`
         */
        Path inputFile = Path.of("C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/input.txt");
        Path outputFile = Path.of("C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/formatted-output.txt");

        String content = Files.readString(inputFile);

        Pattern pattern = Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2}T\\S+).*?Payload:\\s*(<\\?xml.*?</Message>|<Message.*?</Message>)",
                Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(content);

        StringBuilder output = new StringBuilder();

        while (matcher.find()) {

            String timestamp = matcher.group(1);
            String payload = matcher.group(2).trim();

            boolean isBms = payload.startsWith("<?xml");

            String type = isBms
                    ? "BMS Message"
                    : "AIL Message";

            String formattedXml = formatXml(payload);

            output.append(timestamp)
                    .append(" : ")
                    .append(type)
                    .append(":\n")
                    .append(formattedXml)
                    .append("\n");
        }

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