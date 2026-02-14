package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import java.util.Arrays;
import java.util.Objects;

public class PhaseThreeKey {
    byte[] corners;
    byte[] edges;
    boolean isEvenParity;

    public PhaseThreeKey(byte[] corners,byte[] edges,boolean isEvenParity){
        this.corners=corners;
        this.edges=edges;
        this.isEvenParity=isEvenParity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhaseThreeKey that = (PhaseThreeKey) o;
        return isEvenParity == that.isEvenParity &&
                Arrays.equals(corners, that.corners) &&
                Arrays.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(isEvenParity);
        result = 31 * result + Arrays.hashCode(corners);
        result = 31 * result + Arrays.hashCode(edges);
        return result;
    }
}
