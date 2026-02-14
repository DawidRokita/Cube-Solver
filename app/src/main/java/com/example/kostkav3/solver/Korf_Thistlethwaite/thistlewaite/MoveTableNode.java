package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import com.example.kostkav3.solver.Korf_Thistlethwaite.ByteArrayWrapper;
import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MoveTableNode {
    final static HashMap<ByteArrayWrapper,Boolean> parityMap=new HashMap<>();
    final static int cornerPermutationCases=40320;

    RubiksCube rubiksCube;
    ArrayList<Byte> moves;



    public static void main(String[] args){
        RubiksCube.initializeAllowedMovesMaps();
        initializeParityMap();
    }
    public MoveTableNode(){
        this.rubiksCube=new RubiksCube();
        this.moves=new ArrayList<>();
        moves.add(RubiksCube.I);

    }
    public MoveTableNode(byte[] scramble){
        RubiksCube cube=new RubiksCube();
        cube.doAlgorithm(scramble);
        this.rubiksCube=cube;//TODO CLONE
        this.moves=new ArrayList<Byte>();
        moves.add(RubiksCube.I);
    }
    public MoveTableNode(byte[] cubeCorners,byte[] cubeEdges){
        this.rubiksCube= new RubiksCube(cubeCorners,cubeEdges);
        this.moves=new ArrayList<>();
        moves.add(RubiksCube.I);
    }
    public MoveTableNode(RubiksCube cube){
        this.rubiksCube=cube;
        this.moves=new ArrayList<>();
        moves.add(RubiksCube.I);
    }

    public MoveTableNode doMove(byte move){
        MoveTableNode node=this.clone();
        node.rubiksCube.doThistlewaiteMove(move);
        node.moves.add(move);
        return node;

    }
    @Override
    public MoveTableNode clone(){
        MoveTableNode node=new MoveTableNode();
        node.rubiksCube=this.rubiksCube.clone();
        node.moves=new ArrayList<>(this.moves);

        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveTableNode that = (MoveTableNode) o;
        return moves.equals(that.moves) &&
                rubiksCube.equals(that.rubiksCube);
    }
    public ByteArrayWrapper getPhaseOneKey(){
        byte[] array=new byte[rubiksCube.edges.length];
        for(int i=0;i<12;i++){
            array[i]=(byte)(rubiksCube.edges[i]%2);

        }
        return new ByteArrayWrapper(array);

    }
    public PhaseTwoKey getPhaseTwoKey(){
        byte[] cornerOrientations=new byte[rubiksCube.corners.length];
        boolean[] middleEdgePositions=new boolean[rubiksCube.edges.length];
        for(int i=0;i<8;i++){
            cornerOrientations[i]=(byte)(rubiksCube.corners[i]%3);
        }
        for(int j=0;j<12;j++){
            middleEdgePositions[j]= rubiksCube.edges[j] == 0 || rubiksCube.edges[j] == 4 || rubiksCube.edges[j] == 16 || rubiksCube.edges[j] == 20;
        }
        return new PhaseTwoKey(cornerOrientations,middleEdgePositions);
    }
    public PhaseThreeKey getPhaseThreeKey(){
        byte[] cornerEncoding=new byte[rubiksCube.corners.length];
        byte[] edgeEncoding=new byte[12];
        boolean isEvenParity;
//        for(int i=0;i<8;i++){
//            if(rubiksCube.corners[i]==0||rubiksCube.corners[i]==6) {
//                cornerEncoding[i] = 0;
//            }else if(rubiksCube.corners[i]==15||rubiksCube.corners[i]==21) {
//                cornerEncoding[i] = 1;
//            }else if(rubiksCube.corners[i]==3||rubiksCube.corners[i]==9){
//                cornerEncoding[i]=2;
//            }else if(rubiksCube.corners[i]==12||rubiksCube.corners[i]==18){
//                cornerEncoding[i]=3;
//            }
//        }
        cornerEncoding=rubiksCube.corners.clone();
        for(int j=0;j<12;j++){
            if(rubiksCube.edges[j]==0||rubiksCube.edges[j]==4||rubiksCube.edges[j]==16|rubiksCube.edges[j]==20){
                edgeEncoding[j]=0;
            }else if(rubiksCube.edges[j]==2||rubiksCube.edges[j]==6||rubiksCube.edges[j]==18|rubiksCube.edges[j]==22){
                edgeEncoding[j]=1;
            }else if(rubiksCube.edges[j]==8||rubiksCube.edges[j]==10||rubiksCube.edges[j]==12|rubiksCube.edges[j]==14){
                edgeEncoding[j]=2;
            }
        }
        isEvenParity=parityMap.get(getCornerPermutationKey());
        return new PhaseThreeKey(cornerEncoding,edgeEncoding,isEvenParity);

    }
    public PhaseFourKey getPhaseFourKey(){
        return new PhaseFourKey(rubiksCube.corners,rubiksCube.edges);
    }
    public ModifiedPhaseThreeKey getModifiedPhaseThreeKey(){
        byte[] cornerEncoding=new byte[8];
        byte[] edgeEncoding=new byte[12];
        for(int i=0;i<8;i++){
            if(rubiksCube.corners[i]==0||rubiksCube.corners[i]==9||rubiksCube.corners[i]==18||rubiksCube.corners[i]==21){
                cornerEncoding[i]=0;
            }else if(rubiksCube.corners[i]==0||rubiksCube.corners[i]==9||rubiksCube.corners[i]==18||rubiksCube.corners[i]==21){}

        }
        return new ModifiedPhaseThreeKey(cornerEncoding,edgeEncoding);
    }

    public ByteArrayWrapper getCornerPermutationKey(){
        byte[] cornerPermutationKey=new byte[8];
        for(int i=0;i<8;i++){
            cornerPermutationKey[i]=(byte)(rubiksCube.corners[i]-rubiksCube.corners[i]%3);
        }
        return new ByteArrayWrapper(cornerPermutationKey);
    }
    public static void initializeParityMap(){
        Queue<MoveTableNode> searchQueue =new LinkedList<MoveTableNode>();
        MoveTableNode startingNode=new MoveTableNode();
        searchQueue.add(startingNode);
        parityMap.put(startingNode.getCornerPermutationKey(),startingNode.getParity());
        int numberCases=1;
        //System.out.println(searchQueue.peek().toString());
        while((!searchQueue.isEmpty())){
            MoveTableNode currentNode=searchQueue.remove();
            System.out.println(numberCases);
            for(byte move:RubiksCube.allowedMovesMap.get(currentNode.moves.get(currentNode.moves.size()-1))){
                MoveTableNode newNode=currentNode.doMove(move);
                if(parityMap.get(newNode.getCornerPermutationKey())==null){

                    parityMap.put(newNode.getCornerPermutationKey(),newNode.getParity());
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }
    public  boolean getParity(){
        int moveCount=0;
        for(byte move:moves){
            if((move== RubiksCube.R2)||(move== RubiksCube.L2)||(move== RubiksCube.U2)||(move== RubiksCube.D2)||(move== RubiksCube.F2)||(move== RubiksCube.B2)){
                moveCount+=2;
            }else if(move!= RubiksCube.I) {
                moveCount++;
            }
        }
        return moveCount%2==0;
    }



}
