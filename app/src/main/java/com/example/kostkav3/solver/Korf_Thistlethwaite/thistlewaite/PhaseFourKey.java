package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import java.util.Arrays;

public class PhaseFourKey {
    byte[] corners;
    byte[] edges;
    public PhaseFourKey(byte[] corners,byte[] edges) {
        this.corners = corners;
        this.edges = edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhaseFourKey that = (PhaseFourKey) o;
        return Arrays.equals(edges, that.edges) &&
                Arrays.equals(corners, that.corners);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(edges);
        result = 31 * result + Arrays.hashCode(corners);
        return result;
    }
}
