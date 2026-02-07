package com.example.kostkav3.solver.cfop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.StrictMath.abs;

import static com.example.kostkav3.solver.cfop.Colors.*;

public final class Cross {
    private static final int[] sideFaces = {ORANGE, BLUE, RED, GREEN, ORANGE, BLUE};
    private static int faceIndex = -1;
    private static int sideColor;
    private static Map<Edge, ArrayList<String>> solution;
    public static Map<Edge, ArrayList<String>> solve(Cube cube) {
        solution = new HashMap<>();
        for (int i = -3; i <= 3; i += 2) {
            // Order: Orange, Green, Blue, Red
            Edge edge = cube.getEdge(WHITE, i);
            solution.put(edge, new ArrayList<String>());
            placeWhiteEdge(edge, cube);
            fixSolution(solution.get(edge));
        }
        return solution;
    }
    private static void placeWhiteEdge(Edge edge, Cube cube) {
        // if its already solved, don't do anything
        if (edge.solved()) {
            return;
        }
        ArrayList<String> sol = solution.get(edge);
        // saves the non-white color of the edge
        sideColor = edge.getColors()[0];
        if (abs(sideColor) == 2) {
            sideColor = edge.getColors()[1];
        }
        // finds the initial face the edge lies on
        for (int i = 1; i < 5; i++) {
            if (edge.onSide(sideFaces[i])) {
                if (edge.colorOnFace(sideFaces[i]) != WHITE ||
                        edge.onSide(YELLOW) || edge.onSide(WHITE)) {
                    faceIndex = i;
                }
            }
        }
        if (edge.onSide(WHITE)) {
            // if on white side and oriented
            if (edge.getOrient() == 1) {
                sol.add(cube.getFace(sideFaces[faceIndex]).rotate(1));
                sol.add(cube.getFace(sideFaces[faceIndex]).rotate(1));
            // if on white side and misoriented
            } else {
                sol.add(cube.getFace(sideFaces[faceIndex]).rotate(1));
                sol.add(cube.getFace(sideFaces[faceIndex - 1]).rotate(-1));
                sol.add(cube.U());
                sol.add(cube.getFace(sideFaces[faceIndex - 1]).rotate(1));
            }
        }
        if (edge.onSide(YELLOW) && edge.getOrient() == -1) {
            sol.add(cube.getFace(sideFaces[faceIndex]).rotate(1));
            sol.add(cube.getFace(sideFaces[faceIndex + 1]).rotate(1));
            sol.add(cube.Ui());
            sol.add(cube.getFace(sideFaces[faceIndex + 1]).rotate(-1));
            sol.add(cube.getFace(sideFaces[faceIndex]).rotate(-1));
        }
        // if the edge is in the middle layer
        if (!edge.onSide(WHITE) && !edge.onSide(YELLOW)) {
            int count = 0;
            while (!edge.onSide(YELLOW)) {
                sol.add(cube.getFace(sideFaces[faceIndex]).rotate(1));
                count++;
            }
            sol.add(cube.U());
            for (int i = 0; i < count; i++) {
                sol.add(cube.getFace(sideFaces[faceIndex]).rotate(-1));
            }
        }
        while (!edge.onSide(sideColor)) {
            sol.add(cube.U());
        }
        sol.add(cube.getFace(sideColor).rotate(1));
        sol.add(cube.getFace(sideColor).rotate(1));
    }
}
