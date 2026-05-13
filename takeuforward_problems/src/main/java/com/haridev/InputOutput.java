package com.haridev;

import java.util.Scanner;

public class InputOutput {
    public static void main(String[] args){
        var inputOutput = new InputOutput();
        var sc = new Scanner(System.in);
        inputOutput.primeNumber(sc);
    }
    public void primeNumber(Scanner sc){
        System.out.println("Enter the number to check if it is prime or not: ");
        var number = sc.nextInt();
        if(number<2){
            System.out.println(number + " is not a prime number");
            return;
        }else if(number == 2){
            System.out.println(number + " is a prime number");
            return;
        }else if(number%2==0){
            System.out.println(number + " is not a prime number");
            return;
        }else if(number == 3){
            System.out.println(number + " is a prime number");
            return;
        }
        System.out.println("Enter the number to check if it is prime or not: " + number);
    }
}
