package com.example.kostkav3;

public class SolverResult {
    public String algorithmName;
    public String solution;
    public int moveCount;
    public long timeMs;
    public double score;
    public boolean success;

    public SolverResult(String algorithmName, String solution, int moveCount, long timeMs, boolean success) {
        this.algorithmName = algorithmName;
        this.solution = solution;
        this.moveCount = moveCount;
        this.timeMs = timeMs;
        this.success = success;
    }
}