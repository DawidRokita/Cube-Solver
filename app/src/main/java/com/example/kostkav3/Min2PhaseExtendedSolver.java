package com.example.kostkav3;

import com.example.kostkav3.solver.twophase.Search;

public class Min2PhaseExtendedSolver implements CubeSolver {

    @Override
    public String getName() {
        return "TwoPhase_EXT";
    }

    @Override
    public SolverResult solve(String cubeState) {
        long start = System.currentTimeMillis();

        try {
            String result = new Search().solution(cubeState, 25, 100000000, 100000, 0);

            long time = System.currentTimeMillis() - start;

            if (result == null || result.startsWith("Error")) {
                return new SolverResult(getName(), result, Integer.MAX_VALUE, time, false);
            }

            return new SolverResult(getName(), result, countMoves(result), time, true);

        } catch (Exception e) {
            long time = System.currentTimeMillis() - start;
            return new SolverResult(getName(), e.getMessage(), Integer.MAX_VALUE, time, false);
        }
    }

    private int countMoves(String solution) {
        if (solution == null || solution.trim().isEmpty()) return 0;
        return solution.trim().split("\\s+").length;
    }
}