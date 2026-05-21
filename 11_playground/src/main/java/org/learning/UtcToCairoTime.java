package org.learning;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UtcToCairoTime {
    public static void main(String[] args) {
        // Example list of UTC timestamps
        List<String> utcTimes = Arrays.asList(
        "2026-05-17T08:30:55.031Z"


        );

        ZoneId cairoZone = ZoneId.of("Africa/Cairo");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

        for (String utc : utcTimes) {
            Instant instant = Instant.parse(utc);
            ZonedDateTime cairoTime = instant.atZone(cairoZone);
            System.out.println(cairoTime.format(formatter));
        }
    }
}
