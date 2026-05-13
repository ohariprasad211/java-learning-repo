package org.learning._01_primitiveTypes;

public class Variables {
    public static void main(String[] args) {
        /**
         * Declaring a variable
         * type identifier [= value][, identifier [= value]...];
         */
        int a, b, c;
        int d = 3, e, f = 5;
        byte z = 22;
        double pi = 3.14159;
        char x = 'X';

        // Dynamic initialization
        double a1=3.0, b1 = 4.0;
        double c1 = Math.sqrt( a1 * a1 + b1 * b1 );
        System.out.println("c1 = " + c1);

    }
}
