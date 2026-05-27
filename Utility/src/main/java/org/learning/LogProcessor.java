package org.learning;

import net.apmoller.crb.apmt.microservices.ail.model.apmt.xml.*;
import net.apmoller.crb.apmt.microservices.ail.utility.CommonUtility;
import net.apmoller.crb.apmt.microservices.ail.utility.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogProcessor {
    public static void main(String[] args) throws IOException {
        Path inputFile = Path.of(
//                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/input.txt"
        );
        Path outputFile = Path.of(
//                "C:/Drive/MyCodeBase/GitCodeBase/java-learning-repo/Utility/src/main/resources/formatted-output.txt"
                "C:/Drive/MyCodeBase/java-learning-repo/Utility/src/main/resources/formatted-output.txt"
        );
        String content = Files.readString(inputFile);
        Pattern pattern = Pattern.compile("<Message.*?</Message>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        /**
         * CheActivityResponse
         * CheDriveActivity:ArrivedAtLocation
         * CheDriveActivity:ArrivedAtOrigin
         * ContainerActivity:PickSingle
         * ContainerActivity:PlaceSingle
         * OpStateUpdated(Engaged)
         * OpStateUpdated(Disengaged)
         * OpStateUpdatedResponse
         * WaAccepted
         * WaAcceptedResponse
         * WaCreated
         * WaCreatedResponse
         * WaListUpdated
         * WaListUpdatedResponse
         * WaUpdated
         * WaUpdatedResponse
         */

        var messages = new ArrayList<Message>();
        while (matcher.find()) {
            var xmlMessage = Utils.getXmlObject(matcher.group(), Message.class);
            xmlMessage.getMsgId();
            xmlMessage.getPingId();
            // WaStatusEvent(
            if( xmlMessage.getWaStatusEvent() instanceof WaListUpdated){
                System.out.println("Instance of WaListUpdated");
            }
            if(xmlMessage.getWaStatusEvent() instanceof WaCreated){

            }
            if(xmlMessage.getWaStatusEvent() instanceof WaUpdated){

            }
            if(xmlMessage.getWaStatusEvent() instanceof WaAccepted){

            }
            if(xmlMessage.getWaStatusEvent() instanceof WaCancelled){

            }
            // AckEvent()
            if(xmlMessage.getAckEvent() instanceof WaCreatedResponse){

            }
            if(xmlMessage.getAckEvent() instanceof CheActivityResponse){

            }
            if(xmlMessage.getAckEvent() instanceof OpStateUpdatedResponse){

            }
            if(xmlMessage.getAckEvent() instanceof WaAcceptedResponse){

            }
            if(xmlMessage.getAckEvent() instanceof WaListUpdatedResponse){

            }
            if(xmlMessage.getAckEvent() instanceof WaUpdatedResponse){

            }
            // getCheActivityEvent()
            if(xmlMessage.getCheActivityEvent().getActivity() instanceof CheDriveActivity){
                // ArrivedAtLocation, ArrivedAtOrigin and ArrivedAtDest

            }
            if(xmlMessage.getCheActivityEvent().getActivity() instanceof ContainerActivity){
//                Need to handle Pick and Place
            }

            // CheStatusEvent()
            if(xmlMessage.getCheStatusEvent() instanceof OpStateUpdated){
            // Need to handle Engaged and DisEngage
            }
            if(xmlMessage.getSystemEvent() instanceof SystemException){

            }
            messages.add(xmlMessage);
        }

    }
}
