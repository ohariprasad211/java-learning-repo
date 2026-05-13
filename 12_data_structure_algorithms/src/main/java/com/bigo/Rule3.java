package com.bigo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rule 3: Different terms for inputs
 */
@Slf4j
public class Rule3 {
    public static void main(String[] args) {
        var list =Arrays.asList("apple", "banana", "elephant", "egg", "banana", "mango");
        var sortedString = list.stream().filter(a->!a.startsWith("e")).sorted(Comparator.naturalOrder()).toList();
        sortedString.stream().distinct().forEach(System.out::println);

        var boxes1 = Arrays.asList(1, 2, 3, 5, 7, 10, 11, 12);
        var boxes2 = Arrays.asList(5, 6);
        compressBoxes(boxes1, boxes2);
//        System.out.println();

    }
    /**
     * BIG O notation of the compressBoxes method is O(a+b)
     * We can't resolve the O(a+b) as O(n) because the BIG O notation of each method input varies
     */
    public static void compressBoxes(List<Integer> boxes1, List<Integer> boxes2){
        //BiG O notation of first Array List is O(a)
        for(var box : boxes1){
            log.info("Boxes1: {}", box);
        }

        //BiG O notation of second Array List is O(b)
        for(var box : boxes2){
            log.info("Boxes2: {}", box);
        }
    }
}
