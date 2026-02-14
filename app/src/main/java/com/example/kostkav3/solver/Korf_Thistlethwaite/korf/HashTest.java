package com.example.kostkav3.solver.Korf_Thistlethwaite.korf;

import java.util.Arrays;
import java.util.HashMap;

public class HashTest {
    // Utility function to swap two characters in a character array
    static HashMap<Integer,Integer> keyMap =new HashMap<Integer,Integer>();
    static int collisions=0;
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Recursive function to generate all permutations of a String
    private static void permutations(int[] array, int currentIndex) {

        if (currentIndex == array.length - 1) {
            int hash=Arrays.hashCode(array);
            if (keyMap.get(hash)==null||keyMap.get(hash)<1) {
                keyMap.put(hash, 1);
            }
            else{
                System.out.println("Collision!");
                System.out.println(Arrays.toString(array));
                System.out.println(keyMap.get(hash));
                collisions++;
            }

        }

        for (int i = currentIndex; i < array.length; i++) {
            swap(array, currentIndex, i);
            permutations(array, currentIndex + 1);
            swap(array, currentIndex, i);
        }
    }
    public static int hash(int[] array){
        int hash=0;
        for(int i=0;i<array.length;i++){
            hash+=array[i]*factorial(i+1);
        }
        return hash;
    }

    // generate all permutations of a String in Java
    public static void main(String[] args) {
        int[] testArray=new int[]{0,1,2,3,4,5,6,7};
        permutations(testArray, 0);
        //System.out.println(hash(new int[]{0,0,0,0,0,0,6,5}));
        System.out.println(collisions);
    }
    public static int factorial(int number){
        if(number==1||number==0){
            return 1;
        }else{
            return number*factorial(number-1);
        }
    }
}
