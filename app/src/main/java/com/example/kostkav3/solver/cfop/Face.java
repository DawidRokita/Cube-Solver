package com.example.kostkav3.solver.cfop;

import static com.example.kostkav3.solver.cfop.Colors.*;

public class Face {
    private Cube cube;
    private final int[][] corLocs;
    private final int[] corSides;
    private final int[] edgeLocs;
    private final int faceColor;
    public Face(Cube setCube, int[][] setCorners, int[] sides, int[] setEdges, int setFaceColor) {
        cube = setCube;
        corLocs = setCorners;
        corSides = sides;
        edgeLocs = setEdges;
        faceColor = setFaceColor;
    }
    public String rotate(int direction) {
        Corner[] corners = cube.getCorners(corLocs, corSides);
        for (Corner c : corners) {
            c.setOrient(faceColor);
            c.setLoc(faceColor, direction);
        }
        Edge[] edges = cube.getEdges(edgeLocs);
        for (Edge e : edges) {
            e.setOrient(faceColor);
            e.setLoc(faceColor, direction);
        }
        return colorAsMove(faceColor, direction);
    }
    public int getFaceColor() {
        return faceColor;
    }
    public int[] getEdgeLocs() {
        return edgeLocs;
    }
    public int[][] getCorLocs() {
        return corLocs;
    }
    public int[] getCorSides() {
        return corSides;
    }

    public String toString() {
        Corner[] corners = cube.getCorners(corLocs, corSides);
        Edge[] edges = cube.getEdges(edgeLocs);
        int[] cOrder = {1,0,2,3};
        int[] eOrder = {0,3,1,2};
        if (faceColor == GREEN) {
            eOrder[0] = 2; eOrder[1] = 1; eOrder[2] = 3; eOrder[3] = 0;
            cOrder[0] = 3; cOrder[1] = 2; cOrder[2] = 0; cOrder[3] = 1;
        }
        String top = " \n\t" + colorsAsString(corners[cOrder[0]].colorOnFace(faceColor))
                + "\t\t" + colorsAsString(edges[eOrder[0]].colorOnFace(faceColor))
                + "\t\t" + colorsAsString(corners[cOrder[1]].colorOnFace(faceColor)) + "\n";
        String mid = "\t" + colorsAsString(edges[eOrder[1]].colorOnFace(faceColor))
                + "\t\t" + colorsAsString(faceColor)
                + "\t\t" + colorsAsString(edges[eOrder[2]].colorOnFace(faceColor)) + "\n";
        String bot = "\t" + colorsAsString(corners[cOrder[2]].colorOnFace(faceColor))
                + "\t\t" + colorsAsString(edges[eOrder[3]].colorOnFace(faceColor))
                + "\t\t" + colorsAsString(corners[cOrder[3]].colorOnFace(faceColor)) + "\n ";
        return top + mid + bot;
    }
}
