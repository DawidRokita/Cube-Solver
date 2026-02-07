package com.example.kostkav3.solver.cfop;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.kostkav3.solver.cfop.Colors.*;

public class Cube {
    private Corner[] corners = new Corner[8];
    private  Edge[] edges = new Edge[12];
    private Face[] faces = new Face[6];
    public Cube() {
        // initialize corners of the cube
        // initializes 1-4 on front and then back
        for (int i = 0; i < corners.length/2; i++) {
            int[][] setLoc = {{1,4,2},{2,3,1},{3,2,4},{4,1,3}};
            int[] corCol = {BLUE, Colors.YELLOW, RED};
            int[][] setLoc2 = {{1,4,2},{2,3,1},{3,2,4},{4,1,3}};
            int[] corCol2 = {GREEN, YELLOW, ORANGE};
            if (i > 1) {
                corCol[1] = WHITE;
                corCol2[1] = WHITE;
            }
            if (i > 0 && i < 3) {
                corCol[2] = ORANGE;
                corCol2[2] = RED;
            }
            corners[i] = new Corner(0, setLoc[i], corCol, 1);
            corners[i + 4] = new Corner(0, setLoc2[i], corCol2, -1);
        }
        // initialize the edges of the cube
        // initializes in order of initial location number
        int j = -1;
        for (int i = 0; i < edges.length; i++) {
            int[][] edgeCol = {{1,2},{1,3},{-1,2},{-1,3},{2,3},{-2,3}};
            if (i % 2 == 1) {
                edgeCol[j][1] *= -1;
            } else {
                j++;
            }
            edges[i] = new Edge(1, i + 1, edgeCol[j]);
        }
        // initialize the faces of the cube
        int[][] corLocs = {{2,3,1},{1,4,2},{4,1,3},{3,2,4}};
        int[] sides = {1,-1,-1,1};
        int [] edges = {10,4,12,8};
        faces[0] = new Face(this, corLocs,sides,edges,ORANGE);
        int [] sides3 = {-1,1,1,-1};
        int [] edges3 = {9,7,11,3};
        faces[5] = new Face(this, corLocs,sides3,edges3,RED);
        int[][] corLocs1 = {{4,1,3},{3,2,4},{4,1,3},{3,2,4}};
        int[] sides1 = {1,1,-1,-1};
        int [] edges1 = {2,11,6,12};
        faces[1] = new Face(this, corLocs1,sides1,edges1,WHITE);
        int [][] corLocsOther1 = {{2,3,1},{1,4,2},{2,3,1},{1,4,2}};
        int [] sides4 = {-1,-1,1,1};
        int [] edges4 = {5,9,1,10};
        faces[4] = new Face(this, corLocsOther1,sides4,edges4,YELLOW);
        int[][] corLocs2 = {{1,4,2},{2,3,1},{3,2,4},{4,1,3}};
        int[] sides2 = {-1,-1,-1,-1};
        int [] edges2 = {5,8,6,7};
        faces[2] = new Face(this, corLocs2,sides2,edges2,GREEN);
        int [] sides5 = {1,1,1,1};
        int [] edges5 = {1,3,2,4};
        faces[3] = new Face(this, corLocs2,sides5,edges5,BLUE);
    }
    public Corner getCorner(int color1, int color2, int color3) {
        for (Corner c : corners) {
            if (c.getColors()[0] == color1 && c.getColors()[1] == color2 && c.getColors()[2] == color3) {
                return c;
            }
        }
        return null;
    }
    public Corner getCorner(int loc, int side) {
        for (Corner c : corners) {
            if (c.getLoc()[0] == loc && c.getSide() == side) {
                return c;
            }
        }
        return null;
    }
    public Corner[] getCorners(int[][] loc, int[] side) {
        Corner[] foundCorners = new Corner[loc.length];
        int count = 0;
        for (Corner c : corners) {
            for (int l = 0; l < loc.length; l++) {
                if (Arrays.equals(c.getLoc(), loc[l]) && c.getSide() == side[l]) {
                    foundCorners[count] = c;
                    count++;
                }
            }
        }
        return foundCorners;
    }
    public Edge getEdge(int color1, int color2) {
        for (Edge e : edges) {
            if ((e.getColors()[0] == color1 && e.getColors()[1] == color2)
                    || (e.getColors()[0] == color2 && e.getColors()[1] == color1)) {
                return e;
            }
        }
        return null;
    }
    public Edge getEdge(int loc) {
        for (Edge e : edges) {
            if ((e.getLoc() == loc)) {
                return e;
            }
        }
        return null;
    }
    public Edge[] getEdges(int[] loc) {
        Edge[] faceEdges = new Edge[loc.length];
        int count = 0;
        for (Edge e : edges) {
            for (int l : loc) {
                if (e.getLoc() == l) {
                    faceEdges[count] = e;
                    count++;
                }
            }
        }
        return faceEdges;
    }
    public Face getFace(int face) {
        for (Face f : faces) {
            if (f.getFaceColor() == face) {
                return f;
            }
        }
        return null;
    }
    public String toString() {
        String orange = faces[0].toString();
        String white = faces[1].toString();
        String green = faces[2].toString();
        String blue = faces[3].toString();
        String yellow = faces[4].toString();
        String red = faces[5].toString();
        String all = green + "\n" + yellow + "\n" + orange + "\t" + blue + "\t" + red + "\n" + white;
        return all;
    }
    public boolean solved() {
        for (Corner c : corners) {
            if (!c.solved()) {
                return false;
            }
        }
        for (Edge e : edges) {
            if (!e.solved()) {
                return false;
            }
        }
        return true;
    }
    public void mix(ArrayList<String> mix) {
        for (String s : mix) {
            mix(s);
        }
    }
    public String[] mix(String mix) {
        String[] turns = mix.split(" ");
        for (String t : turns) {
            switch (t) {
                case "F":
                    F();
                    break;
                case "F'":
                    Fi();
                    break;
                case "Fi":
                    Fi();
                    break;
                case "B":
                    B();
                    break;
                case "B'":
                    Bi();
                    break;
                case "Bi":
                    Bi();
                    break;
                case "L":
                    L();
                    break;
                case "L'":
                    Li();
                    break;
                case "Li":
                    Li();
                    break;
                case "R":
                    R();
                    break;
                case "R'":
                    Ri();
                    break;
                case "Ri":
                    Ri();
                    break;
                case "U":
                    U();
                    break;
                case "U'":
                    Ui();
                    break;
                case "Ui":
                    Ui();
                    break;
                case "D":
                    D();
                    break;
                case "D'":
                    Di();
                    break;
                case "Di":
                    Di();
                    break;
                case "F2":
                    F2();
                    break;
                case "B2":
                    B2();
                    break;
                case "L2":
                    L2();
                    break;
                case "R2":
                    R2();
                    break;
                case "U2":
                    U2();
                    break;
                case "D2":
                    D2();
                    break;
                default:
                    System.out.println("Invalid operation");
            }
        }
        return turns;
    }
    public String F() {
        return getFace(BLUE).rotate(1);
    }
    public String Fi() {
        return getFace(BLUE).rotate(-1);
    }
    public String F2() {
        F();
        F();
        return "F2";
    }
    public String B() {
        return getFace(GREEN).rotate(1);
    }
    public String Bi() {
        return getFace(GREEN).rotate(-1);
    }
    public String B2() {
        B();
        B();
        return "B2";
    }
    public String R() {
        return getFace(RED).rotate(1);
    }
    public String Ri() {
        return getFace(RED).rotate(-1);
    }
    public String R2() {
        R();
        R();
        return "R2";
    }
    public String L() {
        return getFace(ORANGE).rotate(1);
    }
    public String Li() {
        return getFace(ORANGE).rotate(-1);
    }
    public String L2() {
        L();
        L();
        return "L2";
    }
    public String U() {
        return getFace(YELLOW).rotate(1);
    }
    public String Ui() {
        return getFace(YELLOW).rotate(-1);
    }
    public String U2() {
        U();
        U();
        return "U2";
    }
    public String D() {
        return getFace(WHITE).rotate(1);
    }
    public String Di() {
        return getFace(WHITE).rotate(-1);
    }
    public String D2() {
        D();
        D();
        return "D2";
    }
}
