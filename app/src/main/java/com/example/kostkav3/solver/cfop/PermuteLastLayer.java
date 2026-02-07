package com.example.kostkav3.solver.cfop;

import java.util.ArrayList;

import static com.example.kostkav3.solver.cfop.Colors.*;
import static java.lang.StrictMath.abs;

public final class PermuteLastLayer {
    private static ArrayList<String> solution;
    private static Cube cube;

    private static final String Aa = "R' F R' B2 R F' R' B2 R2";
    private static final String Ab = "R2 B2 R F R' B2 R F' R";
    private static final String E = "F R2 D R' U' R D' R' U2 R' U' R' F' R U R U' R'";
    private static final String F = "R' U' F' R U R' U' R' F R2 U' R' U' R U R' U R";
    private static final String Ga = "R2 U R' U R' U' R U' R2 D U' R' U R D'";
    private static final String Gb = "F' U' F R2 D B' U B U' B D' R2";
    private static final String Gc = "R2 U' R U' R U R' U R2 D' U R U' R' D";
    private static final String Gd = "D' R U R' U' D R2 U' R U' R' U R' U R2 ";
    private static final String H = "R2 U2 R U2 R2 U2 R2 U2 R U2 R2";
    private static final String Ja = "R' U L' U2 R U' R' U2 R L";
    private static final String Jb = "R U R' F' R U R' U' R' F R2 U' R'";
    private static final String Na = "R U R' U R U R' F' R U R' U' R' F R2 U' R' U2 R U' R'";
    private static final String Nb = "R' U R U' R' F' U' F R U R' F R' F' R U' R";
    private static final String Ra = "R U' R' U' R U R D R' U' R D' R' U2 R'";
    private static final String Rb = "R' U2 R U2 R' F R U R' U' R' F' R2";
    private static final String T = "R U R' U' R' F R2 U' R' U' R U R' F'";
    private static final String Ua = "R U' R U R U R U' R' U' R2";
    private static final String Ub = "R2 U R U R' U' R' U' R' U R'";
    private static final String V = "R U2 R' D R U' R U' R U R2 D R' U' R D2";
    private static final String Y = "F R U' R' U' R U R' F' R U R' U' R' F R F'";
    private static final String Z = "R2 L2 D R2 L2 U L R' F2 R2 L2 B2 L R'";

    public static final ArrayList<String> solve(Cube setCube) {
        solution = new ArrayList<>();
        cube = setCube;
        findAlg();
        int rotations = 0;
        while (!cube.solved() && rotations < 4) {
            solution.add(cube.U());
            rotations++;
        }
        if (rotations >= 4) {
            System.out.println("Error: cube remains unsolved.");
        }
        fixSolution(solution);
        return solution;
    }
    private static final void findAlg() {
        if (cube.solved()) {
            return;
        }
        // Will be in following order: Blue, Green, Red, Orange
        Edge[] edges = cube.getEdges(cube.getFace(YELLOW).getEdgeLocs());
        int[] edgeColors = {BLUE, GREEN, RED, ORANGE};
        // Will be in following order: Blue/Red, Blue/Orange, Green/Orange, Green/Red
        Corner[] corners = cube.getCorners(cube.getFace(YELLOW).getCorLocs(),
                cube.getFace(YELLOW).getCorSides());
        // Check for H perm case
        if (edges[0].faceWithColor(BLUE) == corners[2].faceWithColor(GREEN) &&
                edges[0].faceWithColor(BLUE) == corners[3].faceWithColor(GREEN) &&
                edges[1].faceWithColor(GREEN) == corners[0].faceWithColor(BLUE) &&
                edges[1].faceWithColor(GREEN) == corners[1].faceWithColor(BLUE)) {
            saveTurns(H);
            return;
            // Check for N perm case
        } else if (edges[0].faceWithColor(BLUE) == corners[0].faceWithColor(BLUE) &&
                edges[1].faceWithColor(GREEN) == corners[2].faceWithColor(GREEN) &&
                edges[1].faceWithColor(GREEN) == corners[1].faceWithColor(BLUE) &&
                cube.getEdge(9).colorOnFace(RED) == -cube.getEdge(10).colorOnFace(ORANGE)) {
            saveTurns(Na);
            return;
        } else if (edges[0].faceWithColor(BLUE) == corners[2].faceWithColor(GREEN) &&
                edges[1].faceWithColor(GREEN) == corners[0].faceWithColor(BLUE) &&
                edges[1].faceWithColor(GREEN) == corners[0].faceWithColor(BLUE) &&
                cube.getEdge(9).colorOnFace(RED) == -cube.getEdge(10).colorOnFace(ORANGE)) {
            saveTurns(Nb);
            return;
        }
        int turns = 0;
        int solvedEdges = 0;
        int solvedCorners = 0;
        for (int i = 0; i < 4; i++) {
            if (edges[i].solved()) {
                solvedEdges++;
            }
            if (corners[i].solved()) {
                solvedCorners++;
            }
        }
        while (solvedEdges + solvedCorners <= 2 && turns < 4) {
            solution.add(cube.U());
            solvedEdges = 0;
            solvedCorners = 0;
            for (int i = 0; i < 4; i++) {
                if (edges[i].solved()) {
                    solvedEdges++;
                }
                if (corners[i].solved()) {
                    solvedCorners++;
                }
            }
            turns++;
        }
        if (cube.solved()) {
            return;
        }
        if (turns == 4) {
            // Has to be G perm because only 2 pieces solved for every Yellow face orientation
            Corner pairCorner = null;
            Corner otherCorner = null;
            int color = 0;
            boolean toRight = true;
            for (int i = 0; i < 4; i++) {
                if (toRight(edges[i]).colorOnFace(edges[i].faceWithColor(edgeColors[i])) == edgeColors[i]) {
                    pairCorner = toRight(edges[i]);
                    otherCorner = toLeft(edges[i]);
                    color = edgeColors[i];
                    break;
                } else if (toLeft(edges[i]).colorOnFace(edges[i].faceWithColor(edgeColors[i])) == edgeColors[i]) {
                    pairCorner = toLeft(edges[i]);
                    otherCorner = toRight(edges[i]);
                    color = edgeColors[i];
                    toRight = false;
                    break;
                }
            }
            if (toRight) {
                if (abs(otherCorner.colorOnFace(pairCorner.faceWithColor(color))) == abs(color)) {
                    while (cube.getEdge(1).colorOnFace(BLUE)
                            != cube.getCorner(1,1).colorOnFace(BLUE)) {
                        solution.add(cube.U());
                    }
                    saveTurns(Gb);
                    return;
                } else {
                    while (cube.getEdge(1).colorOnFace(BLUE)
                            != cube.getCorner(1,1).colorOnFace(BLUE)) {
                        solution.add(cube.U());
                    }
                    saveTurns(Ga);
                    return;
                }
            } else {
                if (abs(otherCorner.colorOnFace(pairCorner.faceWithColor(color))) == abs(color)) {
                    while (cube.getEdge(9).colorOnFace(RED)
                            != cube.getCorner(1,1).colorOnFace(RED)) {
                        solution.add(cube.U());
                    }
                    saveTurns(Gd);
                    return;
                } else {
                    while (cube.getEdge(5).colorOnFace(GREEN)
                            != cube.getCorner(2,-1).colorOnFace(GREEN)) {
                        solution.add(cube.U());
                    }
                    saveTurns(Gc);
                    return;
                }
            }
        }
        if (solvedCorners == 4 && solvedEdges == 1) {
            Edge edge = null;
            for (Edge e : edges) {
                if (e.solved()) {
                    edge = e;
                }
            }
            while (cube.getEdge(5) != edge) {
                solution.add(cube.U());
            }
            if (abs(cube.getEdge(9).colorOnFace(RED))
                    == abs(cube.getCorner(1, 1).colorOnFace(RED))) {
                saveTurns(Ua);
                return;
            } else {
                saveTurns(Ub);
                return;
            }
        }
        if (solvedCorners == 4 && solvedEdges == 0) {
            if (cube.getCorner(1,1).colorOnFace(BLUE)
                    != cube.getEdge(9).colorOnFace(RED)) {
                solution.add(cube.U());
            }
            saveTurns(Z);
            return;
        }
        if (solvedCorners == 0 && solvedEdges == 4) {
            if (cube.getCorner(1,1).colorOnFace(BLUE)
                    != cube.getEdge(9).colorOnFace(RED)) {
                solution.add(cube.U());
            }
            saveTurns(E);
            return;
        }
        if (solvedCorners == 1 && solvedEdges == 4) {
            Corner corner = null;
            for (Corner c : corners) {
                if (c.solved()) {
                    corner = c;
                }
            }
            while (cube.getCorner(2, 1) != corner) {
                solution.add(cube.U());
            }
            if (abs(cube.getCorner(1, 1).colorOnFace(BLUE))
                    == abs(cube.getEdge(1).colorOnFace(BLUE))) {
                saveTurns(Aa);
                return;
            } else {
                saveTurns(Ab);
                return;
            }
        }
        for (Edge e : edges) {
            if (e.solved() && cube.getEdge(e.getColors()[0], -e.getColors()[1]).solved()
                    && cube.getEdge(-e.getColors()[0], e.getColors()[1]).solved()) {
                if ((toRight(e).solved() && !toLeft(e).solved())
                        || (!toRight(e).solved() && toLeft(e).solved())) {
                    if (toRight(e).solved()){
                        while (e.getLoc() != 5) {
                            solution.add(cube.U());
                        }
                    } else {
                        while (e.getLoc() != 1) {
                            solution.add(cube.U());
                        }
                    }
                    saveTurns(T);
                    return;
                } else {
                    if (toRight(e).solved()){
                        while (e.getLoc() != 10) {
                            solution.add(cube.U());
                        }
                    } else {
                        while (e.getLoc() != 9) {
                            solution.add(cube.U());
                        }
                    }
                    saveTurns(F);
                    return;
                }
            } else if (e.solved() && toRight(e).solved() && toLeft(e).solved()) {
                if (toRight(toRight(e)).solved()) {
                    while (e.getLoc() != 1) {
                        solution.add(cube.U());
                    }
                    saveTurns(Ja);
                    return;
                } else {
                    while (e.getLoc() != 10) {
                        solution.add(cube.U());
                    }
                    saveTurns(Jb);
                    return;
                }
            } else if (e.solved() && toLeft(e).solved() && toLeft(toLeft(e)).solved()
                    && toRight(toRight(toRight(e))).solved()) {
                while (e.getLoc() != 1) {
                    solution.add(cube.U());
                }
                saveTurns(V);
                return;
            } else if (e.solved() && !toLeft(e).solved() && !toRight(e).solved()) {
                if (toLeft(toLeft(e)).solved()) {
                    while (e.getLoc() != 9) {
                        solution.add(cube.U());
                    }
                    saveTurns(Ra);
                    return;
                } else {
                    while (e.getLoc() != 5) {
                        solution.add(cube.U());
                    }
                    saveTurns(Rb);
                    return;
                }
            }
        }
        while (cube.getEdge(1).colorOnFace(BLUE) != -cube.getCorner(1,1).colorOnFace(BLUE)) {
            solution.add(cube.U());
        }
        saveTurns(Y);
    }
    private static Corner toRight(Edge edge) {
        if (edge.getLoc() == 1) {
            return cube.getCorner(1, 1);
        } else if (edge.getLoc() == 10) {
            return cube.getCorner(2, 1);
        } else if (edge.getLoc() == 5) {
            return cube.getCorner(1, -1);
        }
        return cube.getCorner(2, -1);
    }
    private static Corner toLeft(Edge edge) {
        if (edge.getLoc() == 1) {
            return cube.getCorner(2, 1);
        } else if (edge.getLoc() == 10) {
            return cube.getCorner(1, -1);
        } else if (edge.getLoc() == 5) {
            return cube.getCorner(2, -1);
        }
        return cube.getCorner(1, 1);
    }
    private static Edge toRight(Corner corner) {
        if (corner.getLoc()[0] == 1) {
            if (corner.getSide() == 1) {
                return cube.getEdge(9);
            } else {
                return cube.getEdge(10);
            }
        }
        if (corner.getSide() == 1) {
            return cube.getEdge(1);
        }
        return cube.getEdge(5);
    }
    private static Edge toLeft(Corner corner) {
        if (corner.getLoc()[0] == 1) {
            if (corner.getSide() == 1) {
                return cube.getEdge(1);
            } else {
                return cube.getEdge(5);
            }
        }
        if (corner.getSide() == 1) {
            return cube.getEdge(10);
        }
        return cube.getEdge(9);
    }
    private static void saveTurns(String moves) {
        String[] turns = moves.split(" ");
        for (String s : turns) {
            cube.mix(s);
            solution.add(s);
        }
    }
}
