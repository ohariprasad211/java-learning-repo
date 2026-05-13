package com.bigo;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NestedLoops {
    public static void main(String[] args) {
        var boxes = Arrays.asList(1, 2, 3, 4, 5);
        /**
         * Log all pairs of array
         */
        for (var box : boxes) {
            for (var integer : boxes) {
                log.info("[{} {}]", box, integer);
            }
        }
/*        for(var it = boxes.listIterator(); it.hasNext();){
            var index = it.nextIndex();
            var box = it.next();
            System.out.println("Index: " + index + ", Value: " + box);
        }*/
    }
}
