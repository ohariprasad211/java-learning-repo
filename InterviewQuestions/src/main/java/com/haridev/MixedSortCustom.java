package com.haridev;

public class MixedSortCustom {
    public static void main(String[] args) {
        Object[] arr = {1, 4, 2, 3, 6, 'r', 'a'};

        // Separate numbers and characters
        int numCount = 0, charCount = 0;
        for (Object obj : arr) {
            if (obj instanceof Integer) numCount++;
            else if (obj instanceof Character) charCount++;
        }

        int[] numbers = new int[numCount];
        char[] chars = new char[charCount];

        int ni = 0, ci = 0;
        for (Object obj : arr) {
            if (obj instanceof Integer) numbers[ni++] = (Integer) obj;
            else if (obj instanceof Character) chars[ci++] = (Character) obj;
        }

        // Sort numbers using Bubble Sort
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = 0; j < numbers.length - i - 1; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }

        // Sort characters using Bubble Sort
        for (int i = 0; i < chars.length - 1; i++) {
            for (int j = 0; j < chars.length - i - 1; j++) {
                if (chars[j] > chars[j + 1]) {
                    char temp = chars[j];
                    chars[j] = chars[j + 1];
                    chars[j + 1] = temp;
                }
            }
        }

        // Merge results
        Object[] result = new Object[numbers.length + chars.length];
        int idx = 0;
        for (int n : numbers) result[idx++] = n;
        for (char c : chars) result[idx++] = c;

        // Print result
        System.out.print("Sorted Result: [");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
