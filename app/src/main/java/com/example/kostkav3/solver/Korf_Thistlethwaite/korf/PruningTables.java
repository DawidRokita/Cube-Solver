package com.example.kostkav3.solver.Korf_Thistlethwaite.korf;

import android.content.Context;

import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Queue;

public class PruningTables {
    final static int cornerCases = 88179840;
    final static int halfEdgeCases = 42577920;

    final static int halfEdgeOrientationCases = 64;
    final static int halfEdgePermutationCases = 665280;

    final static int cornerOrientationCases = 2187;
    final static int cornerPermutationCases = 40320;

    public final static byte cornerState = 0;
    public final static byte firstEdgeState = 1;
    public final static byte secondEdgeState = 2;

    public byte[][] cornerTable = new byte[cornerOrientationCases][cornerPermutationCases];
    public byte[][] firstEdgeTable = new byte[halfEdgeOrientationCases][halfEdgePermutationCases];
    public byte[][] secondEdgeTable = new byte[halfEdgeOrientationCases][halfEdgePermutationCases];

    private static final PruningTables pruningTables = new PruningTables();

    // ważne: żeby nie ładować wielokrotnie
    private static volatile boolean loaded = false;

    public PruningTables() {}

    public static PruningTables getSingletonInstance() {
        return pruningTables;
    }

    public synchronized void getAllTablesFromFile(Context context) throws IOException, ClassNotFoundException {
        if (loaded) return;
        if (context == null) throw new IllegalArgumentException("context == null");

        Context appContext = context.getApplicationContext();

        getCornerTableFromAssets(appContext);
        getFirstEdgeTableFromAssets(appContext);
        getSecondEdgeTableFromAssets(appContext);

        loaded = true;
    }

    // --- assets loading ---

    private void getCornerTableFromAssets(Context context) throws IOException, ClassNotFoundException {
        try (InputStream is = context.getAssets().open("javacubesolver/corner_pruning_table.txt");
             ObjectInputStream ois = new ObjectInputStream(is)) {
            this.cornerTable = (byte[][]) ois.readObject();
        }
    }

    private void getFirstEdgeTableFromAssets(Context context) throws IOException, ClassNotFoundException {
        try (InputStream is = context.getAssets().open("javacubesolver/first_edge_pruning_table.txt");
             ObjectInputStream ois = new ObjectInputStream(is)) {
            this.firstEdgeTable = (byte[][]) ois.readObject();
        }
    }

    private void getSecondEdgeTableFromAssets(Context context) throws IOException, ClassNotFoundException {
        try (InputStream is = context.getAssets().open("javacubesolver/second_edge_pruning_table.txt");
             ObjectInputStream ois = new ObjectInputStream(is)) {
            this.secondEdgeTable = (byte[][]) ois.readObject();
        }
    }

    // --- heuristics ---

    public byte getCornerHeuristic(RubiksCube cube) {
        return cornerTable[cube.getCornerOrientationHash()][cube.getCornerPermutationHash()];
    }

    public byte getFirstEdgeHeuristic(RubiksCube cube) {
        return firstEdgeTable[cube.getFirstEdgeOrientationHash()][cube.getFirstEdgePermutationHash()];
    }

    public byte getSecondEdgeHeuristic(RubiksCube cube) {
        return secondEdgeTable[cube.getSecondEdgeOrientationHash()][cube.getSecondEdgePermutationHash()];
    }

    // ------------------------------------------------------------
    // Poniżej zostawiam Twoje fillXTable() na wypadek gdybyś kiedyś
    // regenerował tablice, ale na Androidzie normalnie tego NIE używasz.
    // ------------------------------------------------------------

    public void fillCornerTable() {
        Queue<CornerConfigurationNode> searchQueue = new LinkedList<>();
        CornerConfigurationNode startingNode = new CornerConfigurationNode();
        searchQueue.add(startingNode);

        cornerTable[startingNode.getOrientationHash()][startingNode.getPermutationHash()] = startingNode.distance;
        int numberCases = 1;

        while ((!searchQueue.isEmpty()) && (numberCases <= cornerCases)) {
            CornerConfigurationNode currentNode = searchQueue.remove();

            for (byte move : RubiksCube.allowedMovesMap.get(currentNode.previousMove)) {
                CornerConfigurationNode newNode = currentNode.doMove(move);
                int orientationHash = newNode.getOrientationHash();
                int permutationHash = newNode.getPermutationHash();
                if (cornerTable[orientationHash][permutationHash] == -1) {
                    cornerTable[orientationHash][permutationHash] = newNode.distance;
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }

    public void fillFirstEdgeTable() {
        Queue<EdgeConfigurationNode> searchQueue = new LinkedList<>();
        EdgeConfigurationNode startingNode = new EdgeConfigurationNode();
        searchQueue.add(startingNode);

        firstEdgeTable[startingNode.getFirstOrientationHash()][startingNode.getFirstPermutationHash()] = startingNode.distance;
        int numberCases = 1;

        while ((!searchQueue.isEmpty()) && (numberCases <= halfEdgeCases)) {
            EdgeConfigurationNode currentNode = searchQueue.remove();
            for (byte move : RubiksCube.allowedMovesMap.get(currentNode.previousMove)) {
                EdgeConfigurationNode newNode = currentNode.doMove(move);
                int orientationHash = newNode.getFirstOrientationHash();
                int permutationHash = newNode.getFirstPermutationHash();
                if (firstEdgeTable[orientationHash][permutationHash] == -1) {
                    firstEdgeTable[orientationHash][permutationHash] = newNode.distance;
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }

    public void fillSecondEdgeTable() {
        Queue<EdgeConfigurationNode> searchQueue = new LinkedList<>();
        EdgeConfigurationNode startingNode = new EdgeConfigurationNode();
        searchQueue.add(startingNode);

        secondEdgeTable[startingNode.getSecondOrientationHash()][startingNode.getSecondPermutationHash()] = startingNode.distance;
        int numberCases = 1;

        while ((!searchQueue.isEmpty()) && (numberCases <= halfEdgeCases)) {
            EdgeConfigurationNode currentNode = searchQueue.remove();
            for (byte move : RubiksCube.allowedMovesMap.get(currentNode.previousMove)) {
                EdgeConfigurationNode newNode = currentNode.doMove(move);
                int orientationHash = newNode.getSecondOrientationHash();
                int permutationHash = newNode.getSecondPermutationHash();
                if (secondEdgeTable[orientationHash][permutationHash] == -1) {
                    secondEdgeTable[orientationHash][permutationHash] = newNode.distance;
                    searchQueue.add(newNode);
                    numberCases++;
                }
            }
        }
    }
}
