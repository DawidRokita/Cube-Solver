package com.example.kostkav3;

import android.util.Log;

import com.example.kostkav3.solver.lbl.LblSolverFacade;
import com.example.kostkav3.solver.lbl.MoveNormalizer;

public class LblSolver implements CubeSolver {

    private static final String TAG = "LBL_SOLVER";

    @Override
    public String getName() {
        return "LBL";
    }

    @Override
    public SolverResult solve(String cubeState) {

        long start = System.currentTimeMillis();

        Log.d(TAG, "=================================");
        Log.d(TAG, "LBL START");
        Log.d(TAG, "Thread: " + Thread.currentThread().getName());

        try {

            // ==================================
            // TWÓJ LBL
            // ==================================

            String result = LblSolverFacade.solveFromURFDLB(cubeState);

            // normalizacja
            result = MoveNormalizer.normalize(result);

            // ==================================

            long time = System.currentTimeMillis() - start;

            if (result == null ||
                    result.isEmpty() ||
                    result.startsWith("Error")) {

                Log.d(TAG, "LBL FAILED");

                return new SolverResult(
                        getName(),
                        "",
                        Integer.MAX_VALUE,
                        time,
                        false
                );
            }

            SolverResult solverResult = new SolverResult(
                    getName(),
                    result,
                    countMoves(result),
                    time,
                    true
            );

            Log.d(TAG, "LBL SUCCESS");
            Log.d(TAG, "Moves: " + solverResult.moveCount);
            Log.d(TAG, "Time: " + solverResult.timeMs + " ms");
            Log.d(TAG, "Solution: " + solverResult.solution);
            Log.d(TAG, "=================================");

            return solverResult;

        } catch (Exception e) {

            long time = System.currentTimeMillis() - start;

            Log.e(TAG, "LBL ERROR", e);

            return new SolverResult(
                    getName(),
                    e.getMessage(),
                    Integer.MAX_VALUE,
                    time,
                    false
            );
        }
    }

    private int countMoves(String solution) {

        if (solution == null || solution.trim().isEmpty()) {
            return 0;
        }

        return solution.trim().split("\\s+").length;
    }
}