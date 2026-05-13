package org.learning;

import static java.util.Optional.ofNullable;

public class Practice {
    public static void main(String[] args) {
        var str = "10 ";
        ofNullable(str)
                .map(String::trim)
                .map(Integer::valueOf)
                .ifPresent(System.out::println);
    }
}
