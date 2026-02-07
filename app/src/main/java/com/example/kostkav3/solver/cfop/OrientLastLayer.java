package com.example.kostkav3.solver.cfop;

import java.util.ArrayList;

import static com.example.kostkav3.solver.cfop.Colors.*;

/**
 * References at
 * http://www.rubiksplace.com/speedcubing/OLL-algorithms/
 */
public final class OrientLastLayer {
    private static final int[] edgeLocs = {5,9,1,10};
    private static final int[][] corLocs = {{1,4,2}, {2, 3, 1}, {1,4,2}, {2, 3, 1}};
    private static final int[] corSides = {1, 1, -1, -1};
    //all edges correctly oriented
    private static final String sune = "L U L' U L U2 L'";
    private static final String parallel = "R U2 R' U' R U R' U' R U' R'";
    private static final String headlights = "R2 D R' U2 R D' R' U2 R'";
    private static final String figure8 = "R' F R B' R' F' R B";
    private static final String reverseSune = "R' U' R U' R' U2 R";
    private static final String perpendicular = "R U2 R2 U' R2 U' R2 U2 R";
    private static final String fly = "R' F' L F R F' L' F";

    private static Cube cube;
    private static ArrayList<String> solution;

    public static final ArrayList<String> solve(Cube setCube) {
        cube = setCube;
        solution = new ArrayList<>();
        Edge[] edges = cube.getEdges(edgeLocs);
        Corner[] corners = cube.getCorners(corLocs, corSides);
        int orientedEdges = 0;
        for (Edge e : edges) {
            if (e.getOrient() == 1) {
                orientedEdges++;
            }
        }
        if (orientedEdges == 0) {
            saveTurns("F U R U' R' F' L F U F' U' L'");
        } else if (orientedEdges == 2) {
            if (cube.getEdge(1).getOrient() != cube.getEdge(5).getOrient()) {
                while (cube.getEdge(5).getOrient() != 1  || cube.getEdge(10).getOrient() != 1) {
                    solution.add(cube.U());
                }
                saveTurns("F U R U' R' F'");
            } else {
                if (cube.getEdge(1).getOrient() == 1) {
                    solution.add(cube.U());
                }
                saveTurns("F R U R' U' F'");
            }
        }
        switch (cornerOrientation(corners)) {
            case (1) :
                while (cube.getCorner(2, -1).getOrient() != 0) {
                    solution.add(cube.U());
                }
                saveTurns(sune);
                break;
            case (2) :
                while (cube.getCorner(1, -1).getOrient() != 0) {
                    solution.add(cube.U());
                }
                saveTurns(reverseSune);
                break;
            case (3) :
                if (corners[0].faceWithColor(YELLOW) == ORANGE
                        || corners[0].faceWithColor(YELLOW) == RED) {
                    solution.add(cube.U());
                }
                saveTurns(parallel);
                break;
            case (4) :
//
                while (corners[0].faceWithColor(YELLOW) == RED
                    || corners[1].faceWithColor(YELLOW) == RED
                    || corners[2].faceWithColor(YELLOW) == RED
                    || corners[3].faceWithColor(YELLOW) == RED) {
                    solution.add(cube.U());
                }
                saveTurns(perpendicular);
                break;
            case (5) :
                while (cube.getCorner(2,1).getOrient() != -1) {
                    solution.add(cube.U());
                }
                saveTurns(figure8);
                break;
            case (6) :
                while (cube.getCorner(1,1).colorOnFace(BLUE) != YELLOW) {
                    solution.add(cube.U());
                }
                saveTurns(headlights);
                break;
            case (7) :
                while (cube.getCorner(1,1).colorOnFace(BLUE) != YELLOW) {
                    solution.add(cube.U());
                }
                saveTurns(fly);
        }
        fixSolution(solution);
        return solution;
    }
    /**
     * Determines corner orientation each represented by a number
     * Numbers represent, in order: full, sune, reverse sune, parallel, perpendicular, figure8, headlights, fly
     * @param corners corners in the top layer
     * @return
     */
    private static final int cornerOrientation(Corner[] corners) {
        int orientedCorners = 0;
        for (Corner c : corners) {
            if (c.getOrient() == 0) {
                orientedCorners++;
            }
        }
        if (orientedCorners == 4) {
            return 0;
        } else if (orientedCorners == 1) {
            if (corners[0].getOrient() == 1 || corners[1].getOrient() == 1) {
                return 1;
            } else if (corners[0].getOrient() == -1 || corners[1].getOrient() == -1) {
                return 2;
            }
        } else if (orientedCorners == 0) {
            // because there are no oriented corners, no corner can never have orient = 0
            int firstOrient = 0;
            for (Corner c : corners) {
                if (c.getLoc()[0] == 1) {
                    if (c.getOrient() == firstOrient) {
                        return 3;
                    }
                    firstOrient = c.getOrient();
                }
            }
            return 4;
        } else if (orientedCorners == 2) {
            int firstLoc = 0;
            Corner c1 = null;
            for (Corner c : corners) {
                if (c.getOrient() == 0) {
                    if (c.getLoc()[0] == firstLoc) {
                        return 5;
                    }
                    firstLoc = c.getLoc()[0];
                } else {
                    if (c1 != null) {
                        if (c1.faceWithColor(YELLOW) == c.faceWithColor(YELLOW)) {
                            return 6;
                        } else if (c1.getLoc()[0] != c.getLoc()[0]) {
                            return 7;
                        }
                    }
                    c1 = c;
                }
            }
        }
        return -1;
    }
    private static void saveTurns(String moves) {
        String[] turns = moves.split(" ");
        for (String s : turns) {
            cube.mix(s);
            solution.add(s);
        }
    }
}
