package com.example.kostkav3.solver.cfop;

import java.util.ArrayList;

public final class Colors {
    public static final int BLUE = 1;
    public static final int GREEN = -1;
    public static final int YELLOW = 2;
    public static final int WHITE = -2;
    public static final int RED = 3;
    public static final int ORANGE = -3;

    public static String[] colorsAsString(int[] cols) {
        String[] strColors = new String[cols.length];
        for (int i = 0; i < strColors.length; i++) {
            strColors[i] = colorsAsString(cols[i]);
        }
        return strColors;
    }
    public static String colorsAsString(int col) {
        switch (col) {
            case -3:
                return "Orange";
            case -2:
                return "White";
            case -1:
                return "Green";
            case 1:
                return "Blue";
            case 2:
                return "Yellow";
            case 3:
                return "Red\t";
        }
        return "invalid";
    }
    public static String colorAsMove(int face, int direction) {
        String move;
        switch (face) {
            case Colors.ORANGE :
                move = "L";
                break;
            case Colors.WHITE :
                move = "D";
                break;
            case Colors.GREEN :
                move = "B";
                break;
            case Colors.BLUE :
                move = "F";
                break;
            case Colors.YELLOW :
                move = "U";
                break;
            default :
                move = "R";
        }
        if (direction == -1) {
            move += "'";
        }
        return move;
    }
    public static void fixSolution(ArrayList<String> solution) {
        String currentMove;
        String nextMove;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < solution.size(); i++) {
                currentMove = solution.get(i);
                try {
                    nextMove = solution.get(i + 1);
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                String bigMove = currentMove + nextMove;
                if (currentMove.substring(0, 1).equals(nextMove.substring(0, 1))) {
                    if (currentMove.equals(nextMove)) {
                        if (currentMove.endsWith("2")) {
                            solution.remove(i);
                            solution.remove(i);
                        } else {
                            solution.remove(i);
                            solution.set(i, currentMove.substring(0, 1));
                            solution.set(i, solution.get(i) + "2");
                        }
                    } else {
                        if (!bigMove.contains("2")) {
                            solution.remove(i);
                            solution.remove(i);
                        } else {
                            solution.remove(i);
                            if (bigMove.contains("'")) {
                                solution.set(i, bigMove.substring(0, 1));
                            } else {
                                solution.set(i, bigMove.substring(0, 1) + "'");
                            }
                        }
                    }
                    i--;
                }
            }
        }
    }
}
