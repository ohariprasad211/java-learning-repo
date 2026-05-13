package org.learning;

public class ArgPassByValue {
    public static void main(String[] args) {
        //Primitive types are passed by value
        var ob = new Test();
        int a = 15, b = 20;
        System.out.println("a and b before call: " + a + " " + b);
        ob.meth(a, b);
        System.out.println("a and b after call: " + a + " " + b);
    }

}

class Test {
    void meth(int i, int j) {
        i *= 2;
        j /= 2;
    }
}