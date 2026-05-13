package org.learning._02_arrays;

public class Arrays {
    public static void main(String[] args) {


    }
}
class OneDimensionalArray {
    public static void main(String[] args) {
        // One-dimensional array
        int[] arr; //Declaration
        arr = new int[5]; // Instantiation
        arr[0] = 10; // Initialization
        arr[1] = 20;
        arr[2] = 30;
        arr[3] = 40;
        arr[4] = 50;

        // Accessing elements
        for (int i = 0; i < arr.length; i++) {
            System.out.println("Element at index " + i + ": " + arr[i]);
        }

        /**
         * Auto Array Initialization
         */
        int[] month_days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        System.out.println("Days in February: " + month_days[1]);
    }
}

class MultiDimensionalArray {
    public static void main(String[] args) {
        int[][] twoD = new int[4][5];
        // This allocates a 4-by-5 array and assigns it to twoD.
        // Two-dimensional array
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        // Accessing elements
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
class ThreeDMatrix{
    public static void main(String[] args) {
        // Three-dimensional array
        int[][][] threeD = new int[2][3][4];
        // This allocates a 2-by-3-by-4 array and assigns it to threeD.

        // Initializing a 3D array
        int[][][] cube = {
            {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
            },
            {
                {13, 14, 15, 16},
                {17, 18, 19, 20},
                {21, 22, 23, 24}
            }
        };

        // Accessing elements
        for (int i = 0; i < cube.length; i++) {
            for (int j = 0; j < cube[i].length; j++) {
                for (int k = 0; k < cube[i][j].length; k++) {
                    System.out.print(cube[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
