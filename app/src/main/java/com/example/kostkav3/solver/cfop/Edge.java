package com.example.kostkav3.solver.cfop;

import static java.lang.Math.abs;

import static com.example.kostkav3.solver.cfop.Colors.*;

public class Edge {
    /**
     * 1: Oriented
     * -1: Mis-oriented
     * (not in conventional sense)
     * Oriented if
     */
    private int orient;
    /**
     * Numbered 1-12
     */
    private int loc;
    /**
     * Get from FaceColors class
     */
    private final int[] colors;
    /**
     * which faces contain which side
     */
    final static int[] blue = {1,3,2,4};
    final static int[] green = {5,8,6,7};
    final static int[] yellow = {5,9,1,10};
    final static int[] white = {2,11,6,12};
    final static int[] red = {9,7,11,3};
    final static int[] orange = {10,4,12,8};
    //                                  -3      -2  -1      1   2       3
    //                                   0      1      2    3   4       5
    private final int[][] allColors = {orange,white,green,blue,yellow,red};

    public Edge(int setOrient, int setLoc, int[] setColors) {
        orient = setOrient;
        loc = setLoc;
        colors = setColors;
    }
    public int getLoc() {
        return loc;
    }
    public int getOrient() {
        return orient;
    }
    public int[] getColors() {
        return colors;
    }

    /**
     * Sets the new location of the edge piece.
     * @param face The face being rotated
     * @param direction CW or CCW
     */
    public void setLoc(int face, int direction) {
        if(!onSide(face)) {
            return;
        }
        // loops through allColors array
        for (int i = 0; i <= 5; i++) {
            // finds the face that is being rotated
            if ((i - 3 == face && i < 3) || (i - 2 == face && i >= 3)) {
                // loops through face's edges to find current location
                for (int j = 0; j < allColors[i].length; j++) {
                    if (loc == allColors[i][j]) {
                        if (direction == 1) {
                            if (j == 3) {
                                loc = allColors[i][0];
                            } else {
                                loc = allColors[i][j + 1];
                            }
                        } else {
                            if (j == 0) {
                                loc = allColors[i][3];
                            } else {
                                loc = allColors[i][j - 1];
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Orients the edge. Should be called on every turn involving this piece.
     * @param face The face that is being rotated.
     */
    public void setOrient(int face) {
        if (abs(face) == BLUE && onSide(face)) {
            orient *= -1;
        }
    }

    /**
     * checks to see if the edge piece is on the given face.
     * @param face the given face
     * @return whether the piece is on the face or not.
     */
    public boolean onSide(int face) {
        for (int i = 0; i <= 5; i++) {
            if ((i - 3 == face && i < 3) || (i - 2 == face && i >= 3)) {
                for (int j : allColors[i]) {
                    if (j == loc) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public int faceWithColor(int color) {
        if (colors[0] != color && colors[1] != color) {
            return 0;
        }
        for (int i = -3; i <= 3; i++) {
            if (i == 0) {
                i++;
            }
            if (colorOnFace(i) == color) {
                return i;
            }
        }
        return 0;
    }
    /**
     * NOTE: For pieces on green side, assume green side faces you
     * @return
     */
    public String toString() {
        String type = "TYPE: Edge Piece\n";
        String[] strColors = colorsAsString(colors);
        String color = "Color: [" + strColors[0] + "," + strColors[1] + "]\n";;
        String slot;
        switch (loc) {
            case 1 :
                slot = "Front top";
                break;
            case 2 :
                slot = "Front bottom";
                break;
            case 3 :
                slot = "Front right";
                break;
            case 4 :
                slot = "Front left";
                break;
            case 5 :
                slot = "Back top";
                break;
            case 6 :
                slot = "Back bottom";
                break;
            case 7 :
                slot = "Back left on green side";
                break;
            case 8 :
                slot = "Back right on green side";
                break;
            case 9 :
                slot = "Top right";
                break;
            case 10 :
                slot = "Top left";
                break;
            case 11 :
                slot = "Bottom right";
                break;
            default :
                slot = "Bottom left";
        }
        String location = "Location: " + slot + "\n";
        String oriented = "Yes";
        if (orient == -1) {
            oriented = "No";
        }
        String orientation = "Oriented? " + oriented + "\n";
        String solved = "Solved? " + solved() + "\n ";
        return type + color + location + orientation + solved;
    }
    public int colorOnFace(int face) {
        if (!onSide(face)) {
            return 0;
        }
        int i = 0;
        int j = 1;
        if (abs(colors[1]) != RED) {
            i = 1;
            j = 0;
        }
        if (loc == 3 || loc == 4 || loc == 7 || loc == 8) {
            if (orient == -1) {
                if (abs(face) == RED) {
                    return colors[i];
                }
                return colors[j];
            } else {
                if (abs(face) == RED) {
                    return colors[j];
                }
                return colors[i];
            }
        } else {
            if (orient == -1) {
                if (abs(face) == YELLOW) {
                    return colors[j];
                }
                return colors[i];
            } else {
                if (abs(face) == YELLOW) {
                    return colors[i];
                }
                return colors[j];
            }
        }
    }
    /**
     * determines whether the edge is permuted and oriented correctly
     * @return true if the piece is solved.
     */
    public boolean solved() {
        if (orient != 1) {
            return false;
        }
        return onSide(colors[0]) && onSide(colors[1]);
    }
}
