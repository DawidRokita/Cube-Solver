package com.example.kostkav3.solver.lbl;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.List;


public class Cube {

    // A Cube has 6 CubeSides:
    public CubeSide front, left, right, back, up, down;

    // Define the rules for each edge when being moved
    private static final EdgeRule[] F_EDGE_RULES = new EdgeRule[] {new EdgeRule('C', 2, false), new EdgeRule('R', 2, true), new EdgeRule('C', 0, false), new EdgeRule('R', 0, true)};
    private static final EdgeRule[] L_EDGE_RULES = new EdgeRule[] {new EdgeRule('C', 2, true), new EdgeRule('C', 0, true), new EdgeRule('C', 0, false), new EdgeRule('C', 0, false)};
    private static final EdgeRule[] R_EDGE_RULES = new EdgeRule[] {new EdgeRule('C', 2, false), new EdgeRule('C', 2, false), new EdgeRule('C', 0, true), new EdgeRule('C', 2, true)};
    private static final EdgeRule[] B_EDGE_RULES = new EdgeRule[] {new EdgeRule('C', 2, true), new EdgeRule('R', 0, false), new EdgeRule('C', 0, true), new EdgeRule('R', 2, false)};
    private static final EdgeRule[] U_EDGE_RULES = new EdgeRule[] {new EdgeRule('R', 0, false), new EdgeRule('R', 0, false), new EdgeRule('R', 0, false), new EdgeRule('R', 0, false)};
    private static final EdgeRule[] D_EDGE_RULES = new EdgeRule[] {new EdgeRule('R', 2, false), new EdgeRule('R', 2, false), new EdgeRule('R', 2, false), new EdgeRule('R', 2, false)};

    // User input regex pattern - First char is move (e.g. F = front face turn, Y = rotate cube on Y axis), optional second char is a modifier (nothing = clockwise, ' = anti-clockwise, 2 = two rotations)
    private Pattern inputPattern = Pattern.compile("^([FLRBUDMESXYZ])(['2])?$");
    private Matcher inputMatcher;

    private final ArrayList<String> moveHistory = new ArrayList<>();

    public List<String> getMoveHistory() {
        return moveHistory;
    }

    public void clearMoveHistory() {
        moveHistory.clear();
    }


    // Create an initial default cube (already solved)
    public Cube() {
        char[] cells = new char[9];
        
        // Fill the cells of each cube side with a different colour
        Arrays.fill(cells, 'R');
        front = new CubeSide(cells);

        Arrays.fill(cells, 'G');
        left = new CubeSide(cells);

        Arrays.fill(cells, 'B');
        right = new CubeSide(cells);

        Arrays.fill(cells, 'O');
        back = new CubeSide(cells);

        Arrays.fill(cells, 'W');
        up = new CubeSide(cells);

        Arrays.fill(cells, 'Y');
        down = new CubeSide(cells);
        
        // Set the adjacent sides for each cube side
        front.setAdjacentSides(new CubeSide[]{left, up, right, down});
        left.setAdjacentSides(new CubeSide[]{back, up, front, down});
        right.setAdjacentSides(new CubeSide[]{front, up, back, down});
        back.setAdjacentSides(new CubeSide[]{right, up, left, down});
        up.setAdjacentSides(new CubeSide[]{left, back, right, front});
        down.setAdjacentSides(new CubeSide[]{left, front, right, back});

        printCube();
    }


    // Create a cube from user input (as an array of characters)
    public Cube(char[] allCells) {
        // Set the cells of each cube side from a range of the user input array
        front = new CubeSide(Arrays.copyOfRange(allCells, 0, 9));
        left = new CubeSide(Arrays.copyOfRange(allCells, 9, 18));
        right = new CubeSide(Arrays.copyOfRange(allCells, 18, 27));
        back = new CubeSide(Arrays.copyOfRange(allCells, 27, 36));
        up = new CubeSide(Arrays.copyOfRange(allCells, 36, 45));
        down = new CubeSide(Arrays.copyOfRange(allCells, 45, 54));

        // Set the adjacent sides for each cube side
        front.setAdjacentSides(new CubeSide[]{left, up, right, down});
        left.setAdjacentSides(new CubeSide[]{back, up, front, down});
        right.setAdjacentSides(new CubeSide[]{front, up, back, down});
        back.setAdjacentSides(new CubeSide[]{right, up, left, down});
        up.setAdjacentSides(new CubeSide[]{left, back, right, front});
        down.setAdjacentSides(new CubeSide[]{left, front, right, back});

        printCube();
    }


    // Print a representation of the cube
    public void printCube() {
        up.printSide(7);
        
        for (int i = 0; i < 3; i++) {
            left.printRow(i);
            front.printRow(i);
            right.printRow(i);
            back.printRow(i);
            System.out.println();
        }
        
        down.printSide(7);
    }


    // Given an input string, make a move
    public void makeMove(String input) {
        System.out.println(input);

        inputMatcher = inputPattern.matcher(input);

        // First, check that the move is valid by matching it against the regex pattern
        if (!inputMatcher.matches()) {
            throw new IllegalArgumentException("Invalid move syntax.");
        }

        // Face turn moves
        if ("FLRBUD".contains(inputMatcher.group(1))) {
            CubeSide selectedSide = null;
            EdgeRule[] selectedEdgeRules = null;

            // Use a switch statement on the first input group to set the selected side / edge rules
            switch (inputMatcher.group(1)) {
                case "F":
                    selectedSide = front;
                    selectedEdgeRules = F_EDGE_RULES;
                    break;

                case "L":
                    selectedSide = left;
                    selectedEdgeRules = L_EDGE_RULES;
                    break;

                case "R":
                    selectedSide = right;
                    selectedEdgeRules = R_EDGE_RULES;
                    break;
                
                case "B":
                    selectedSide = back;
                    selectedEdgeRules = B_EDGE_RULES;
                    break;

                case "U":
                    selectedSide = up;
                    selectedEdgeRules = U_EDGE_RULES;
                    break;
                
                case "D":
                    selectedSide = down;
                    selectedEdgeRules = D_EDGE_RULES;
                    break;
            }

            // If there is no modifer, do a clockwise turn
            if (inputMatcher.group(2) == null) {
                selectedSide.rotateClockwise(selectedEdgeRules);
            }
            else switch (inputMatcher.group(2)) {
                // Do 2 clockwise turns
                case "2":
                    selectedSide.rotateClockwise(selectedEdgeRules);
                    selectedSide.rotateClockwise(selectedEdgeRules);
                    break;
                
                    // Do an counter-clockwise turn
                case "'":
                    selectedSide.rotateCounterClockwise(selectedEdgeRules);
                    break;
            }

        }
        // Slices and full cube rotation moves
        else {
            boolean counterClockwise = false;
            int noOfTurns = 1;

            if (inputMatcher.group(2) != null) {
                switch (inputMatcher.group(2)) {
                    // Do 2 clockwise turns
                    case "2":
                        noOfTurns = 2;
                        break;
                    
                    // Do an counter-clockwise turn
                    case "'":
                        counterClockwise = true;
                        break;
                }
            }
            

            for (int i = 0; i < noOfTurns; i++) {
                switch (inputMatcher.group(1)) {
                    case "M":
                        middleTurn(counterClockwise);
                        break;
                    
                    case "E":
                        equatorialTurn(counterClockwise);
                        break;

                    case "S":
                        standingTurn(counterClockwise);
                        break;

                    case "X":
                        rotateCubeOnX(counterClockwise);
                        break;

                    case "Y":
                        rotateCubeOnY(counterClockwise);
                        break;

                    case "Z":
                        rotateCubeOnZ(counterClockwise);
                        break;
                }
            }
            
        }

        moveHistory.add(input);


        // printCube();
    }


    private void middleTurn(boolean counterClockwise) {
        // Make copy of middle column on front face
        char[] frontCol = front.getCol(1);

        if (!counterClockwise) {
            front.setCol(1, up.getCol(1));
            up.setCol(1, CubeSide.reverse(back.getCol(1)));
            back.setCol(1, CubeSide.reverse(down.getCol(1)));
            down.setCol(1, frontCol);
        }
        else {
            front.setCol(1, down.getCol(1));
            down.setCol(1, CubeSide.reverse(back.getCol(1)));
            back.setCol(1, CubeSide.reverse(up.getCol(1)));
            up.setCol(1, frontCol);
        }
        
    }

    private void equatorialTurn(boolean counterClockwise) {
        // Make copy of middle row on front face
        char[] frontRow = front.getRow(1);

        if (!counterClockwise) {
            front.setRow(1, left.getRow(1));
            left.setRow(1, back.getRow(1));
            back.setRow(1, right.getRow(1));
            right.setRow(1, frontRow);
        }
        else {
            front.setRow(1, right.getRow(1));
            right.setRow(1, back.getRow(1));
            back.setRow(1, left.getRow(1));
            left.setRow(1, frontRow);
        }
    }

    private void standingTurn(boolean counterClockwise) {
        // Make copy of middle row on up face
        char[] upRow = up.getRow(1);

        if (!counterClockwise) {
            up.setRow(1, CubeSide.reverse(left.getCol(1)));
            left.setCol(1, down.getRow(1));
            down.setRow(1, CubeSide.reverse(right.getCol(1)));
            right.setCol(1, upRow);
        }
        else {
            up.setRow(1, right.getCol(1));
            right.setCol(1, CubeSide.reverse(down.getRow(1)));
            down.setRow(1, left.getCol(1));
            left.setCol(1, CubeSide.reverse(upRow));
        }
    }

    private void rotateCubeOnX(boolean counterClockwise) {
        if (!counterClockwise) {
            left.rotateCounterClockwise(L_EDGE_RULES);
            middleTurn(true);
            right.rotateClockwise(R_EDGE_RULES);
        }
        else {
            left.rotateClockwise(L_EDGE_RULES);
            middleTurn(false);
            right.rotateCounterClockwise(R_EDGE_RULES);
        }
    }

    private void rotateCubeOnY(boolean counterClockwise) {
        if (!counterClockwise) {
            up.rotateClockwise(U_EDGE_RULES);
            equatorialTurn(true);
            down.rotateCounterClockwise(D_EDGE_RULES);
        }
        else {
            up.rotateCounterClockwise(U_EDGE_RULES);
            equatorialTurn(false);
            down.rotateClockwise(D_EDGE_RULES);
        }
    }

    private void rotateCubeOnZ(boolean counterClockwise) {
        if (!counterClockwise) {
            back.rotateCounterClockwise(B_EDGE_RULES);
            standingTurn(false);
            front.rotateClockwise(F_EDGE_RULES);
        }
        else {
            back.rotateClockwise(B_EDGE_RULES);
            standingTurn(true);
            front.rotateCounterClockwise(F_EDGE_RULES);
        }
    }

}
