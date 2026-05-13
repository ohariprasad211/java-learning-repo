package org.learning._01_primitiveTypes;

public class PrimitiveTypes {
    public static void main(String[] args){
        // Primitive types in Java
        byte b = 10;          // 8-bit signed integer
        short s = 1000;      // 16-bit signed integer
        int i = 100000;      // 32-bit signed integer
        long l = 100000L;    // 64-bit signed integer
        float f = 10.5f;     // 32-bit floating point
        double d = 20.99;    // 64-bit floating point
        char c = 'A';        // single 16-bit Unicode character
        boolean bool = true; // true or false

        System.out.println("Byte: " + b + " Min: " + Byte.MIN_VALUE + " Max: " + Byte.MAX_VALUE);
        System.out.println("Short: " + s + " Min: " + Short.MIN_VALUE + " Max: " + Short.MAX_VALUE);
        System.out.println("Integer: " + i + " Min: " + Integer.MIN_VALUE + " Max: " + Integer.MAX_VALUE);
        System.out.println("Long: " + l + " Min: " + Long.MIN_VALUE + " Max: " + Long.MAX_VALUE);
        System.out.println("Float: " + f + " Min: " + Float.MIN_VALUE + " Max: " + Float.MAX_VALUE);
        System.out.println("Double: " + d + " Min: " + Double.MIN_VALUE + " Max: " + Double.MAX_VALUE);
        System.out.println("Character: " + c + " Unicode: " + (int)c);
        System.out.println("Boolean: " + bool);
    }
}
class CharDemo{
    public static void main(String[] args) {
        char ch1, ch2;
        ch1=88;
        ch2='Y';
        System.out.println(ch1);
        System.out.println(ch2);
    }
}
class CharDemo2{
    public static void main(String[] args) {
        char ch1;
        ch1 = 'X';
        System.out.println("ch1: " + ch1);
        ch1++;
        System.out.println("ch1 after increment: " + ch1);
    }
}
