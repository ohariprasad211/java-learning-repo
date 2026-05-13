package org.learning.arrays;

public class PrintArrayUsingRecursive {
    public static void main(String[] args) {
        var recTest = new RecTest(10);
        for(var i = 0; i<10; i++){
            recTest.values[i]=i;
        }
        recTest.printArray(10);
    }
}
class RecTest{
    int[] values;
    RecTest(int i) {
        this.values = new int[i];
    }
    void printArray(int i){
        System.out.println("I= " + i);
        if(i==0) return;
        else printArray(i-1);
        System.out.println("["+(i+1)+"] " + values[i]);
    }
}
