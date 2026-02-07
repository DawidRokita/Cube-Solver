package com.example.kostkav3.solver.cfop;

import java.util.Arrays;

import static java.lang.Math.abs;

import static com.example.kostkav3.solver.cfop.Colors.*;

public class Corner {
    /**
     * 0: White/yellow side is on correct face or opposite face
     * 1: Piece must be rotated CW to get to 0 orientation
     * -1: Piece must be rotated CCW to get to 0 orientation
     */
    private int orient;
    /**
     * (1,4,2): top right
     * (2,3,1): top left
     * (3,2,4): bottom left
     * (4,1,3): bottom right
     */
    private int[] loc;
    /**
     * Get from FaceColors class
     */
    private final int[] colors;
    /**
     * 1 if on front
     * -1 if on back
     */
    private int side;

    public Corner(int setOrient, int[] setLoc, int[] setColors, int setSide) {
        orient = setOrient;
        loc = setLoc;
        colors = setColors;
        side = setSide;
    }
    public int getOrient() {
        return orient;
    }
    public int[] getLoc() {
        return loc;
    }
    public int[] getColors() {
        return colors;
    }
    public int getSide() {
        return side;
    }

    /**
     * Re-orients the corner piece
     * should only be called on a corner that was just turned.
     * @param face color of face that is being turned, from FaceColor class
     */
    public void setOrient(int face) {
        if (abs(face) == YELLOW) {
            return;
        }
        if (!this.onSide(face)) {
            return;
        }
        // holds 1,2,3 or 4 depending on location on face
        int location = loc[abs(face) - 1];
        if (orient == 0) {
            if (location % 2 == 0) {
                orient = -1;
            } else {
                orient = 1;
            }
            return;
        } else if (orient == 1) {
            if (location % 2 == 0) {
                orient = 0;
            } else {
                orient = -1;
            }
            return;
        } else {
            if (location % 2 == 0) {
                orient = 1;
            } else {
                orient = 0;
            }
        }
    }

    /**
     * sets the location of a corner
     * Should only be called on a corner that has just been turned
     * @param face color of face that is being turned
     * @param direction 1 for CW or -1 for CCW
     */
    public void setLoc(int face, int direction) {
        int[] loc1 = {1,4,2};
        int[] loc2 = {2,3,1};
        int[] loc3 = {3,2,4};
        int[] loc4 = {4,1,3};
        if (!this.onSide(face)) {
            return;
        }
        if (face == YELLOW) {
            if (Arrays.equals(loc,loc1)) {
                loc = loc2;
                if (direction == -1) {
                    side *= -1;
                }
            } else {
                loc = loc1;
                if (direction == 1) {
                    side *= -1;
                }
            }
            return;
        }
        if (face == WHITE) {
            if (Arrays.equals(loc,loc4)) {
                loc = loc3;
                if (direction == 1) {
                    side *= -1;
                }
            } else {
                loc = loc4;
                if (direction == -1) {
                    side *= -1;
                }
            }
            return;
        }
        if (abs(face) == BLUE) {
            if (direction == -1) {
                loc[0] = loc[0] + 1;
                loc[1] = loc[1] - 1;
                loc[2] = loc[2] - 1;
                if (loc[0] == 5) {
                    loc[0] = 1;
                }
                if (loc[1] == 0) {
                    loc[1] = 4;
                }
                if (loc[2] == 0) {
                    loc[2] = 4;
                }
            } else {
                loc[0] = loc[0] - 1;
                loc[1] = loc[1] + 1;
                loc[2] = loc[2] + 1;
                if (loc[0] == 0) {
                    loc[0] = 4;
                }
                if (loc[1] == 5) {
                    loc[1] = 1;
                }
                if (loc[2] == 5) {
                    loc[2] = 1;
                }
            }
            return;
        }
        if (abs(face) == RED) {
            if (direction == 1) {
                if (Arrays.equals(loc,loc1)) {
                    loc = loc2;
                    side *= -1;
                } else if (Arrays.equals(loc,loc2)) {
                    loc = loc3;
                } else if (Arrays.equals(loc,loc3)) {
                    loc = loc4;
                    side *= -1;
                } else {
                    loc = loc1;
                }
            } else {
                if (Arrays.equals(loc,loc1)) {
                    loc = loc4;
                } else if (Arrays.equals(loc,loc4)) {
                    loc = loc3;
                    side *= -1;
                } else if (Arrays.equals(loc,loc3)) {
                    loc = loc2;
                } else {
                    loc = loc1;
                    side *= -1;
                }
            }
        }
    }

    /**
     * determines if corner is contained on given face.
     * @param face face being checked
     * @return if corner is on face
     */
    public boolean onSide(int face) {
        if (face == YELLOW) {
            return loc[0] <= 2;
        } else if (face == WHITE) {
            return loc[0] > 2;
        }
        if (abs(face) == BLUE) {
            return face == side;
        }
        if (side == 1) {
            if (face == RED) {
                return loc[0] == 1 || loc[0] == 4;
            } else {
                return loc[0] == 2 || loc[0] == 3;
            }
        } else {
            if (face == ORANGE) {
                return loc[0] == 1 || loc[0] == 4;
            } else {
                return loc[0] == 2 || loc[0] == 3;
            }
        }
    }
    public String toString() {
        String type = "TYPE: Corner Piece\n";
        String[] strColors = colorsAsString(colors);
        String color = "Color: [" + strColors[0] + "," + strColors[1] + "," + strColors[2] + "]\n";
        String corner;
        switch (loc[0]) {
            case 1:
                corner = "Top right";
                break;
            case 2:
                corner = "Top left";
                break;
            case 3:
                corner = "Bottom left";
                break;
            default:
                corner = "Bottom right";
        }
        String fb = "blue";
        if (side == -1) {
            fb = "green";
        }
        String location = "Location: " + corner + " on " + fb + " side" + "\n";

        String orientation = "Orientation: " + orient + "\n";
        String solved = "Solved? " + solved() + "\n ";
        return type + color + location + orientation + solved;
    }
    public int faceWithColor(int color) {
        if (colors[0] != color && colors[1] != color && colors[2] != color) {
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
     * The most shameful code in this project
     * @param face
     * @return
     */
    public int colorOnFace(int face) {
        if (!onSide(face)) {
            return 0;
        }
        int[] loc1 = {1,4,2};
        int[] loc3 = {3,2,4};
        if (colors[0]*colors[1]*colors[2] > 0) {
            if (orient == 0) {
                if (Arrays.equals(loc, loc1) || Arrays.equals(loc, loc3)) {
                    // same colors
                    return colors[abs(face) - 1];
                } else {
                    switch (abs(face)) {
                        case 1:
                            return colors[2];
                        case 2:
                            return colors[1];
                        default :
                            return colors[0];
                    }
                }
            } else if (orient == -1) {
                if (Arrays.equals(loc, loc1) || Arrays.equals(loc, loc3)) {
                    switch (abs(face)) {
                        case 1:
                            return colors[2];
                        case 2:
                            return colors[0];
                        default :
                            return colors[1];
                    }
                } else {
                    switch (abs(face)) {
                        case 1:
                            return colors[1];
                        case 2:
                            return colors[0];
                        default :
                            return colors[2];
                    }
                }
            } else {
                if ( Arrays.equals(loc,loc1) || Arrays.equals(loc,loc3)) {
                    switch (abs(face)) {
                        case 1:
                            return colors[1];
                        case 2:
                            return colors[2];
                        default :
                            return colors[0];
                    }
                } else {
                    switch (abs(face)) {
                        case 1:
                            return colors[0];
                        case 2:
                            return colors[2];
                        default :
                            return colors[1];
                    }
                }
            }
        } else {
            if (orient == 0) {
                if (Arrays.equals(loc, loc1) || Arrays.equals(loc, loc3)) {
                    switch (abs(face)) {
                        case 1:
                            return colors[2];
                        case 2:
                            return colors[1];
                        default :
                            return colors[0];
                    }
                } else {
                    // same colors
                    return colors[abs(face) - 1];
                }
            } else if (orient == -1) {
                if (Arrays.equals(loc, loc1) || Arrays.equals(loc, loc3)) {
                    switch (abs(face)) {
                        case 1:
                            return colors[0];
                        case 2:
                            return colors[2];
                        default :
                            return colors[1];
                    }
                } else {
                    switch (abs(face)) {
                        case 1:
                            return colors[1];
                        case 2:
                            return colors[2];
                        default :
                            return colors[0];
                    }
                }
            } else {
                if (Arrays.equals(loc,loc1) || Arrays.equals(loc,loc3)) {
                    switch (abs(face)) {
                        case 1:
                            return colors[1];
                        case 2:
                            return colors[0];
                        default :
                            return colors[2];
                    }
                } else {
                    switch (abs(face)) {
                        case 1:
                            return colors[2];
                        case 2:
                            return colors[0];
                        default :
                            return colors[1];
                    }
                }
            }
        }
    }
    public boolean solved() {
        if (orient != 0) {
            return false;
        }
        return onSide(colors[0]) && onSide(colors[1]) && onSide(colors[2]);
    }
}
