package com.learning.core.basics.controlflow;

public class Recursive {
    public static void main(String[] args) {
//        System.out.println(fact(4));
        printFunction(4);
    }


    public static Integer fact(int n) {
        if (n == 0) {
            return 1;
        } else if (n == 1) {
            return 1;
        } else {
            return fact(n - 1);
        }
    }

    public static int printFunction(int n) {
        if (n == 0)
            return 0;
        else {
            System.out.println(n);
            return printFunction(n - 1);
        }
    }
}
