package org.learning;

public class BitwiseLogicalOperators {
    public static void main(String[] args) {
        String[] binary = {
                "0000", "0001", "0010", "0011",
                "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011",
                "1100", "1101", "1110", "1111"
        };
        System.out.println(binary[0]);
        int a = 3;//0011
        int b = 6;//0110
        int c = a | b;
        int d = a & b;
        int e = a ^ b;
        int f = (~a & b) | (a & ~b);
        int g = ~a & 0x0F;
        System.out.println("Negation of a: " + ~a);
        System.out.println("Bitwise AND: " + d);
        System.out.println("Bitwise XOR: " + e);
        System.out.println("Expression: " + f);
    }
}
