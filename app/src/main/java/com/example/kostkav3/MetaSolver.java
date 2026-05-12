package com.example.kostkav3;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MetaSolver {

    private static final String TAG = "MetaSolver";

    private final List<CubeSolver> solvers;

    public MetaSolver() {
        solvers = Arrays.asList(
                new LblSolver(),
                new CfopSolver(),
                new Min2PhaseFastSolver(),
                new Min2PhaseExtendedSolver()
        );
    }

    public MetaSolverResult solve(String cubeState) {
        ExecutorService executor = Executors.newFixedThreadPool(solvers.size());
        List<Future<SolverResult>> futures = new ArrayList<>();
        List<SolverResult> allResults = new ArrayList<>();

        long globalStart = System.currentTimeMillis();

        Log.d(TAG, "==============================");
        Log.d(TAG, "START META-SOLVER");
        Log.d(TAG, "Liczba algorytmów: " + solvers.size());
        Log.d(TAG, "==============================");

        for (CubeSolver solver : solvers) {
            Callable<SolverResult> task = () -> {
                long start = System.currentTimeMillis();

                Log.d(TAG, "[" + solver.getName() + "] START");
                Log.d(TAG, "[" + solver.getName() + "] Thread: " + Thread.currentThread().getName());

                SolverResult result = solver.solve(cubeState);

                long end = System.currentTimeMillis();
                result.timeMs = end - start;
                result.score = score(result);

                Log.d(TAG, "[" + solver.getName() + "] KONIEC");
                Log.d(TAG, "[" + solver.getName() + "] Czas: " + result.timeMs + " ms");
                Log.d(TAG, "[" + solver.getName() + "] Ruchy: " + result.moveCount);
                Log.d(TAG, "[" + solver.getName() + "] Score: " + result.score);
                Log.d(TAG, "[" + solver.getName() + "] Sukces: " + result.success);
                Log.d(TAG, "[" + solver.getName() + "] Rozwiązanie: " + result.solution);

                return result;
            };

            futures.add(executor.submit(task));
        }

        SolverResult best = null;

        try {
            for (Future<SolverResult> future : futures) {
                SolverResult result = future.get();
                allResults.add(result);

                if (result.success) {
                    if (best == null || result.score < best.score) {
                        best = result;
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Błąd podczas działania meta-solvera", e);
        } finally {
            executor.shutdown();
        }

        long globalEnd = System.currentTimeMillis();

        Log.d(TAG, "==============================");
        Log.d(TAG, "PODSUMOWANIE ALGORYTMÓW");

        for (SolverResult r : allResults) {
            Log.d(TAG,
                    r.algorithmName +
                            " | sukces=" + r.success +
                            " | ruchy=" + r.moveCount +
                            " | czas=" + r.timeMs + " ms" +
                            " | score=" + r.score +
                            " | solution=" + r.solution
            );
        }

        Log.d(TAG, "------------------------------");

        if (best != null) {
            Log.d(TAG, "NAJLEPSZY ALGORYTM: " + best.algorithmName);
            Log.d(TAG, "NAJMNIEJ / NAJLEPIEJ WG SCORE: " + best.score);
            Log.d(TAG, "RUCHY: " + best.moveCount);
            Log.d(TAG, "CZAS: " + best.timeMs + " ms");
            Log.d(TAG, "ROZWIĄZANIE: " + best.solution);
        } else {
            Log.d(TAG, "Nie znaleziono poprawnego rozwiązania.");
        }

        Log.d(TAG, "Całkowity czas meta-solvera: " + (globalEnd - globalStart) + " ms");
        Log.d(TAG, "==============================");

        return new MetaSolverResult(best, allResults, globalEnd - globalStart);
    }

    private double score(SolverResult result) {
        if (!result.success) return Double.MAX_VALUE;
        return 0.7 * result.moveCount + 0.01 * result.timeMs;
    }
}
