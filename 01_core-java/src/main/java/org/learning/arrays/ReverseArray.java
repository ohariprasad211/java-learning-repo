package org.learning.arrays;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReverseArray {
    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        List<Integer> list = Arrays.asList(arr);
        Collections.reverse(list);
        arr = list.toArray(new Integer[0]);
        for (Integer integer : arr) {
            System.out.println(integer);
        }
    }
}
