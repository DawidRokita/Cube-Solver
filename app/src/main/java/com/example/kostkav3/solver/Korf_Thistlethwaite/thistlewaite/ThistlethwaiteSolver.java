package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import android.os.SystemClock;

import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.util.ArrayList;
import java.util.HashSet;

public class ThistlethwaiteSolver {

    // Maksima z klasycznej analizy (HTM). W praktyce zwykle mniej. :contentReference[oaicite:1]{index=1}
    private static final int MAX_P1 = 7;
    private static final int MAX_P2 = 13;
    private static final int MAX_P3 = 15;
    private static final int MAX_P4 = 17;

    // Timeout całego solve (ms) – ustawiaj w appce jak chcesz
    private final long timeoutMs;

    private long deadline;
    private final ArrayList<Byte> solutionMoves = new ArrayList<>();

    public ThistlethwaiteSolver(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public String solve(String facelets54) {
        if (facelets54 == null || facelets54.length() != 54) {
            return "Błąd: stan musi mieć 54 znaki";
        }

        RubiksCube.initializeAllowedMovesMaps();
        RubiksCube cube = RubiksCube.fromURFDLBFacelets(facelets54);

        deadline = SystemClock.uptimeMillis() + Math.max(1, timeoutMs);
        solutionMoves.clear();

        // Faza 1: EO (edge orientation)
        ArrayList<Byte> p1 = phaseSearch(cube, Phase.P1, MAX_P1);
        if (p1 == null) return "Brak rozwiązania (timeout/faza1)";
        applyMoves(cube, p1);
        solutionMoves.addAll(p1);

        // Faza 2: Domino reduction (CO + slice edges)
        ArrayList<Byte> p2 = phaseSearch(cube, Phase.P2, MAX_P2);
        if (p2 == null) return "Brak rozwiązania (timeout/faza2)";
        applyMoves(cube, p2);
        solutionMoves.addAll(p2);

        // Faza 3: Half-turn reduction (orbits)
        ArrayList<Byte> p3 = phaseSearch(cube, Phase.P3, MAX_P3);
        if (p3 == null) return "Brak rozwiązania (timeout/faza3)";
        applyMoves(cube, p3);
        solutionMoves.addAll(p3);

        // Faza 4: final solve (tylko połówki)
        ArrayList<Byte> p4 = phaseSearch(cube, Phase.P4, MAX_P4);
        if (p4 == null) return "Brak rozwiązania (timeout/faza4)";
        applyMoves(cube, p4);
        solutionMoves.addAll(p4);

        return joinMoves(solutionMoves);
    }

    // ---------------- PHASE SEARCH (IDDFS) ----------------

    private enum Phase { P1, P2, P3, P4 }

    private ArrayList<Byte> phaseSearch(RubiksCube cube, Phase phase, int maxDepth) {
        for (int depth = 0; depth <= maxDepth; depth++) {
            ArrayList<Byte> path = new ArrayList<>();
            // proste "visited w ścieżce": nie wracaj do identycznych stanów w tej fazie
            HashSet<Long> seen = new HashSet<>();
            if (dfs(cube, phase, depth, RubiksCube.I, path, seen)) {
                return path;
            }
            if (SystemClock.uptimeMillis() > deadline) return null;
        }
        return null;
    }

    private boolean dfs(RubiksCube cube,
                        Phase phase,
                        int depthLeft,
                        byte prevMove,
                        ArrayList<Byte> path,
                        HashSet<Long> seen) {

        if (SystemClock.uptimeMillis() > deadline) return false;

        if (isPhaseGoal(cube, phase)) return true;
        if (depthLeft == 0) return false;

        // Hash stanu dla "seen" (lekki, nie idealny, ale działa)
        long key = phaseStateKey(cube, phase);
        if (!seen.add(key)) return false;

        byte[] moves = allowedMovesForPhase(prevMove, phase);
        if (moves == null) return false;

        int prevFace = faceIndex(prevMove);
        int prevAxis = axisIndex(prevMove);

        for (byte mv : moves) {
            if (SystemClock.uptimeMillis() > deadline) return false;

            // 1) nie cofaj natychmiast
            if (RubiksCube.isInverse(prevMove, mv)) continue;

            // 2) nie ta sama ściana po sobie
            int f = faceIndex(mv);
            if (prevMove != RubiksCube.I && f == prevFace) continue;

            // 3) nie ta sama oś po sobie
            int ax = axisIndex(mv);
            if (prevMove != RubiksCube.I && ax == prevAxis) continue;

            cube.doMove(mv);
            path.add(mv);

            if (dfs(cube, phase, depthLeft - 1, mv, path, seen)) return true;

            path.remove(path.size() - 1);
            cube.doMove(RubiksCube.inverseMove(mv));
        }

        return false;
    }

    private byte[] allowedMovesForPhase(byte prevMove, Phase phase) {
        // Uwaga: te mapy już masz w RubiksCube.initializeAllowedMovesMaps()
        switch (phase) {
            case P1:
                return RubiksCube.allowedMovesMap.get(prevMove);
            case P2:
                return RubiksCube.phaseTwoAllowedMovesMap.get(prevMove);
            case P3:
                return RubiksCube.phaseThreeAllowedMovesMap.get(prevMove);
            case P4:
                return RubiksCube.phaseFourAllowedMovesMap.get(prevMove);
            default:
                return null;
        }
    }

    // ---------------- PHASE GOALS ----------------

    // Faza 1: wszystkie krawędzie "nieodwrócone"
    private boolean goalP1(RubiksCube c) {
        for (int i = 0; i < 12; i++) {
            if ((c.edges[i] % 2) != 0) return false;
        }
        return true;
    }

    // Faza 2: wszystkie rogi zorientowane + środkowe krawędzie w E-slice
    private boolean goalP2(RubiksCube c) {
        if (!goalP1(c)) return false; // EO ma zostać

        for (int i = 0; i < 8; i++) {
            if ((c.corners[i] % 3) != 0) return false;
        }
        // pozycje fr, fl, bl, br = indeksy 4..7
        for (int pos = 4; pos <= 7; pos++) {
            int pieceId = (c.edges[pos] / 2);
            if (pieceId < 4 || pieceId > 7) return false; // musi być jednym ze środkowych
        }
        return true;
    }

    // Faza 3: HTR – orbits (tetrady rogów + slice krawędzi)
    private boolean goalP3(RubiksCube c) {
        if (!goalP2(c)) return false;

        // orbit/tetrady rogów (klasyczny podział dla half-turn group)
        // Tetrada A: {URF(0), ULB(2), DLF(5), DRB(7)}
        // Tetrada B: {UFL(1), UBR(3), DBL(4), DFR(6)}
        int[] A = {0, 2, 5, 7};
        int[] B = {1, 3, 4, 6};

        for (int pos = 0; pos < 8; pos++) {
            int pieceId = (c.corners[pos] / 3);
            boolean posInA = isIn(pos, A);
            boolean pieceInA = isIn(pieceId, A);
            if (posInA != pieceInA) return false;
        }

        // Edges: po fazie 2 mamy środek OK, teraz dopilnuj, że "górno-dolne" są w górno-dolnych slotach
        // Sloty 0..3 i 8..11 powinny zawierać tylko edgeId z {0,1,2,3,8,9,10,11}
        for (int pos : new int[]{0,1,2,3,8,9,10,11}) {
            int id = (c.edges[pos] / 2);
            if (!(id <= 3 || id >= 8)) return false;
        }

        return true;
    }

    // Faza 4: pełne rozwiązanie (tylko połówki)
    private boolean goalP4(RubiksCube c) {
        return c.isSolved();
    }

    private boolean isPhaseGoal(RubiksCube c, Phase phase) {
        switch (phase) {
            case P1: return goalP1(c);
            case P2: return goalP2(c);
            case P3: return goalP3(c);
            case P4: return goalP4(c);
            default: return false;
        }
    }

    // ---------------- STATE KEY (dla seen) ----------------

    private long phaseStateKey(RubiksCube c, Phase phase) {
        // Prosty hash na fazę: nie perfekcyjny, ale działa jako "anti-loop".
        // Uwaga: to NIE jest pełny hash kostki – tylko do przycięcia w IDDFS.
        long h = 1469598103934665603L;
        if (phase == Phase.P1) {
            for (int i = 0; i < 12; i++) h = fnv(h, (c.edges[i] & 1));
        } else if (phase == Phase.P2) {
            for (int i = 0; i < 12; i++) h = fnv(h, (c.edges[i] & 1));
            for (int i = 0; i < 8; i++) h = fnv(h, (c.corners[i] % 3));
            for (int i = 4; i <= 7; i++) h = fnv(h, (c.edges[i] / 2));
        } else if (phase == Phase.P3) {
            for (int i = 0; i < 8; i++) h = fnv(h, (c.corners[i] / 3));
            for (int i = 0; i < 12; i++) h = fnv(h, (c.edges[i] / 2));
        } else { // P4
            for (int i = 0; i < 8; i++) h = fnv(h, c.corners[i]);
            for (int i = 0; i < 12; i++) h = fnv(h, c.edges[i]);
        }
        return h;
    }

    private long fnv(long h, int v) {
        h ^= (v & 0xFF);
        return h * 1099511628211L;
    }

    private boolean isIn(int x, int[] set) {
        for (int v : set) if (v == x) return true;
        return false;
    }

    // ---------------- MOVE HELPERS ----------------

    private int faceIndex(byte move) {
        switch (move) {
            case RubiksCube.R:
            case RubiksCube.R_PRIME:
            case RubiksCube.R2: return 0;
            case RubiksCube.L:
            case RubiksCube.L_PRIME:
            case RubiksCube.L2: return 1;
            case RubiksCube.U:
            case RubiksCube.U_PRIME:
            case RubiksCube.U2: return 2;
            case RubiksCube.D:
            case RubiksCube.D_PRIME:
            case RubiksCube.D2: return 3;
            case RubiksCube.F:
            case RubiksCube.F_PRIME:
            case RubiksCube.F2: return 4;
            case RubiksCube.B:
            case RubiksCube.B_PRIME:
            case RubiksCube.B2: return 5;
            default: return -1;
        }
    }

    private int axisIndex(byte move) {
        switch (move) {
            case RubiksCube.R:
            case RubiksCube.R_PRIME:
            case RubiksCube.R2:
            case RubiksCube.L:
            case RubiksCube.L_PRIME:
            case RubiksCube.L2: return 0; // X
            case RubiksCube.U:
            case RubiksCube.U_PRIME:
            case RubiksCube.U2:
            case RubiksCube.D:
            case RubiksCube.D_PRIME:
            case RubiksCube.D2: return 1; // Y
            case RubiksCube.F:
            case RubiksCube.F_PRIME:
            case RubiksCube.F2:
            case RubiksCube.B:
            case RubiksCube.B_PRIME:
            case RubiksCube.B2: return 2; // Z
            default: return -1;
        }
    }

    private void applyMoves(RubiksCube cube, ArrayList<Byte> moves) {
        for (byte m : moves) cube.doMove(m);
    }

    private String joinMoves(ArrayList<Byte> moves) {
        StringBuilder sb = new StringBuilder();
        for (byte m : moves) {
            String s = RubiksCube.getMoveString(m);
            if (s == null || s.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(s);
        }
        return sb.toString().trim();
    }
}
