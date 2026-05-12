package com.example.kostkav3;

import java.util.List;

public class MetaSolverResult {
    public SolverResult bestResult;
    public List<SolverResult> allResults;
    public long totalTimeMs;

    public MetaSolverResult(SolverResult bestResult, List<SolverResult> allResults, long totalTimeMs) {
        this.bestResult = bestResult;
        this.allResults = allResults;
        this.totalTimeMs = totalTimeMs;
    }
}