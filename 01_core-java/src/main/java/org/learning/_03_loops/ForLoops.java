package org.learning._03_loops;

public class ForLoops {
    public static void main(String[] args) {
        // Use for-each style for one a two dimensional array
        int sum;
        int[][] nums = new int[3][5];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                nums[i][j] = (i + 1) * (j + 1);
            }
        }
        // Displaying the 2D array using for-each loop
        for (int[] x : nums) {
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }

    }
}
