package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;
import com.example.kostkav3.solver.Korf_Thistlethwaite.ByteArrayWrapper;
import com.example.kostkav3.solver.Korf_Thistlethwaite.CubeEncoder;
import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class MoveTables {

    final static int phaseOneCases=2048;
    final static int phaseTwoCases=1082565;
    final static int phaseThreeCases=352800;
    final static HashMap<Byte,Byte> moveInverses=new HashMap<>();
    HashMap<ByteArrayWrapper,ArrayList<Byte>> phaseOneTable=new HashMap<>();
    HashMap<PhaseTwoKey,ArrayList<Byte>> phaseTwoTable =new HashMap<>();
    HashMap<PhaseThreeKey,ArrayList<Byte>> phaseThreeTable=new HashMap<>();
    HashMap<PhaseFourKey,ArrayList<Byte>> phaseFourTable=new HashMap<>();

    HashMap<ModifiedPhaseThreeKey,ArrayList<Byte>> modifiedPhaseThreeTable=new HashMap<>();

    public static void main(String[] args){
        CubeEncoder encoder=new CubeEncoder();
        encoder.initializeMaps();
        RubiksCube cube=encoder.getCube();
        RubiksCube.initializeAllowedMovesMaps();
        initializeMoveInversesMap();
        MoveTableNode.initializeParityMap();
        MoveTables tables = new MoveTables();
        tables.generatePhaseOneTable();
        tables.generatePhaseTwoTable();
        tables.generatePhaseThreeTable();
        tables.generatePhaseFourTable();


        System.out.println(cube);
        System.out.println(tables.solve(cube));
        System.out.println("Maximum moves="+tables.calculateMaximumMoves());


    }
    public String solve(RubiksCube cube){
        MoveTableNode node=new MoveTableNode(cube);
        ArrayList<Byte> phaseOneMoves=phaseOneTable.get(node.getPhaseOneKey());
        Collections.reverse(phaseOneMoves);
        invertMoves(phaseOneMoves);
        System.out.println("phaseOneMoves= "+phaseOneMoves);
        for(byte move:phaseOneMoves){
            node.rubiksCube.doThistlewaiteMove(move);
        }

        ArrayList<Byte> phaseTwoMoves=phaseTwoTable.get(node.getPhaseTwoKey());
        Collections.reverse(phaseTwoMoves);
        invertMoves(phaseTwoMoves);
        System.out.println("phaseTwoMoves= "+phaseTwoMoves);

        for(byte move:phaseTwoMoves){
            node.rubiksCube.doThistlewaiteMove(move);
        }
        ArrayList<Byte> phaseThreeMoves=phaseThreeTable.get(node.getPhaseThreeKey());
        Collections.reverse(phaseThreeMoves);
        invertMoves(phaseThreeMoves);
        System.out.println("phaseThreeMoves= "+phaseThreeMoves);

        for(byte move:phaseThreeMoves){
            node.rubiksCube.doThistlewaiteMove(move);
        }

        System.out.println(node.getPhaseThreeKey().isEvenParity);
        ArrayList<Byte> phaseFourMoves=phaseFourTable.get(node.getPhaseFourKey());
        System.out.println(phaseFourMoves);
        Collections.reverse(phaseFourMoves);
        invertMoves(phaseFourMoves);
        for(byte move:phaseFourMoves){
            node.rubiksCube.doThistlewaiteMove(move);
        }
        String solution="";
        for(byte move:phaseOneMoves){
            solution+=RubiksCube.getMoveString(move)+" ";
        }
        for(byte move:phaseTwoMoves){
            solution+=RubiksCube.getMoveString(move)+" ";
        }for(byte move:phaseThreeMoves){
            solution+=RubiksCube.getMoveString(move)+" ";
        }for(byte move:phaseFourMoves){
            solution+=RubiksCube.getMoveString(move)+" ";
        }

        return solution;





    }

    public void generatePhaseOneTable(){
        Queue<MoveTableNode> searchQueue =new LinkedList<MoveTableNode>();
        MoveTableNode startingNode=new MoveTableNode();
        searchQueue.add(startingNode);
        phaseOneTable.put(startingNode.getPhaseOneKey(),new ArrayList<>(startingNode.moves));
        int numberCases=1;
        //System.out.println(searchQueue.peek().toString());
        while((!searchQueue.isEmpty())&&(numberCases<=phaseOneCases)){
            MoveTableNode currentNode=searchQueue.remove();
            System.out.println(numberCases);
            for(byte move:RubiksCube.allowedMovesMap.get(currentNode.moves.get(currentNode.moves.size()-1))){
                MoveTableNode newNode=currentNode.doMove(move);
                if(phaseOneTable.get(newNode.getPhaseOneKey())==null){

                    phaseOneTable.put(newNode.getPhaseOneKey(),new ArrayList<>(newNode.moves));
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }
    public void generatePhaseTwoTable(){
        Queue<MoveTableNode> searchQueue =new LinkedList<MoveTableNode>();
        MoveTableNode startingNode=new MoveTableNode();
        searchQueue.add(startingNode);
        phaseTwoTable.put(startingNode.getPhaseTwoKey(),new ArrayList<>(startingNode.moves));
        int numberCases=1;
        //System.out.println(searchQueue.peek().toString());
        while((!searchQueue.isEmpty())&&(numberCases<=phaseTwoCases)){
            MoveTableNode currentNode=searchQueue.remove();
            System.out.println(numberCases);
            for(byte move:RubiksCube.phaseTwoAllowedMovesMap.get(currentNode.moves.get(currentNode.moves.size()-1))){
                MoveTableNode newNode=currentNode.doMove(move);
                if(phaseTwoTable.get(newNode.getPhaseTwoKey())==null){

                    phaseTwoTable.put(newNode.getPhaseTwoKey(),new ArrayList<>(newNode.moves));
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }
    public void generatePhaseThreeTable(){
        Queue<MoveTableNode> searchQueue =new LinkedList<MoveTableNode>();
        MoveTableNode startingNode=new MoveTableNode();
        searchQueue.add(startingNode);
        phaseThreeTable.put(startingNode.getPhaseThreeKey(),new ArrayList<>(startingNode.moves));
        int numberCases=1;
        //System.out.println(searchQueue.peek().toString());
        while((!searchQueue.isEmpty())){
            MoveTableNode currentNode=searchQueue.remove();
            System.out.println(numberCases);
            for(byte move:RubiksCube.phaseThreeAllowedMovesMap.get(currentNode.moves.get(currentNode.moves.size()-1))){
                MoveTableNode newNode=currentNode.doMove(move);
                if(phaseThreeTable.get(newNode.getPhaseThreeKey())==null){

                    phaseThreeTable.put(newNode.getPhaseThreeKey(),new ArrayList<>(newNode.moves));
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }
    public void generatePhaseFourTable(){
        Queue<MoveTableNode> searchQueue =new LinkedList<MoveTableNode>();
        MoveTableNode startingNode=new MoveTableNode();
        searchQueue.add(startingNode);
        phaseFourTable.put(startingNode.getPhaseFourKey(),new ArrayList<>(startingNode.moves));
        int numberCases=1;
        //System.out.println(searchQueue.peek().toString());
        while((!searchQueue.isEmpty())){
            MoveTableNode currentNode=searchQueue.remove();
            System.out.println(numberCases);
            for(byte move:RubiksCube.phaseFourAllowedMovesMap.get(currentNode.moves.get(currentNode.moves.size()-1))){
                MoveTableNode newNode=currentNode.doMove(move);
                if(phaseFourTable.get(newNode.getPhaseFourKey())==null){

                    phaseFourTable.put(newNode.getPhaseFourKey(),new ArrayList<>(newNode.moves));
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }
    public void generateModifiedPhaseThreeTable(){

    }
    public void writeTablesToFile() throws IOException {
        FileOutputStream fisOne=new FileOutputStream("phase_one_table.txt");
        ObjectOutputStream objectOutputStreamOne=new ObjectOutputStream(fisOne);
        objectOutputStreamOne.writeObject(phaseOneTable);
        fisOne.close();

        FileOutputStream fisTwo=new FileOutputStream("phase_two_table.txt");
        ObjectOutputStream objectOutputStreamTwo=new ObjectOutputStream(fisTwo);
        objectOutputStreamTwo.writeObject(phaseTwoTable);
        fisTwo.close();

        FileOutputStream fisThree=new FileOutputStream("phase_three_table.txt");
        ObjectOutputStream objectOutputStreamThree=new ObjectOutputStream(fisThree);
        objectOutputStreamThree.writeObject(phaseThreeTable);
        fisThree.close();

        FileOutputStream fisFour=new FileOutputStream("phase_four_table.txt");
        ObjectOutputStream objectOutputStreamFour=new ObjectOutputStream(fisFour);
        objectOutputStreamFour.writeObject(phaseFourTable);
        fisFour.close();
    }
    public static void invertMoves(ArrayList<Byte> moves){

        for(int i=0;i<moves.size();i++){
                byte element=moves.get(i);
                moves.set(i,moveInverses.get(element));
        }

    }
    public int calculateMaximumMoves(){
        int phaseOneMax=Integer.MIN_VALUE;
        for(ArrayList<Byte> moves:phaseOneTable.values()){
            if(moves.size()>phaseOneMax){
                phaseOneMax=moves.size();
            }
        }
        int phaseTwoMax=Integer.MIN_VALUE;
        for(ArrayList<Byte> moves:phaseTwoTable.values()){
            if(moves.size()>phaseTwoMax){
                phaseTwoMax=moves.size();
            }
        }
        int phaseThreeMax=Integer.MIN_VALUE;
        for(ArrayList<Byte> moves:phaseThreeTable.values()){
            if(moves.size()>phaseThreeMax){
                phaseThreeMax=moves.size();
            }
        }
        int phaseFourMax=Integer.MIN_VALUE;
        for(Map.Entry<PhaseFourKey,ArrayList<Byte>> entry:phaseFourTable.entrySet()){
            MoveTableNode node=new MoveTableNode(entry.getKey().corners,entry.getKey().edges);
            if(entry.getValue().size()>phaseFourMax&&Arrays.equals(node.getPhaseThreeKey().corners,new MoveTableNode().getPhaseThreeKey().corners)){
                phaseFourMax=entry.getValue().size();
            }
        }
        return phaseOneMax+phaseTwoMax+phaseThreeMax+phaseFourMax;
        
    }
    public static void initializeMoveInversesMap(){
        moveInverses.put(RubiksCube.R,RubiksCube.R_PRIME);
        moveInverses.put(RubiksCube.R_PRIME,RubiksCube.R);
        moveInverses.put(RubiksCube.R2,RubiksCube.R2);

        moveInverses.put(RubiksCube.L,RubiksCube.L_PRIME);
        moveInverses.put(RubiksCube.L_PRIME,RubiksCube.L);
        moveInverses.put(RubiksCube.L2,RubiksCube.L2);

        moveInverses.put(RubiksCube.U,RubiksCube.U_PRIME);
        moveInverses.put(RubiksCube.U_PRIME,RubiksCube.U);
        moveInverses.put(RubiksCube.U2,RubiksCube.U2);

        moveInverses.put(RubiksCube.D,RubiksCube.D_PRIME);
        moveInverses.put(RubiksCube.D_PRIME,RubiksCube.D);
        moveInverses.put(RubiksCube.D2,RubiksCube.D2);

        moveInverses.put(RubiksCube.F,RubiksCube.F_PRIME);
        moveInverses.put(RubiksCube.F_PRIME,RubiksCube.F);
        moveInverses.put(RubiksCube.F2,RubiksCube.F2);

        moveInverses.put(RubiksCube.B,RubiksCube.B_PRIME);
        moveInverses.put(RubiksCube.B_PRIME,RubiksCube.B);
        moveInverses.put(RubiksCube.B2,RubiksCube.B2);

        moveInverses.put(RubiksCube.I, RubiksCube.I);

    }

}
