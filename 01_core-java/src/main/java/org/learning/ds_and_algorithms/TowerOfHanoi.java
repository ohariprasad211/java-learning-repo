package org.learning.ds_and_algorithms;

public class TowerOfHanoi {
    public static void main(String[] args) {
        int numberOfDisks = 3;
        towerOfHanoi(numberOfDisks, 'A', 'B', 'C');
    }
    public static void towerOfHanoi(int n, char source, char auxiliary,  char destination) {
        if(n == 1){
            System.out.println("Move disk 1 from " + source + " to " + destination);
            return ;
        }
        // Step 1: Move n-1 disks from source to auxiliary
        towerOfHanoi(n-1, source, destination, auxiliary);
        // Step 2: Move the largest disk to destination
        System.out.println("Move disk " + n + " from " + source + " to " + destination);
        // Step 3: Move n-1 disks from auxiliary to destination
        towerOfHanoi(n-1, auxiliary, source, destination);
    }
}
