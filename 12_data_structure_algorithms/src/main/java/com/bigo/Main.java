package com.bigo;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        var arrayList = new ArrayList<String>();
        for (var i = 0; i < 10000000; i++) {
            arrayList.add("nemo");
        }
        findNemo(arrayList);

    }

    public static void findNemo(List<String> arrayList) {
        var startTime = System.nanoTime();
        for (var item : arrayList) {
            if (item.equalsIgnoreCase("nemo")) {
                log.info("Found NEMO!");
            }
        }
        var endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        log.info("Call to find Nemo took: {} ms", String.format("%.6f", durationMs));
    }

    public static void findBoxes(List<Integer> boxList) {
        log.info("First Box: {}", boxList.indexOf(0)); //0(1)
        log.info("First Box: {}", boxList.indexOf(1)); //0(2)
    }

    public static Integer funChallenge(List<Integer> arrayList) {
        var a = 10; //O(1)
        a = 50 + 3; //O(1)
        for(var item: arrayList){ //O(n)
            //anotherMethod(); //O(n)
            var stranger = true; //O(n)
            a++; //O(n)
        }
        return a; //O(1)
    }
}