package com.example.kostkav3.solver.Korf_Thistlethwaite.korf;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CubeSolver {

    private static final int SOLVED = -1;
    private static final int TIMEOUT = -2;

    private static volatile boolean initialized = false;

    private final ArrayList<Byte> movePath = new ArrayList<>();
    private long deadlineUptimeMs;

    private long expanded; // licznik rozwinięć

    /**
     * Domyślne parametry: 15s timeout, maxBound 30
     */
    public String solve(Context context, String facelets54)
            throws IOException, ClassNotFoundException {
        return solve(context, facelets54, 15_000, 30, true);
    }

    /**
     * @param timeoutMs ile ms maksymalnie solver ma liczyć
     * @param maxBound maksymalny bound IDA* (głębokość)
     * @param logProgress czy logować postęp do Logcat
     */
    public String solve(Context context, String facelets54, long timeoutMs, int maxBound, boolean logProgress)
            throws IOException, ClassNotFoundException {

        if (context == null) throw new IllegalArgumentException("context == null");
        if (facelets54 == null || facelets54.length() != 54) {
            throw new IllegalArgumentException("facelets54 musi mieć 54 znaki. Jest: " +
                    (facelets54 == null ? "null" : facelets54.length()));
        }

        initOnce(context.getApplicationContext());

        RubiksCube cube = RubiksCube.fromURFDLBFacelets(facelets54);

        // Jeśli dodałeś walidację (polecam) – przerwij od razu:
        try {
            if (!cube.isValidState()) {
                return "Błąd: nielegalny stan kostki (wczytanie)";
            }
        } catch (Throwable ignored) {
            // jeśli nie wkleiłeś isValidState(), nic się nie dzieje
        }

        movePath.clear();
        expanded = 0;

        deadlineUptimeMs = SystemClock.uptimeMillis() + Math.max(1, timeoutMs);

        int bound = cube.getHeuristic();
        byte prevMove = RubiksCube.I;

        if (logProgress) {
            Log.d("KORF", "start heuristic=" + bound + " timeoutMs=" + timeoutMs + " maxBound=" + maxBound);
        }

        while (true) {
            if (SystemClock.uptimeMillis() > deadlineUptimeMs) {
                return "Brak rozwiązania (timeout)";
            }
            if (bound > maxBound) {
                return "Brak rozwiązania (limit głębokości)";
            }

            if (logProgress) {
                Log.d("KORF", "IDA bound=" + bound);
            }

            int t = searchInPlace(cube, 0, bound, prevMove, bound, logProgress);

            if (t == SOLVED) {
                return joinMoves(movePath);
            }
            if (t == TIMEOUT) {
                return "Brak rozwiązania (timeout)";
            }
            if (t == Integer.MAX_VALUE) {
                return "Brak rozwiązania";
            }

            bound = t;
        }
    }

    private static synchronized void initOnce(Context appContext)
            throws IOException, ClassNotFoundException {
        if (initialized) return;

        // Mapy ruchów - czyść przed init, bo init() używa put()
        if (!RubiksCube.allowedMovesMap.isEmpty()) {
            RubiksCube.allowedMovesMap.clear();
            RubiksCube.phaseTwoAllowedMovesMap.clear();
            RubiksCube.phaseThreeAllowedMovesMap.clear();
            RubiksCube.phaseFourAllowedMovesMap.clear();
        }
        RubiksCube.initializeAllowedMovesMaps();

        // permutationHashes tylko raz
        if (EdgeConfigurationNode.permutationHashes == null || EdgeConfigurationNode.permutationHashes.isEmpty()) {
            ArrayList<Byte> current = new ArrayList<>();
            ArrayList<Integer> possibleCases = new ArrayList<>(
                    Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
            );
            EdgeConfigurationNode.generatePermutationHashes(current, possibleCases);
        }

        // pruning tables tylko raz
        PruningTables.getSingletonInstance().getAllTablesFromFile(appContext);

        initialized = true;
    }

    /**
     * IDA* in-place:
     * - move, recurse, undo (bez klonów)
     * - pruning:
     *   (a) nie cofaj natychmiast inverse
     *   (b) nie rób 2 ruchów na tej samej ścianie
     *   (c) nie rób 2 ruchów na tej samej osi
     */
    private int searchInPlace(RubiksCube cube,
                              int g,
                              int bound,
                              byte prevMove,
                              int rootBound,
                              boolean logProgress) {

        // timeout
        if (SystemClock.uptimeMillis() > deadlineUptimeMs) return TIMEOUT;

        int h = cube.getHeuristic();
        int f = g + h;

        if (f > bound) return f;
        if (cube.isSolved()) return SOLVED;

        int min = Integer.MAX_VALUE;

        byte[] moves = RubiksCube.allowedMovesMap.get(prevMove);
        if (moves == null) moves = RubiksCube.allowedMovesMap.get(RubiksCube.I);
        if (moves == null) return Integer.MAX_VALUE;

        int prevFace = faceIndex(prevMove);
        int prevAxis = axisIndex(prevMove);

        for (byte move : moves) {

            if (SystemClock.uptimeMillis() > deadlineUptimeMs) return TIMEOUT;

            // (a) nie cofaj natychmiast
            if (RubiksCube.isInverse(prevMove, move)) continue;

            // (b) nie ta sama ściana po sobie
            int face = faceIndex(move);
            if (prevMove != RubiksCube.I && face == prevFace) continue;

            // (c) nie ta sama oś po sobie (R/L, U/D, F/B)
            int axis = axisIndex(move);
            if (prevMove != RubiksCube.I && axis == prevAxis) continue;

            // do move
            cube.doMove(move);
            movePath.add(move);
            expanded++;

            // log postępu co 5 mln (opcjonalnie)
            if (logProgress && (expanded % 5_000_000L == 0)) {
                Log.d("KORF", "expanded=" + expanded + " rootBound=" + rootBound + " g=" + g + " h=" + h + " path=" + movePath.size());
            }

            int t = searchInPlace(cube, g + 1, bound, move, rootBound, logProgress);

            if (t == SOLVED) return SOLVED;
            if (t == TIMEOUT) return TIMEOUT;

            if (t < min) min = t;

            // undo
            cube.doMove(RubiksCube.inverseMove(move));
            movePath.remove(movePath.size() - 1);
        }

        return min;
    }

    /**
     * Mapuje ruch na ścianę: R/L/U/D/F/B (0..5)
     */
    private int faceIndex(byte move) {
        switch (move) {
            case RubiksCube.R:
            case RubiksCube.R_PRIME:
            case RubiksCube.R2:
                return 0;
            case RubiksCube.L:
            case RubiksCube.L_PRIME:
            case RubiksCube.L2:
                return 1;
            case RubiksCube.U:
            case RubiksCube.U_PRIME:
            case RubiksCube.U2:
                return 2;
            case RubiksCube.D:
            case RubiksCube.D_PRIME:
            case RubiksCube.D2:
                return 3;
            case RubiksCube.F:
            case RubiksCube.F_PRIME:
            case RubiksCube.F2:
                return 4;
            case RubiksCube.B:
            case RubiksCube.B_PRIME:
            case RubiksCube.B2:
                return 5;
            default:
                return -1; // I
        }
    }

    /**
     * Oś: X = R/L, Y = U/D, Z = F/B
     */
    private int axisIndex(byte move) {
        switch (move) {
            case RubiksCube.R:
            case RubiksCube.R_PRIME:
            case RubiksCube.R2:
            case RubiksCube.L:
            case RubiksCube.L_PRIME:
            case RubiksCube.L2:
                return 0; // X

            case RubiksCube.U:
            case RubiksCube.U_PRIME:
            case RubiksCube.U2:
            case RubiksCube.D:
            case RubiksCube.D_PRIME:
            case RubiksCube.D2:
                return 1; // Y

            case RubiksCube.F:
            case RubiksCube.F_PRIME:
            case RubiksCube.F2:
            case RubiksCube.B:
            case RubiksCube.B_PRIME:
            case RubiksCube.B2:
                return 2; // Z

            default:
                return -1; // I
        }
    }

    private String joinMoves(ArrayList<Byte> moves) {
        if (moves == null || moves.isEmpty()) return "";
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
