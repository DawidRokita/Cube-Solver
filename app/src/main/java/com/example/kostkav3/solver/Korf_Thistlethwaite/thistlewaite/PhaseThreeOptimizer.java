package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PhaseThreeOptimizer {
    HashMap<Integer,byte[]> cornerPermutationMap=new HashMap<>();
    HashMap<Integer,byte[]> middleSlicePermutationMap=new HashMap<>();
    HashMap<Integer,byte[]> edgePermutationMap=new HashMap<>();
    public static void main(String[] args){
        ArrayList<Byte> current=new ArrayList<>();
        ArrayList<Byte> allowed= new ArrayList<Byte>(Arrays.asList(new Byte[]{0,1,2,3,4,5,6,7,8,9,10,11}));
        PhaseThreeOptimizer optimizer=new PhaseThreeOptimizer();
        optimizer.generateMiddleSlicePermutations(current,allowed);

    }

    public RubiksCube generateRandomPhaseThreeState(){
        return new RubiksCube();
    }
    public void initializeMaps(){

    }
    public void generateCornerPermutations(ArrayList<Byte> current, ArrayList<Byte> allowed){
        if(current.size()==8){
            cornerPermutationMap.put(cornerPermutationMap.size(),convertToArray(current));
            System.out.println(current);
        }else{
            for(byte b:allowed){
                current.add(b);
                allowed.remove(b);
                generateCornerPermutations((ArrayList<Byte>)current.clone(),(ArrayList<Byte>)allowed.clone());
            }
        }

    }
    public void generateMiddleSlicePermutations(ArrayList<Byte> current, ArrayList<Byte> allowed){
        if(current.size()==4){
            cornerPermutationMap.put(cornerPermutationMap.size(),convertToArray(current));
            System.out.println(current);
        }else{
            for(byte b:allowed){
                ArrayList<Byte> currentCopy= (ArrayList<Byte>) current.clone();
                ArrayList<Byte> allowedCopy=(ArrayList<Byte>)allowed.clone();
                currentCopy.add(b);
                allowedCopy.remove(Byte.valueOf(b));

                generateMiddleSlicePermutations(currentCopy,allowedCopy);
            }
        }

    }
    public void generateEdgePermutations(ArrayList<Byte> current, ArrayList<Byte> allowed){
        if(current.size()==8){
            cornerPermutationMap.put(cornerPermutationMap.size(),convertToArray(current));
            System.out.println(current);
        }else{
            for(byte b:allowed){
                current.add(b);
                allowed.remove(b);
                generateEdgePermutations((ArrayList<Byte>)current.clone(),(ArrayList<Byte>)allowed.clone());
            }
        }

    }

    public byte[] convertToArray(ArrayList<Byte> list){
        byte[] array=new byte[list.size()];
        for(int i=0;i<list.size();i++){
            array[i]=list.get(i);
        }
        return array;
    }

}
