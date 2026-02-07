package com.example.kostkav3.solver.cfop;

import com.example.kostkav3.solver.twophase.Search;

import java.util.ArrayList;
import java.util.Map;

import static com.example.kostkav3.solver.cfop.Colors.*;

public class CfopSolver {


    public static String getSolutionReportFromFacelets(String state54) {

        //TwoPhase: state -> solved
        String twoPhaseSol = new Search().solution(state54, 21, 100000000, 5000, 0);
        if (twoPhaseSol == null || twoPhaseSol.trim().isEmpty() || twoPhaseSol.toLowerCase().contains("brak")) {
            return "Brak rozwiązania (TwoPhase)";
        }

        String scramble = invertAlgorithm(twoPhaseSol);

        // CFOP na scramble (w ich orientacji: WHITE na dole, GREEN z przodu)
        Cube cube = new Cube();
        cube.mix(scramble);

        StringBuilder sb = new StringBuilder();

//        sb.append("SCRAMBLE:\n").append(scramble).append("\n\n");

        // ===================== CROSS =====================
        sb.append("CROSS SOLUTION\n");

        Edge e1 = cube.getEdge(WHITE, -3);
        Edge e2 = cube.getEdge(WHITE, -1);
        Edge e3 = cube.getEdge(WHITE,  1);
        Edge e4 = cube.getEdge(WHITE,  3);

        Map<Edge, ArrayList<String>> cross = Cross.solve(cube);

        sb.append("Cross piece #1:\n").append(listToMoves(cross.get(e1))).append("\n");
        sb.append("Cross piece #2:\n").append(listToMoves(cross.get(e2))).append("\n");
        sb.append("Cross piece #3:\n").append(listToMoves(cross.get(e3))).append("\n");
        sb.append("Cross piece #4:\n").append(listToMoves(cross.get(e4))).append("\n\n");

        // ===================== F2L =====================
        sb.append("F2L SOLUTION\n");

        Corner c1 = cube.getCorner(BLUE,  WHITE, RED);
        Corner c2 = cube.getCorner(BLUE,  WHITE, ORANGE);
        Corner c3 = cube.getCorner(GREEN, WHITE, ORANGE);
        Corner c4 = cube.getCorner(GREEN, WHITE, RED);

        Map<Corner, ArrayList<String>> f2l = FirstTwoLayers.solve(cube);

        sb.append("Pair #1 (Green/Red):\n").append(listToMoves(f2l.get(c1))).append("\n");
        sb.append("Pair #2 (Green/Orange):\n").append(listToMoves(f2l.get(c2))).append("\n");
        sb.append("Pair #3 (Orange/Blue):\n").append(listToMoves(f2l.get(c3))).append("\n");
        sb.append("Pair #4 (Blue/Red):\n").append(listToMoves(f2l.get(c4))).append("\n\n");

        // ===================== OLL =====================
        sb.append("OLL SOLUTION:\n");
        ArrayList<String> oll = OrientLastLayer.solve(cube);
        sb.append(cleanMoves(oll.toString())).append("\n\n");

        // ===================== PLL =====================
        sb.append("PLL SOLUTION:\n");
        ArrayList<String> pll = PermuteLastLayer.solve(cube);
        sb.append(cleanMoves(pll.toString())).append("\n\n");

        return sb.toString();
    }


    private static String invertAlgorithm(String alg) {
        String[] t = alg.trim().split("\\s+");
        StringBuilder out = new StringBuilder();
        for (int i = t.length - 1; i >= 0; i--) {
            String inv = invertMove(t[i]);
            if (!inv.isEmpty()) out.append(inv).append(' ');
        }
        return out.toString().trim();
    }

    private static String invertMove(String m) {
        m = m.trim();
        if (m.isEmpty()) return "";
        if (m.endsWith("2")) return m;
        if (m.endsWith("'")) return m.substring(0, m.length() - 1);
        return m + "'";
    }

    private static String listToMoves(ArrayList<String> moves) {
        if (moves == null || moves.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String s : moves) {
            if (s == null) continue;
            s = s.trim();
            if (!s.isEmpty()) sb.append(s).append(' ');
        }
        return sb.toString().trim();
    }

    private static String cleanMoves(String s) {
        if (s == null) return "";
        s = s.replace("[", "").replace("]", "").replace(",", " ");
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }

    public static String extractMovesFromReport(String report) {
        if (report == null || report.trim().isEmpty()) return "";

        StringBuilder out = new StringBuilder();

        String moveRegex = "(?<![A-Za-z0-9_])[UDLRFB](?:2|')?(?![A-Za-z0-9_])";

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(moveRegex);
        java.util.regex.Matcher m = p.matcher(report);

        while (m.find()) {
            out.append(m.group()).append(' ');
        }

        return out.toString().trim().replaceAll("\\s+", " ");
    }

}
