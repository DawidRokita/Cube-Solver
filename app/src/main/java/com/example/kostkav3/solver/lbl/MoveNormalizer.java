package com.example.kostkav3.solver.lbl;

import java.util.*;

public class MoveNormalizer {

    private static class Orientation {
        char U='U', D='D', L='L', R='R', F='F', B='B';

        // x: obrót jak R (prawa ściana do góry)
        void rotX() {
            char oldU = U, oldF = F, oldD = D, oldB = B;
            U = oldB;
            F = oldU;
            D = oldF;
            B = oldD;
        }

        // y: obrót jak U (góra w prawo)
        void rotY() {
            char oldF = F, oldR = R, oldB = B, oldL = L;
            F = oldR;
            R = oldB;
            B = oldL;
            L = oldF;
        }

        // z: obrót jak F (przód zgodnie z ruchem wskazówek)
        void rotZ() {
            char oldU = U, oldR = R, oldD = D, oldL = L;
            U = oldL;
            R = oldU;
            D = oldR;
            L = oldD;
        }

        void applyRotation(char axis, int turnsCW) {
            turnsCW = ((turnsCW % 4) + 4) % 4;
            for (int i = 0; i < turnsCW; i++) {
                if (axis == 'X') rotX();
                else if (axis == 'Y') rotY();
                else if (axis == 'Z') rotZ();
            }
        }

        char mapFace(char face) {
            switch (face) {
                case 'U': return U;
                case 'D': return D;
                case 'L': return L;
                case 'R': return R;
                case 'F': return F;
                case 'B': return B;
                default:  return face;
            }
        }
    }

    private static class Move {
        char base;      // UDLRFB / X Y Z / M E S
        int turnsCW;    // 1,2,3 (3 = prime)
        Move(char base, int turnsCW) { this.base = base; this.turnsCW = turnsCW; }
    }

    /**
     * Wejście: string typu "R U y M' F2 ..."
     * Wyjście: tylko U D L R F B + ' + 2
     */
    public static String normalize(String moves) {
        if (moves == null || moves.trim().isEmpty()) return "";

        List<String> tokens = Arrays.asList(moves.trim().split("\\s+"));
        Orientation o = new Orientation();
        List<String> out = new ArrayList<>();

        for (String t : tokens) {
            if (t.isEmpty()) continue;

            Move m = parseMove(t);
            char b = m.base;
            int k = m.turnsCW;

            // 1) Obroty kostki: x/y/z -> tylko aktualizują orientację, nie wypisujemy ich
            if (b == 'X' || b == 'Y' || b == 'Z') {
                o.applyRotation(b, k);
                continue;
            }

            // 2) Środkowe warstwy M/E/S -> zamieniamy na ruchy ścian + "wchłaniamy" rotację
            if (b == 'M') {
                // M = L' R x'
                for (int i = 0; i < k; i++) {
                    out.add(remap(o, "L'"));
                    out.add(remap(o, "R"));
                    o.applyRotation('X', 3); // x'
                }
                continue;
            }
            if (b == 'E') {
                // E = D' U y'
                for (int i = 0; i < k; i++) {
                    out.add(remap(o, "D'"));
                    out.add(remap(o, "U"));
                    o.applyRotation('Y', 3); // y'
                }
                continue;
            }
            if (b == 'S') {
                // S = F B' z'
                for (int i = 0; i < k; i++) {
                    out.add(remap(o, "F"));
                    out.add(remap(o, "B'"));
                    o.applyRotation('Z', 3); // z'
                }
                continue;
            }

            // 3) Normalne ruchy ścian: mapowanie według orientacji
            if ("UDLRFB".indexOf(b) >= 0) {
                out.add(remap(o, toToken(b, k)));
            }
        }

        // Opcjonalnie: uprość sekwencję (np. R R -> R2, R R' -> nic)
        out = simplify(out);

        return String.join(" ", out);
    }

    private static String remap(Orientation o, String token) {
        Move m = parseMove(token);
        char mapped = o.mapFace(m.base);
        return toToken(mapped, m.turnsCW);
    }

    private static Move parseMove(String token) {
        token = token.trim();
        char base = Character.toUpperCase(token.charAt(0)); // obsługuje też x/y/z małe litery
        String suf = token.length() > 1 ? token.substring(1) : "";

        boolean two = suf.contains("2");
        boolean prime = suf.contains("'");

        int turnsCW;
        if (two) turnsCW = 2;
        else if (prime) turnsCW = 3;
        else turnsCW = 1;

        return new Move(base, turnsCW);
    }

    private static String toToken(char base, int turnsCW) {
        turnsCW = ((turnsCW % 4) + 4) % 4;
        if (turnsCW == 0) return ""; // powinno nie wystąpić po simplify
        if (turnsCW == 1) return "" + base;
        if (turnsCW == 2) return "" + base + "2";
        return "" + base + "'"; // 3
    }

    private static List<String> simplify(List<String> moves) {
        List<Move> parsed = new ArrayList<>();
        for (String s : moves) {
            if (s == null || s.isEmpty()) continue;
            Move m = parseMove(s);
            if ("UDLRFB".indexOf(m.base) >= 0) parsed.add(m);
        }

        Deque<Move> stack = new ArrayDeque<>();
        for (Move m : parsed) {
            if (!stack.isEmpty() && stack.peekLast().base == m.base) {
                Move last = stack.removeLast();
                int sum = (last.turnsCW + m.turnsCW) % 4;
                if (sum != 0) stack.addLast(new Move(m.base, sum));
            } else {
                stack.addLast(m);
            }
        }

        List<String> out = new ArrayList<>();
        for (Move m : stack) {
            out.add(toToken(m.base, m.turnsCW));
        }
        return out;
    }
}
