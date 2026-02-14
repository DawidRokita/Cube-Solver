package com.example.kostkav3.solver.Korf_Thistlethwaite.korf;

import com.example.kostkav3.solver.Korf_Thistlethwaite.ByteArrayWrapper;
import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EdgeConfigurationNode {

    final static byte uf=0;
    final static byte ul=1;
    final static byte ub=2;
    final static byte ur=3;
    final static byte fr=4;
    final static byte fl=5;
    final static byte bl=6;
    final static byte br=7;
    final static byte db=8;
    final static byte dl=9;
    final static byte df=10;
    final static byte dr=11;



    byte[] edgeConfiguration;
    byte previousMove;
    byte distance;

    public static HashMap<ByteArrayWrapper,Integer> permutationHashes=new HashMap<ByteArrayWrapper, Integer>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ArrayList<Byte> current=new ArrayList<>();
        ArrayList<Integer> possibleCases= new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11));
        generatePermutationHashes(current,possibleCases);
        //System.out.println(Arrays.toString(permutationHashes.entrySet().toArray()));
//        System.out.println(new EdgeConfigurationNode(new byte[]{22,20,18,16,14,12,0,2,4,6,8,11}).getSecondOrientationHash());


    }
    public EdgeConfigurationNode(){
        this.edgeConfiguration=new byte[]{0,2,4,6,8,10,12,14,16,18,20,22};
        this.previousMove= RubiksCube.I;
        this.distance=0;
    }
    public EdgeConfigurationNode(byte[] state){
        this.edgeConfiguration=state.clone();
        this.previousMove=RubiksCube.I;
        this.distance=0;
    }

    public EdgeConfigurationNode doMove(byte move){
        EdgeConfigurationNode node=this.clone();
        switch(move){
            case RubiksCube.R:

                node.doGenericEdgeMove(ur,br,dr,fr,
                        fr,ur,br,dr,
                        0);
                break;
            case RubiksCube.R_PRIME:

                node.doGenericEdgeMove(ur,br,dr,fr,
                        br,dr,fr,ur,
                        0);
                break;
            case RubiksCube.R2:

                node.doGenericEdgeMove(ur,br,dr,fr,
                        dr,fr,ur,br,
                        0);
                break;
            case RubiksCube.L:


                node.doGenericEdgeMove(ul,fl,dl,bl,
                        bl,ul,fl,dl,
                        0);
                break;
            case RubiksCube.L_PRIME:

                node.doGenericEdgeMove(ul,fl,dl,bl,
                        fl,dl,bl,ul,
                        0);
                break;
            case RubiksCube.L2:

                node.doGenericEdgeMove(ul,fl,dl,bl,
                        dl,bl,ul,fl,
                        0);
                break;
            case RubiksCube.U:

                node.doGenericEdgeMove(uf,ul,ub,ur,
                        ur,uf,ul,ub,
                        0);
                break;
            case RubiksCube.U_PRIME:

                node.doGenericEdgeMove(uf,ul,ub,ur,
                        ul,ub,ur,uf,
                        0);
                break;
            case RubiksCube.U2:

                node.doGenericEdgeMove(uf,ul,ub,ur,
                        ub,ur,uf,ul,
                        0);
                break;
            case RubiksCube.D:

                node.doGenericEdgeMove(db,dl,df,dr,
                        dr,db,dl,df,
                        0);
                break;
            case RubiksCube.D_PRIME:

                node.doGenericEdgeMove(db,dl,df,dr,
                        dl,df,dr,db,
                        0);
                break;
            case RubiksCube.D2:

                node.doGenericEdgeMove(db,dl,df,dr,
                        df,dr,db,dl,
                        0);
                break;
            case RubiksCube.F:

                node.doGenericEdgeMove(uf,fr,df,fl,
                        fl,uf,fr,df,
                        1);
                break;
            case RubiksCube.F_PRIME:

                node.doGenericEdgeMove(uf,fr,df,fl,
                        fr,df,fl,uf,
                        1);
                break;
            case RubiksCube.F2:

                node.doGenericEdgeMove(uf,fr,df,fl,
                        df,fl,uf,fr,
                        0);
                break;
            case RubiksCube.B:

                node.doGenericEdgeMove(db,br,ub,bl,
                        bl,db,br,ub,
                        1);
                break;
            case RubiksCube.B_PRIME:

                node.doGenericEdgeMove(db,br,ub,bl,
                        br,ub,bl,db,
                        1);
                break;
            case RubiksCube.B2:

                node.doGenericEdgeMove(db,br,ub,bl,
                        ub,bl,db,br,
                        0);
                break;
            default:
                System.out.println("Wrong move");
                break;

        }
        node.previousMove=move;
        node.distance=(byte)(this.distance+1);
        return node;
    }
    public int getFirstOrientationHash(){
        int hash=0;
        for(int i=0;i<6;i++){
            byte orientation=(byte)(edgeConfiguration[i]%2);
            hash+=orientation*Math.pow(2,5-i);
        }
        return hash;
    }
    public int getSecondOrientationHash(){
        int hash=0;
        for(int i=6;i<12;i++){
            byte orientation=(byte)(edgeConfiguration[i]%2);
            hash+=orientation*Math.pow(2,11-i);
        }
        return hash;
    }
    public int getFirstPermutationHash(){
        byte[] array=Arrays.copyOfRange(this.edgeConfiguration,0,6);
        byte[] permutations=new byte[array.length];
        for(int i=0;i<permutations.length;i++){
            permutations[i]=(byte)(array[i]/2);
        }
        //System.out.println(Arrays.toString(permutations));
        return permutationHashes.get(new ByteArrayWrapper(permutations));

    }
    public int getSecondPermutationHash(){
        byte[] array=Arrays.copyOfRange(this.edgeConfiguration,6,12);
        byte[] permutations=new byte[array.length];
        for(int i=0;i<permutations.length;i++){
            permutations[i]=(byte)(array[i]/2);
        }
        //System.out.println(Arrays.toString(permutations));
        //System.out.println(permutationHashes.get(new cube.solver.ByteArrayWrapper(permutations)));
        return permutationHashes.get(new ByteArrayWrapper(permutations));


    }
    public static void generatePermutationHashes(ArrayList<Byte> current,ArrayList<Integer> possibleCases){

        if(current.size()!=6) {
            for (Integer i : possibleCases) {
                ArrayList<Byte> currentCopy=(ArrayList)current.clone();
                ArrayList<Integer> possibleCasesCopy=(ArrayList)possibleCases.clone();
                currentCopy.add((i.byteValue()));
                possibleCasesCopy.remove(i);
                generatePermutationHashes(currentCopy,possibleCasesCopy);
            }
        }else{

            byte[] array=new byte[current.size()];
            for(int i=0;i<current.size();i++){
                array[i]=current.get(i);
            }
            //System.out.println(Arrays.toString(array));
            permutationHashes.put(new ByteArrayWrapper(array),permutationHashes.size());
//            System.out.println(permutationHashes.size());
            //System.out.println(Arrays.toString(array));
        }

    }
    public void doGenericEdgeMove(byte d1, byte d2, byte d3, byte d4,
                                  byte s1 , byte s2,byte s3,byte s4,
                                  int o /* 0 or 1*/){
        byte[] oldEdges=edgeConfiguration.clone();
        edgeConfiguration[d1]=(byte)((oldEdges[s1]-oldEdges[s1]%2)+((oldEdges[s1]%2+o)%2));
        edgeConfiguration[d2]=(byte)((oldEdges[s2]-oldEdges[s2]%2)+((oldEdges[s2]%2+o)%2));
        edgeConfiguration[d3]=(byte)((oldEdges[s3]-oldEdges[s3]%2)+((oldEdges[s3]%2+o)%2));
        edgeConfiguration[d4]=(byte)((oldEdges[s4]-oldEdges[s4]%2)+((oldEdges[s4]%2+o)%2));
    }

    public EdgeConfigurationNode clone(){
        EdgeConfigurationNode node=new EdgeConfigurationNode();
        node.edgeConfiguration=this.edgeConfiguration.clone();
        node.previousMove=this.previousMove;
        node.distance=this.distance;
        return node;
    }
    public static void writePermutationHashesToFile() throws IOException {
        FileOutputStream fos=new FileOutputStream("edge_permutation_hashes.txt");
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fos);
        objectOutputStream.writeObject(permutationHashes);
        objectOutputStream.close();
    }
    public static void getPermutationHashesFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fis=new FileInputStream("edge_permutation_hashes.txt");
        ObjectInputStream objectOutputStream=new ObjectInputStream(fis);
        permutationHashes= (HashMap<ByteArrayWrapper, Integer>) objectOutputStream.readObject();
        objectOutputStream.close();
        fis.close();
    }
}
