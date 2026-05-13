package org.learning;

public class ArgPassByReference {
    public static void main(String[] args) {
        var object = new Test1(15,20);
        System.out.println("a and b before call: " + object.a + " " + object.b);
        object.meth(object);
        System.out.println("a and b after call: " + object.a + " " + object.b);
    }
}
class Test1 {
    int a, b;
    Test1(int a, int b){
        this.a = a;
        this.b = b;
    }
    //Pass by reference
    void meth(Test1 ob){
        ob.a *= 2;
        ob.b /= 2;
    }
}
