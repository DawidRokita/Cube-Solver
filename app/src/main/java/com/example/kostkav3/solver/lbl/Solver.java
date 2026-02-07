package com.example.kostkav3.solver.lbl;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Solver {
    


    public static void main(String[] args) {
        // // Setup dummy print stream to disable println
        // PrintStream originalStream = System.out;

        // PrintStream dummyStream = new PrintStream(new OutputStream(){
        //     public void write(int b) { }
        // });

        // System.setOut(dummyStream);


        // // Create a new cube and shuffle it
        // Cube cube = new Cube();
        // ArrayList<String> shuffleMoves = new ArrayList<String>();
        // shuffleCube(cube, shuffleMoves);

        // // Enable the original print stream
        // System.setOut(originalStream);

        // // Print cube, solve it
        // System.out.println("Shuffle moves:\n" + shuffleMoves.toString());



        // Create a new cube from user input
        Cube cube = makeNewCube();

        System.out.println("Initial cube state:");
        cube.printCube();
        solve(cube);

    }


    // Start with default cube (already solved), then interact with it
    private static void interactiveMode(Cube cube) {
        Scanner sc = new Scanner(System.in);
        String input = "";

        while (!input.equals("QUIT")) {
            System.out.println("Enter a move (or 'QUIT' to quit):");
            input = sc.nextLine().toUpperCase();

            if (!input.equals("QUIT")) {
                cube.makeMove(input);
            }
        }

        sc.close();
    }


    // Create a cube by using user input for colour of each cell
    private static Cube makeNewCube() {
        char[] inputColours = new char[54];

        Scanner sc = new Scanner(System.in);
        String input = "";

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                input = "";

                while (!input.equals("W") && !input.equals("G") && !input.equals("R") && !input.equals("B") && !input.equals("O") && !input.equals("Y")) {
                    System.out.println("Enter the colour ('R', 'G', 'B', 'O', 'W', 'Y') of cell " + (j + 1) + " on the " + getFaceName(i) + " face:");
                    input = sc.nextLine().toUpperCase();
                }
                
                inputColours[(9 * i) + j] = input.charAt(0);
            }
        }

        sc.close();
        return new Cube(inputColours);
    }

    private static String getFaceName(int sideNo) {
        switch (sideNo) {
            case 0:
                return "front";
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "back";
            case 4:
                return "top";
            case 5:
                return "bottom";
        }
        return null;
    }

    private static Cube shuffleCube(Cube cube, ArrayList<String> movesMade) {
        Random rand = new Random();
        String[] possibleMoves = new String[] {
            "F",  "L",  "R",  "B",  "U",  "D",
            "F'", "L'", "R'", "B'", "U'", "D'",
            "F2", "L2", "R2", "B2", "U2", "D2"
        };

        // Make 50 random moves
        for (int i = 0; i < 25; i++) {
            int randInt = rand.nextInt(possibleMoves.length);
            cube.makeMove(possibleMoves[randInt]);
            movesMade.add(possibleMoves[randInt]);
        }

        return cube;
    }

    public static ArrayList<String> solve(Cube cube) {
        cube.clearMoveHistory();

        System.out.println("-------- Solving Cube --------");
        ArrayList<String> movesMade = new ArrayList<String>();

        System.out.println("--- Stage 1: White Cross ---");
        // Get the white center piece on top of cube
        if (cube.front.getCell(1, 1) == 'W') {
            cube.makeMove("X");
        }
        else if (cube.left.getCell(1, 1) == 'W') {
            cube.makeMove("Z");
        }
        else if (cube.right.getCell(1, 1) == 'W') {
            cube.makeMove("Z'");
        }
        else if (cube.back.getCell(1, 1) == 'W') {
            cube.makeMove("X'");
        }
        else if (cube.down.getCell(1, 1) == 'W') {
            cube.makeMove("X2");
        }

        
        // Get the red center piece on front of cube
        if (cube.left.getCell(1, 1) == 'R') {
            cube.makeMove("Y'");
        }
        else if (cube.right.getCell(1, 1) == 'R') {
            cube.makeMove("Y");
        }
        else if (cube.back.getCell(1, 1) == 'R') {
            cube.makeMove("Y2");
        }


        char[] sideColours = new char[] {'R', 'B', 'O', 'G'};

        // Move each white edge piece into position
        for (char colour : sideColours) {

            // Case 1: piece on bottom face
            if (cube.down.getCell(0, 1) == 'W' && cube.front.getCell(2, 1) == colour) {
                cube.makeMove("F2");
            }
            else if (cube.down.getCell(1, 0) == 'W' && cube.left.getCell(2, 1) == colour) {
                cube.makeMove("D");
                cube.makeMove("F2");
            }
            else if (cube.down.getCell(1, 2) == 'W' && cube.right.getCell(2, 1) == colour) {
                cube.makeMove("D'");
                cube.makeMove("F2");
            }
            else if (cube.down.getCell(2, 1) == 'W' && cube.back.getCell(2, 1) == colour) {
                cube.makeMove("D2");
                cube.makeMove("F2");
            }
            // Case 2: piece on bottom layer, facing outwards
            else if (cube.front.getCell(2, 1) == 'W' && cube.down.getCell(0, 1) == colour) {
                cube.makeMove("F");
                cube.makeMove("E");
                cube.makeMove("F'");
            }
            else if (cube.left.getCell(2, 1) == 'W' && cube.down.getCell(1, 0) == colour) {
                cube.makeMove("D");
                cube.makeMove("F");
                cube.makeMove("E");
                cube.makeMove("F'");
            }
            else if (cube.right.getCell(2, 1) == 'W' && cube.down.getCell(1, 2) == colour) {
                cube.makeMove("D'");
                cube.makeMove("F");
                cube.makeMove("E");
                cube.makeMove("F'");
            }
            else if (cube.back.getCell(2, 1) == 'W' && cube.down.getCell(2, 1) == colour) {
                cube.makeMove("D2");
                cube.makeMove("F");
                cube.makeMove("E");
                cube.makeMove("F'");
            }
            // Case 3: piece on middle layer
            else if (cube.front.getCell(1, 2) == 'W' && cube.right.getCell(1, 0) == colour) {
                cube.makeMove("E'");
                cube.makeMove("F");
            }
            else if (cube.left.getCell(1, 2) == 'W' && cube.front.getCell(1, 0) == colour) {
                cube.makeMove("F");
            }
            else if (cube.right.getCell(1, 2) == 'W' && cube.back.getCell(1, 0) == colour) {
                cube.makeMove("E2");
                cube.makeMove("F");
            }
            else if (cube.back.getCell(1, 2) == 'W' && cube.left.getCell(1, 0) == colour) {
                cube.makeMove("E");
                cube.makeMove("F");
            }
            else if (cube.front.getCell(1, 0) == 'W' && cube.left.getCell(1, 2) == colour) {
                cube.makeMove("E");
                cube.makeMove("F'");
            }
            else if (cube.left.getCell(1, 0) == 'W' && cube.back.getCell(1, 2) == colour) {
                cube.makeMove("E2");
                cube.makeMove("F'");
            }
            else if (cube.right.getCell(1, 0) == 'W' && cube.front.getCell(1, 2) == colour) {
                cube.makeMove("F'");
            }
            else if (cube.back.getCell(1, 0) == 'W' && cube.right.getCell(1, 2) == colour) {
                cube.makeMove("E'");
                cube.makeMove("F'");
            }
            // Case 4: piece on top layer, facing outwards
            else if (cube.front.getCell(0, 1) == 'W' && cube.up.getCell(2, 1) == colour) {
                cube.makeMove("F");
                cube.makeMove("E'");
                cube.makeMove("F");
            }
            else if (cube.left.getCell(0, 1) == 'W' && cube.up.getCell(1, 0) == colour) {
                cube.makeMove("L");
                cube.makeMove("F");
            }
            else if (cube.right.getCell(0, 1) == 'W' && cube.up.getCell(1, 2) == colour) {
                cube.makeMove("R'");
                cube.makeMove("F'");
            }
            else if (cube.back.getCell(0, 1) == 'W' && cube.up.getCell(0, 1) == colour) {
                cube.makeMove("B");
                cube.makeMove("E");
                cube.makeMove("F");
            }
            // Case 5: piece on top face
            else if (cube.up.getCell(1, 0) == 'W' && cube.left.getCell(0, 1) == colour) {
                // we can assume that this is the first colour (red), otherwise the piece couldn't be in this position
                cube.makeMove("U'");
            }
            else if (cube.up.getCell(1, 2) == 'W' && cube.right.getCell(0, 1) == colour) {
                cube.makeMove("R'");
                cube.makeMove("E'");
                cube.makeMove("F");
            }
            else if (cube.up.getCell(0, 1) == 'W' && cube.back.getCell(0, 1) == colour) {
                cube.makeMove("B");
                cube.makeMove("E2");
                cube.makeMove("F'");
            }

            cube.makeMove("Y");
        }

        // Line up edges with center pieces
        if (cube.left.getCell(1, 1) == 'R') {
            cube.makeMove("E");
        }
        else if (cube.right.getCell(1, 1) == 'R') {
            cube.makeMove("E'");
        }
        else if (cube.back.getCell(1, 1) == 'R') {
            cube.makeMove("E2");
        }

        cube.printCube();

        System.out.println("--- Stage 2: White Corners ---");

        String[] whiteCornerCombinations = new String[] {"WGR", "WRB", "WBO", "WOG"};

        for (String cornerCombo : whiteCornerCombinations) {
            
            // Case 1: in correct corner, wrong orientation
            if (cube.front.getCell(0, 0) == cornerCombo.charAt(0) && cube.up.getCell(2, 0) == cornerCombo.charAt(1) && cube.left.getCell(0, 2) == cornerCombo.charAt(2)) {
                cube.makeMove("F'");
                cube.makeMove("D'");
                cube.makeMove("F");
            }
            else if (cube.left.getCell(0, 2) == cornerCombo.charAt(0) && cube.front.getCell(0, 0) == cornerCombo.charAt(1) && cube.up.getCell(2, 0) == cornerCombo.charAt(2)) {
                cube.makeMove("L");
                cube.makeMove("D");
                cube.makeMove("L'");
            }
            // Case 2: bottom layer, wrong corner
            if (cornerCombo.indexOf(cube.back.getCell(2, 2)) >= 0 && cornerCombo.indexOf(cube.left.getCell(2, 0)) >= 0 && cornerCombo.indexOf(cube.down.getCell(2, 0)) >= 0) {
                cube.makeMove("D");
            }
            else if (cornerCombo.indexOf(cube.front.getCell(2, 2)) >= 0 && cornerCombo.indexOf(cube.right.getCell(2, 0)) >= 0 && cornerCombo.indexOf(cube.down.getCell(0, 2)) >= 0) {
                cube.makeMove("D'");
            }
            else if (cornerCombo.indexOf(cube.right.getCell(2, 2)) >= 0 && cornerCombo.indexOf(cube.back.getCell(2, 0)) >= 0 && cornerCombo.indexOf(cube.down.getCell(2, 2)) >= 0) {
                cube.makeMove("D2");
            }
            // Case 3: top layer, wrong corner
            else if (cornerCombo.indexOf(cube.up.getCell(0, 0)) >= 0 && cornerCombo.indexOf(cube.back.getCell(0, 2)) >= 0 && cornerCombo.indexOf(cube.left.getCell(0, 0)) >= 0) {
                cube.makeMove("B");
                cube.makeMove("D");
                cube.makeMove("B'");
            }
            else if (cornerCombo.indexOf(cube.up.getCell(2, 2)) >= 0 && cornerCombo.indexOf(cube.front.getCell(0, 2)) >= 0 && cornerCombo.indexOf(cube.right.getCell(0, 0)) >= 0) {
                cube.makeMove("R'");
                cube.makeMove("D'");
                cube.makeMove("R");
            }
            else if (cornerCombo.indexOf(cube.up.getCell(0, 2)) >= 0 && cornerCombo.indexOf(cube.right.getCell(0, 2)) >= 0 && cornerCombo.indexOf(cube.back.getCell(0, 0)) >= 0) {
                cube.makeMove("R");
                cube.makeMove("D2");
                cube.makeMove("R'");
            }
            // Case 4: bottom layer, under destination corner
            if (cube.left.getCell(2, 2) == cornerCombo.charAt(0) && cube.down.getCell(0, 0) == cornerCombo.charAt(1) && cube.front.getCell(2, 0) == cornerCombo.charAt(2)) {
                cube.makeMove("L");
                cube.makeMove("D");
                cube.makeMove("L'");
            }
            else if (cube.front.getCell(2, 0) == cornerCombo.charAt(0) && cube.left.getCell(2, 2) == cornerCombo.charAt(1) && cube.down.getCell(0, 0) == cornerCombo.charAt(2)) {
                cube.makeMove("F'");
                cube.makeMove("D'");
                cube.makeMove("F");
            }
            else if (cube.down.getCell(0, 0) == cornerCombo.charAt(0) && cube.front.getCell(2, 0) == cornerCombo.charAt(1) && cube.left.getCell(2, 2) == cornerCombo.charAt(2)) {
                cube.makeMove("L");
                cube.makeMove("B");
                cube.makeMove("D2");
                cube.makeMove("B'");
                cube.makeMove("L'");
            }

            cube.makeMove("Y");
        }

        cube.printCube();

        System.out.println("--- Stage 3: Second Layer ---");

        cube.makeMove("X2");
        cube.makeMove("Y2");

        String[] edgeCombinations = new String[] {"RB", "GR", "OG", "BO"};

        for (String edgeCombo : edgeCombinations) {
            
            // Check middle layer for edge piece
            if (cube.left.getCell(1, 2) == edgeCombo.charAt(0) && cube.front.getCell(1, 0) == edgeCombo.charAt(1)) {
                // wrong orientation - do left algorithm to get piece out of position
                cube.makeMove("U'");
                cube.makeMove("L'");
                cube.makeMove("U");
                cube.makeMove("L");
                cube.makeMove("U");
                cube.makeMove("F");
                cube.makeMove("U'");
                cube.makeMove("F'");
            }
            else if (edgeCombo.indexOf(cube.front.getCell(1, 2)) >= 0 && edgeCombo.indexOf(cube.right.getCell(1, 0)) >= 0) {
                cube.makeMove("U'");
                cube.makeMove("F'");
                cube.makeMove("U");
                cube.makeMove("F");
                cube.makeMove("U");
                cube.makeMove("R");
                cube.makeMove("U'");
                cube.makeMove("R'");
            }
            else if (edgeCombo.indexOf(cube.right.getCell(1, 2)) >= 0 && edgeCombo.indexOf(cube.back.getCell(1, 0)) >= 0) {
                cube.makeMove("U'");
                cube.makeMove("R'");
                cube.makeMove("U");
                cube.makeMove("R");
                cube.makeMove("U");
                cube.makeMove("B");
                cube.makeMove("U'");
                cube.makeMove("B'");
            }
            else if (edgeCombo.indexOf(cube.back.getCell(1, 2)) >= 0 && edgeCombo.indexOf(cube.left.getCell(1, 0)) >= 0) {
                cube.makeMove("U'");
                cube.makeMove("B'");
                cube.makeMove("U");
                cube.makeMove("B");
                cube.makeMove("U");
                cube.makeMove("L");
                cube.makeMove("U'");
                cube.makeMove("L'");
            }
            
            // Move edge piece into correct position on top layer
            if ((cube.left.getCell(0, 1) == edgeCombo.charAt(0) && cube.up.getCell(1, 0) == edgeCombo.charAt(1)) || (cube.back.getCell(0, 1) == edgeCombo.charAt(1) && cube.up.getCell(0, 1) == edgeCombo.charAt(0))) {
                cube.makeMove("U'");
            }
            else if ((cube.right.getCell(0, 1) == edgeCombo.charAt(0) && cube.up.getCell(1, 2) == edgeCombo.charAt(1)) || (cube.front.getCell(0, 1) == edgeCombo.charAt(1) && cube.up.getCell(2, 1) == edgeCombo.charAt(0))) {
                cube.makeMove("U");
            }
            else if ((cube.back.getCell(0, 1) == edgeCombo.charAt(0) && cube.up.getCell(0, 1) == edgeCombo.charAt(1)) || (cube.right.getCell(0, 1) == edgeCombo.charAt(1) && cube.up.getCell(1, 2) == edgeCombo.charAt(0))) {
                cube.makeMove("U2");
            }

            // Depending on orientation of piece on top layer, do either left/right algorithm
            if (cube.front.getCell(0, 1) == edgeCombo.charAt(0) && cube.up.getCell(2, 1) == edgeCombo.charAt(1)) {
                cube.makeMove("U'");
                cube.makeMove("L'");
                cube.makeMove("U");
                cube.makeMove("L");
                cube.makeMove("U");
                cube.makeMove("F");
                cube.makeMove("U'");
                cube.makeMove("F'");
            }
            else if (cube.left.getCell(0, 1) == edgeCombo.charAt(1) && cube.up.getCell(1, 0) == edgeCombo.charAt(0)) {
                cube.makeMove("U");
                cube.makeMove("F");
                cube.makeMove("U'");
                cube.makeMove("F'");
                cube.makeMove("U'");
                cube.makeMove("L'");
                cube.makeMove("U");
                cube.makeMove("L");
            }

            cube.makeMove("Y");

        }

        cube.printCube();

        System.out.println("--- Stage 4: Yellow Cross ---");

        if (cube.up.getCell(0, 1) != 'Y' && cube.up.getCell(1, 0) != 'Y' && cube.up.getCell(1, 2) != 'Y' && cube.up.getCell(2, 1) != 'Y') {
            // scenario 1: yellow dot
            cube.makeMove("F");
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U'");
            cube.makeMove("R'");
            cube.makeMove("F'");
        }
        if (cube.up.getCell(0, 1) == 'Y' && cube.up.getCell(2, 1) == 'Y') {
            // scenario 2: yellow line (vertical)
            cube.makeMove("Y");
        }
        if (cube.up.getCell(1, 0) == 'Y' && cube.up.getCell(1, 2) == 'Y') {
            // scenario 2: yellow line (horizontal)
            cube.makeMove("F");
            cube.makeMove("R");
            cube.makeMove("U");
            cube.makeMove("R'");
            cube.makeMove("U'");
            cube.makeMove("F'");
        }

        if (!(cube.up.getCell(0, 1) == 'Y' && cube.up.getCell(1, 0) == 'Y' && cube.up.getCell(1, 2) == 'Y' && cube.up.getCell(2, 1) == 'Y')) {
            // scenario 3: yellow L - orient into correct position first
            if (cube.up.getCell(0, 1) == 'Y' && cube.up.getCell(1, 2) == 'Y') {
                cube.makeMove("Y'");
            }
            else if (cube.up.getCell(1, 0) == 'Y' && cube.up.getCell(2, 1) == 'Y') {
                cube.makeMove("Y");
            }
            else if (cube.up.getCell(1, 2) == 'Y' && cube.up.getCell(2, 1) == 'Y') {
                cube.makeMove("Y2");
            }

            cube.makeMove("F");
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U'");
            cube.makeMove("R'");
            cube.makeMove("F'");
        }

        // Orient cube so red is front
        if (cube.left.getCell(1, 1) == 'R') {
            cube.makeMove("Y'");
        }
        else if (cube.right.getCell(1, 1) == 'R') {
            cube.makeMove("Y");
        }
        else if (cube.back.getCell(1, 1) == 'R') {
            cube.makeMove("Y2");
        }
        
        cube.printCube();

        System.out.println("--- Stage 5: Yellow Edges ---");

        // Rotate top face so red-yellow edge in correct position
        if (cube.left.getCell(0, 1) == 'R') {
            cube.makeMove("U'");
        }
        else if (cube.right.getCell(0, 1) == 'R') {
            cube.makeMove("U");
        }
        else if (cube.back.getCell(0, 1) == 'R') {
            cube.makeMove("U2");
        }

        // Step 1: get blue-yellow edge into correct position
        if (cube.right.getCell(0, 1) == 'B') {
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U");
            cube.makeMove("R'");
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U2");
            cube.makeMove("R'");
            cube.makeMove("U");
        }
        else if (cube.back.getCell(0, 1) == 'B') {
            cube.makeMove("Y'");
            cube.makeMove("R");
            cube.makeMove("U");
            cube.makeMove("R'");
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U2");
            cube.makeMove("R'");
            cube.makeMove("U");
            cube.makeMove("Y");
        }

        // Step 2: get green-yellow and orange-yellow edges into correct positions
        if (cube.right.getCell(0, 1) == 'O') {
            cube.makeMove("Y2");
            cube.makeMove("R");
            cube.makeMove("U");
            cube.makeMove("R'");
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U2");
            cube.makeMove("R'");
            cube.makeMove("U");
            cube.makeMove("Y2");
        }

        cube.printCube();

        System.out.println("--- Stage 6: Position Yellow Corners ---");

        String[] yellowCornerCombinations = new String[] {"YRG", "YGO", "YOB", "YBR"};
        boolean cornerFound = false;

        // First, look for a corner already in the correct position.
        for (String cornerCombo : yellowCornerCombinations) {

            // If front-right corner in position, set boolean to true
            if (cornerCombo.indexOf(cube.front.getCell(0, 2)) >= 0 && cornerCombo.indexOf(cube.right.getCell(0, 0)) >= 0 && cornerCombo.indexOf(cube.up.getCell(2, 2)) >= 0) {
                cornerFound = true;
                break;
            }
            else {
                cube.makeMove("Y");
            }
        }
        
        // If no corners in position, perform algorithm to get one corner in position
        if (!cornerFound) {
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U'");
            cube.makeMove("L'");
            cube.makeMove("U");
            cube.makeMove("R'");
            cube.makeMove("U'");
            cube.makeMove("L");

            // Rotate cube until top-right corner in position
            for (String cornerCombo : yellowCornerCombinations) {
                if (cornerCombo.indexOf(cube.front.getCell(0, 2)) >= 0 && cornerCombo.indexOf(cube.right.getCell(0, 0)) >= 0 && cornerCombo.indexOf(cube.up.getCell(2, 2)) >= 0) {
                    break;
                }
                else {
                    cube.makeMove("Y");
                }
            }
        }
        
        // Create a string for the target front-left corner
        String targetCorner = "Y" + cube.left.getCell(1, 1) + cube.front.getCell(1, 1);

        // While the front-left corner isn't in position, perform the algorithm
        while (!(targetCorner.indexOf(cube.left.getCell(0, 2)) >= 0 && targetCorner.indexOf(cube.front.getCell(0, 0)) >= 0 && targetCorner.indexOf(cube.up.getCell(2, 0)) >= 0)) {
            cube.makeMove("U");
            cube.makeMove("R");
            cube.makeMove("U'");
            cube.makeMove("L'");
            cube.makeMove("U");
            cube.makeMove("R'");
            cube.makeMove("U'");
            cube.makeMove("L");
        }

        cube.printCube();

        System.out.println("--- Stage 7: Orient Yellow Corners ---");

        // Check for a non-yellow piece on top face, use U moves to get into position
        while (true) {
            if (cube.up.getCell(0, 2) != 'Y') {
                cube.makeMove("U");
            }
            else if (cube.up.getCell(2, 0) != 'Y') {
                cube.makeMove("U'");
            }
            else if (cube.up.getCell(0, 0) != 'Y') {
                cube.makeMove("U2");
            }
            else if (cube.up.getCell(2, 2) == 'Y') {
                // All yellow corners correctly oriented!
                break;
            }

            while (cube.up.getCell(2, 2) != 'Y') {
                    cube.makeMove("R'");
                    cube.makeMove("D'");
                    cube.makeMove("R");
                    cube.makeMove("D");
            }
        }

        // Orient cube so red is front
        if (cube.left.getCell(1, 1) == 'R') {
            cube.makeMove("Y'");
        }
        else if (cube.right.getCell(1, 1) == 'R') {
            cube.makeMove("Y");
        }
        else if (cube.back.getCell(1, 1) == 'R') {
            cube.makeMove("Y2");
        }

        // Rotate top layer to solve
        if (cube.left.getCell(0, 1) == 'R') {
            cube.makeMove("U'");
        }
        else if (cube.right.getCell(0, 1) == 'R') {
            cube.makeMove("U");
        }
        else if (cube.back.getCell(0, 1) == 'R') {
            cube.makeMove("U2");
        }

        cube.printCube();

//        return movesMade;
        return new ArrayList<>(cube.getMoveHistory());
    }
}

/*
 Todo:
    - Replace all 'slice' turns with equivalent face turn sequences (slice turns aren't technically allowed)
    - Create a list of all moves made, return the size of list at end
    - Check for duplicate/undoing moves that can be simplified
        - e.g. U and U' cancel each other out
        - e.g. F and F2 is equivalent to F'
    - General code tidying
*/