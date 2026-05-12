package com.example.kostkav3;

import android.util.Log;

public class CfopSolver implements CubeSolver {

    private static final String TAG = "CFOP_SOLVER";

    @Override
    public String getName() {
        return "CFOP";
    }

    @Override
    public SolverResult solve(String cubeState) {

        long start = System.currentTimeMillis();

        try {
            String report = com.example.kostkav3.solver.cfop.CfopSolver.solveFromURFDLB(cubeState);

            Log.d(TAG, "CFOP REPORT:");
            Log.d(TAG, report);

            String result = com.example.kostkav3.solver.cfop.CfopSolver.extractMovesFromReport(report);

            long time = System.currentTimeMillis() - start;

            if (result == null || result.trim().isEmpty() || result.startsWith("Error")) {

                return new SolverResult(getName(), "", Integer.MAX_VALUE, time, false);
            }

            SolverResult solverResult = new SolverResult(getName(), result, countMoves(result), time, true);

            Log.d(TAG, "=================================");
            Log.d(TAG, "CFOP SUCCESS");
            Log.d(TAG, "Moves: " + solverResult.moveCount);
            Log.d(TAG, "Time: " + solverResult.timeMs + " ms");
            Log.d(TAG, "Solution: " + solverResult.solution);
            Log.d(TAG, "=================================");

            return solverResult;

        } catch (Exception e) {

            long time = System.currentTimeMillis() - start;

            Log.e(TAG, "CFOP ERROR", e);

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