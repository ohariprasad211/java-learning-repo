package org.learning.streams;

import java.util.Arrays;

public class Streams {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Arrays.stream(numbers)
                .forEach(System.out::println);
        int sum = Arrays.stream(numbers)
                .sum();
        System.out.println("Sum of numbers: " + sum);
        Arrays.stream(numbers)
                .findAny()
                .ifPresent(System.out::println);
        Arrays.stream(numbers)
                .filter(number -> number % 2 == 0)
                .findAny()
                .ifPresent(System.out::println);
    }
}
