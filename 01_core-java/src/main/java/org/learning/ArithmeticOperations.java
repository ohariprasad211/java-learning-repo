package org.learning;


public class ArithmeticOperations {
    public static void main(String[] args) {
        int a = 10;
        // Prefix increment
        int b = ++a;
        System.out.println("Value of a after pre-increment: " + a + " and value of b: " + b);

        // Postfix increment: Current value of a is used in the expression, then a is incremented
        int c = a++;
        System.out.printf("Value of a after post-increment: %d and value of c: %d%n", a, c);
        System.out.printf("""
                Value of a after post-increment: %d and value of c: %d
                """, a, c);
        System.out.println("""
                Value of a after post-increment: %d and value of c: %d
                """.formatted(a, c));
    }
}
