package com.example.kostkav3.solver.lbl;

import java.util.ArrayList;

public class LblSolverFacade {

    /**
     * Wejście: 54 znaki w kolejności URFDLB (UUU...RRR...FFF...DDD...LLL...BBB...)
     * Znaki: U,R,F,D,L,B (Twoje oznaczenia)
     *
     * Wyjście: sekwencja ruchów np. "F R U' ..."
     */
    public static String solveFromURFDLB(String state) {
        if (state == null || state.length() != 54) {
            throw new IllegalArgumentException("Stan musi mieć dokładnie 54 znaki.");
        }

        // wytnij ściany w kolejności URFDLB
        String U = state.substring(0, 9);
        String R = state.substring(9, 18);
        String F = state.substring(18, 27);
        String D = state.substring(27, 36);
        String L = state.substring(36, 45);
        String B = state.substring(45, 54);

        // przestaw kolejność na FLRBUD (tak oczekuje Cube(char[]))
        String reordered = F + L + R + B + U + D;

        // zamień litery (Twoje) -> kolory (solver)
        char[] cells = new char[54];
        for (int i = 0; i < 54; i++) {
            char c = reordered.charAt(i);
            cells[i] = mapToSolverColor(c);
        }

        Cube cube = new Cube(cells);

        ArrayList<String> moves = Solver.solve(cube);

        // złącz do jednego stringa
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(moves.get(i));
        }
        return sb.toString();
    }

    private static char mapToSolverColor(char c) {
        switch (c) {
            case 'U': return 'W'; // white
            case 'R': return 'R'; // red
            case 'F': return 'G'; // green
            case 'L': return 'O'; // orange
            case 'B': return 'B'; // blue
            case 'D': return 'Y'; // yellow
            default:
                throw new IllegalArgumentException("Nieznany znak w stanie kostki: " + c);
        }
    }
}
