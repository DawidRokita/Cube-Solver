package com.example.kostkav3.solver.lbl;

public class CubeSide {
    private char[][] face;
    private CubeSide[] adjacentSides;


    public CubeSide(char[] cells) {
        if (cells.length != 9) {
            throw new IllegalArgumentException("The cells array must have 9 elements.");
        }

        face = new char[3][3];
        adjacentSides = new CubeSide[4];

        for (int i = 0; i < 9; i++) {
            face[i / 3][i % 3] = cells[i];
        }
    }

    public void setAdjacentSides(CubeSide[] sides) {
        if (sides.length != 4) {
            throw new IllegalArgumentException("The sides array must have 4 elements.");
        }

        adjacentSides = sides;
    }




    private char[] updateCol(CubeSide side, int sideNo, boolean reverseOrder, char[] saveNew, char[] saveOld) {
        saveNew = side.getCol(sideNo);
        // System.out.println("Setting column " + sideNo + " from [" + saveNew[0] + ", " + saveNew[1] + ", " + saveNew[2] + "] to [" + saveOld[0] + ", " + saveOld[1] + ", " + saveOld[2] + "]");
        
        if (reverseOrder) {
            side.setCol(sideNo, reverse(saveOld));
        }
        else {
            side.setCol(sideNo, saveOld);
        }

        return saveNew.clone();
    }

    private char[] updateRow(CubeSide side, int sideNo, boolean reverseOrder, char[] saveNew, char[] saveOld) {
        saveNew = side.getRow(sideNo);
        // System.out.println("Setting row " + sideNo + " from [" + saveNew[0] + ", " + saveNew[1] + ", " + saveNew[2] + "] to [" + saveOld[2] + ", " + saveOld[1] + ", " + saveOld[0] + "]");

        if (reverseOrder) {
            side.setRow(sideNo, reverse(saveOld));
        }
        else {
            side.setRow(sideNo, saveOld);
        }

        return saveNew.clone();
    }


    public void rotateClockwise(EdgeRule[] edgeRules) {
        // Make a copy of the current face
        char[][] faceCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            faceCopy[i] = face[i].clone();
        }

        // Rotate the current face
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                face[j][2-i] = faceCopy[i][j];
            }
        }


        char[] saveNew = new char[3];
        char[] saveOld = null;
        
        if (edgeRules[3].getSideType() == 'R') {
            saveOld = adjacentSides[3].getRow(edgeRules[3].getSideNo());
        }
        else if (edgeRules[3].getSideType() == 'C') {
            saveOld = adjacentSides[3].getCol(edgeRules[3].getSideNo());
        }
        
        for (int i = 0; i < adjacentSides.length; i++) {
            
            if (edgeRules[i].getSideType() == 'R') {
                saveOld = updateRow(adjacentSides[i], edgeRules[i].getSideNo(), edgeRules[i].getReverseOrder(), saveNew, saveOld);
            }
            else if (edgeRules[i].getSideType() == 'C') {
                saveOld = updateCol(adjacentSides[i], edgeRules[i].getSideNo(), edgeRules[i].getReverseOrder(), saveNew, saveOld);
            }
        
        }
    }

    public void rotateCounterClockwise(EdgeRule[] edgeRules) {
        // Make a copy of the current face
        char[][] faceCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            faceCopy[i] = face[i].clone();
        }

        // Rotate the current face
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                face[2-j][i] = faceCopy[i][j];
            }
        }


        char[] saveNew = new char[3];
        char[] saveOld = null;
        
        if (edgeRules[0].getSideType() == 'R') {
            saveOld = adjacentSides[0].getRow(edgeRules[0].getSideNo());
        }
        else if (edgeRules[0].getSideType() == 'C') {
            saveOld = adjacentSides[0].getCol(edgeRules[0].getSideNo());
        }
        
        for (int i = adjacentSides.length - 1; i >= 0; i--) {
            
            if (edgeRules[i].getSideType() == 'R') {
                saveOld = updateRow(adjacentSides[i], edgeRules[i].getSideNo(), edgeRules[(i + 1) % edgeRules.length].getReverseOrder(), saveNew, saveOld);
            }
            else if (edgeRules[i].getSideType() == 'C') {
                saveOld = updateCol(adjacentSides[i], edgeRules[i].getSideNo(), edgeRules[(i + 1) % edgeRules.length].getReverseOrder(), saveNew, saveOld);
            }
        
        }
    }

    

    public static char[] reverse(char[] arr) {
        return new char[] {arr[2], arr[1], arr[0]};
    }




    public char[] getRow(int rowNo) {
        return face[rowNo].clone();
    }

    public void setRow(int rowNo, char[] newRow) {
        if (newRow.length != 3) {
            throw new IllegalArgumentException("The newRow array must have 3 elements.");
        }

        face[rowNo] = newRow.clone();
    }



    public char[] getCol(int colNo) {
        return new char[] {face[0][colNo], face[1][colNo], face[2][colNo]};
    }

    public void setCol(int colNo, char[] newCol) {
        if (newCol.length != 3) {
            throw new IllegalArgumentException("The newCol array must have 3 elements.");
        }

        for (int i = 0; i < 3; i++) {
            face[i][colNo] = newCol[i];
        }
    }


    public char getCell(int row, int col) {
        return face[row][col];
    }


    public void printRow(int rowNo) {
        System.out.print(face[rowNo][0] + " " + face[rowNo][1] + " " + face[rowNo][2] + "  ");
    }

    public void printSide(int spacing) {
        System.out.println();

        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < spacing; x++) {
                System.out.print(" ");
            }
            printRow(i);
            System.out.println();
        }
        System.out.println();
    }
}
