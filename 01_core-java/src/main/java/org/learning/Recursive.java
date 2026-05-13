package org.learning;

public class Recursive {
    public static void main(String [] args){
        Integer number = 5;
        System.out.println("Factorial of " + number + " is " + factorial(number));

    }
    private static long factorial(Integer n){
        if(n==0 || n==1){
            return 1;
        }
        return n * factorial(n-1);
    }
}
