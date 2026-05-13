package com.haridev;

import java.util.Arrays;

public class CumulativeSum {
    public static void main(String[] args) {
        var array = new int[]{1,2,3,4};
        var resultArray = new int[4];
        int temp =0;
        for (var i = 0; i<array.length; i++){
            var result = temp + array[i];
            temp = result;
            resultArray[i] = result;
        }
        Arrays.stream(resultArray)
                .forEach(System.out::println);
    }
}
