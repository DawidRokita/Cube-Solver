package com.example.kostkav3.solver.cfop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.kostkav3.solver.cfop.Colors.*;

public final class FirstTwoLayers {
    private static Map<Corner, ArrayList<String>> solution;
    public static Map<Corner, ArrayList<String>> solve(Cube cube) {
        solution = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            // Order: Blue/Red, Blue/Orange, Green/Orange, Green/Red
            int[] colors = {1,-2,3};
            if (i > 1) {
                colors[0] *= -1;
            }
            if (i == 1 || i == 2) {
                colors[2] *= -1;
            }
            Corner corner = cube.getCorner(colors[0], colors[1], colors[2]);
            Edge edge = cube.getEdge(colors[0], colors[2]);
            solution.put(corner, new ArrayList<String>());
            solvePair(corner, edge, cube);
            fixSolution(solution.get(corner));
        }
        return solution;
    }
    private static void solvePair(Corner corner, Edge edge, Cube cube) {
        if (corner.solved() && edge.solved()) {
            return;
        }
        ArrayList<String> sol = solution.get(corner);
        int orient = connected(corner, edge);
        if (orient == -1) {
            if (!(edge.onSide(YELLOW))) {
                if (!(corner.onSide(YELLOW))) {
                    // if neither piece is in the top layer
                    int[] loc = corner.getLoc();
                    int side = corner.getSide();
                    int toTurn;
                    switch (side) {
                        case 1 :
                            if (loc[0] == 3) {
                                toTurn = ORANGE;
                            } else {
                                toTurn = BLUE;
                            }
                            break;
                        default :
                            if (loc[0] == 3) {
                                toTurn = RED;
                            } else {
                                toTurn = GREEN;
                            }
                    }
                    sol.add(cube.getFace(toTurn).rotate(-1));
                    sol.add(cube.U());
                    sol.add(cube.getFace(toTurn).rotate(1));
                }
                // only the corner should now be in the top layer
                while (connected(corner, edge) == -1) {
                    sol.add(cube.U());
                }
            } else {
                if (!(corner.onSide(YELLOW))) {
                    // if only the edge is in the top layer
                    int[] loc = corner.getLoc();
                    int side = corner.getSide();
                    int toTurn;
                    int mount;
                    switch (side) {
                        case 1 :
                            if (loc[0] == 3) {
                                toTurn = ORANGE;
                                mount = BLUE;
                            } else {
                                toTurn = BLUE;
                                mount = RED;
                            }
                            break;
                        default :
                            if (loc[0] == 3) {
                                toTurn = RED;
                                mount = GREEN;
                            } else {
                                toTurn = GREEN;
                                mount = ORANGE;
                            }
                    }
                    while (!edge.onSide(mount)) {
                        sol.add(cube.U());
                    }
                    sol.add(cube.getFace(toTurn).rotate(-1));
                    sol.add(cube.Ui());
                    sol.add(cube.getFace(toTurn).rotate(1));
                } else {
                    // if both pieces are in the top layer
                    int[] cols = edge.getColors();
                    while (!edge.onSide(cols[1])) {
                        sol.add(cube.U());
                    }
                    int flip = -1;
                    if (cols[0] * cols[1] > 0) {
                        flip = 1;
                    }
                    sol.add(cube.getFace(cols[1]).rotate(flip));
                    while (connected(corner, edge) == -1) {
                        sol.add(cube.U());
                    }
                    sol.add(cube.getFace(cols[1]).rotate(-flip));
                }
            }
        }
        orient = connected(corner, edge);
        if (orient >= 0) {
            // If the pair is connected
            if (!edge.onSide(YELLOW)) {
                // If the pair is not in the top layer, put it there
                int toTurn = GREEN;
                switch (edge.getLoc()) {
                    case 3:
                        toTurn = RED;
                        break;
                    case 4:
                        toTurn = BLUE;
                        break;
                    case 8:
                        toTurn = ORANGE;
                }
                //brings pair into top layer
                sol.add(cube.getFace(toTurn).rotate(1));
                sol.add(cube.U2());
                sol.add(cube.getFace(toTurn).rotate(-1));
            }
            if (orient == 0) {
                if (corner.getOrient() == 0) {
                    while (!(corner.onSide(corner.getColors()[0]) && corner.onSide(corner.getColors()[2]))) {
                        sol.add(cube.U());
                    }
                    int toTurn = corner.colorOnFace(edge.colorOnFace(YELLOW));
                    int flip = 1;
                    if (Arrays.equals(cube.getFace(toTurn).getCorLocs()[0], corner.getLoc())) {
                        flip = -1;
                    }
                    sol.add(cube.getFace(toTurn).rotate(flip));
                    sol.add(cube.U2());
                    sol.add(cube.getFace(toTurn).rotate(-flip));
                    splitInsert(cube, corner);
                    return;
                } else {
                    int toTurn = corner.colorOnFace(YELLOW);
                    while (!(corner.onSide(toTurn) && corner.colorOnFace(toTurn) != WHITE)) {
                        sol.add(cube.U());
                    }
                    int flip = 1;
                    if (Arrays.equals(cube.getFace(toTurn).getCorLocs()[0], corner.getLoc())) {
                        flip = -1;
                    }
                    sol.add(cube.getFace(toTurn).rotate(-flip));
                    sol.add(cube.getFace(YELLOW).rotate(flip));
                    sol.add(cube.getFace(toTurn).rotate(flip));
                    splitInsert(cube, corner);
                    return;
                }
            }
            if (orient == 1) {
                if (corner.getOrient() == 0) {
                    int toTurn = edge.colorOnFace(YELLOW);
                    while (!(corner.colorOnFace(toTurn) == toTurn)) {
                        sol.add(cube.U());
                    }
                    int flip = -1;
                    if (Arrays.equals(cube.getFace(toTurn).getCorLocs()[0], corner.getLoc())) {
                        flip = 1;
                    }
                    sol.add(cube.getFace(toTurn).rotate(flip));
                    sol.add(cube.getFace(YELLOW).rotate(flip));
                    sol.add(cube.getFace(toTurn).rotate(-flip));
                    sol.add(cube.getFace(YELLOW).rotate(flip));
                    sol.add(cube.getFace(toTurn).rotate(flip));
                    sol.add(cube.getFace(YELLOW).rotate(-flip));
                    sol.add(cube.getFace(toTurn).rotate(-flip));
                    sexy(cube, corner, edge);
                    return;
                } else {
                    int toTurn = edge.colorOnFace(YELLOW);
                    while (!(corner.colorOnFace(toTurn) == WHITE)) {
                        sol.add(cube.U());
                    }
                    int flip = -1;
                    if (Arrays.equals(cube.getFace(toTurn).getCorLocs()[0], corner.getLoc())) {
                        flip = 1;
                    }
                    sol.add(cube.getFace(toTurn).rotate(-flip));
                    sol.add(cube.getFace(YELLOW).rotate(flip));
                    sol.add(cube.getFace(toTurn).rotate(flip));
                    splitInsert(cube, corner);
                    return;
                }
            }
            if (orient == 2) {
                sexy(cube, corner, edge);
                return;
            }
            if (orient == 3) {
                int toTurn = edge.colorOnFace(YELLOW);
                while (!(corner.colorOnFace(toTurn) == WHITE)) {
                    sol.add(cube.U());
                }
                int flip = 1;
                if (Arrays.equals(cube.getFace(toTurn).getCorLocs()[0], corner.getLoc())) {
                    flip = -1;
                }
                sol.add(cube.getFace(toTurn).rotate(-flip));
                sol.add(cube.U2());
                sol.add(cube.getFace(toTurn).rotate(flip));
                splitInsert(cube, corner);
                return;
            }
        }
    }

    /**
     * Determines whether the pair is connected, and, if so, their relative orientation
     * @param corner pair corner
     * @param edge pair edges
     * @return -1 if not connected; getPairOrientation if connected
     */
    private static int connected(Corner corner, Edge edge) {
        if (edge.getLoc() == 3) {
            if (corner.getSide() == 1) {
                if (corner.getLoc()[0] == 1 || corner.getLoc()[0] == 4) {
                    return getPairOrientation(corner, edge);
                }
            }
            return -1;
        } else if (edge.getLoc() == 4) {
            if (corner.getSide() == 1) {
                if (corner.getLoc()[0] == 2 || corner.getLoc()[0] == 3) {
                    return getPairOrientation(corner, edge);
                }
            }
            return -1;
        } else if (edge.getLoc() == 7) {
            if (corner.getSide() == -1) {
                if (corner.getLoc()[0] == 2 || corner.getLoc()[0] == 3) {
                    return getPairOrientation(corner, edge);
                }
            }
            return -1;
        } else if (edge.getLoc() == 8) {
            if (corner.getSide() == -1) {
                if (corner.getLoc()[0] == 1 || corner.getLoc()[0] == 4) {
                    return getPairOrientation(corner, edge);
                }
            }
            return -1;
        } else if (edge.getLoc() == 1) {
            if (corner.getSide() == 1) {
                if (corner.getLoc()[0] == 1 || corner.getLoc()[0] == 2) {
                    return getPairOrientation(corner, edge);
                }
            }
            return -1;
        } else if (edge.getLoc() == 5) {
            if (corner.getSide() == -1) {
                if (corner.getLoc()[0] == 1 || corner.getLoc()[0] == 2) {
                    return getPairOrientation(corner, edge);
                }
            }
            return -1;
        } else if (edge.getLoc() == 9) {
            if ((corner.getSide() == 1 && corner.getLoc()[0] == 1)
                    || (corner.getSide() == -1 && corner.getLoc()[0] == 2)) {
                return getPairOrientation(corner, edge);
            }
            return -1;
        } else {
            if ((corner.getSide() == 1 && corner.getLoc()[0] == 2)
                    || (corner.getSide() == -1 && corner.getLoc()[0] == 1)) {
                return getPairOrientation(corner, edge);
            }
            return -1;
        }
    }

    /**
     * Determines the orientation of 2 connected pieces with respect to each other
     * @param corner corner piece
     * @param edge edge piece
     * @return the orientation; determined by the number of connected matching colors on the pieces
     * (Only considered this later; there are two cases where no colors match,
     * returns 0 for one case, 3 for the other)
     */
    private static int getPairOrientation(Corner corner, Edge edge) {
        int[] colors = {ORANGE, WHITE, GREEN, BLUE, YELLOW, RED};
        int orientation = 0;
        int whitesFace = 0;
        for (int i : colors) {
            if (corner.onSide(i) && corner.colorOnFace(i) == edge.colorOnFace(i)) {
                orientation++;
            }
            if (corner.colorOnFace(i) == WHITE) {
                whitesFace = i;
            }
        }
        if (orientation == 0 && !edge.onSide(whitesFace)) {
            orientation = 3;
        }
        return orientation;
    }

    /**
     * Inserts correctly connected pair into correct slot.
     * Note: must already be correctly connected and in top layer
     * @param corner Corner to be solved
     * @param edge Edge to be solved
     */
    private static void sexy(Cube cube, Corner corner, Edge edge) {
        while (!edge.onSide(edge.colorOnFace(YELLOW))) {
            solution.get(corner).add(cube.U());
        }
        int toTurn = edge.colorOnFace(edge.colorOnFace(YELLOW));
        int flip = -1;
        if (Arrays.equals(corner.getLoc(), cube.getFace(edge.colorOnFace(YELLOW)).getCorLocs()[0])) {
            flip = 1;
        }

        solution.get(corner).add(cube.getFace(toTurn).rotate(-flip));
        solution.get(corner).add(cube.getFace(YELLOW).rotate(flip));
        solution.get(corner).add(cube.getFace(toTurn).rotate(flip));
    }
    /**
     * Split-inserts pair into correct slot.
     * Note: must already be in split insert position and in top layer
     * @param corner Corner to be solved
     */
    private static void splitInsert(Cube cube, Corner corner) {
        int toTurn = corner.colorOnFace(YELLOW);
        while (corner.colorOnFace(toTurn) != WHITE) {
            solution.get(corner).add(cube.U());
        }
        int flip = 1;
        if (Arrays.equals(corner.getLoc(), cube.getFace(toTurn).getCorLocs()[0])) {
            flip = -1;
        }
        solution.get(corner).add(cube.getFace(toTurn).rotate(flip));
        solution.get(corner).add(cube.getFace(YELLOW).rotate(flip));
        solution.get(corner).add(cube.getFace(toTurn).rotate(-flip));
    }
}
