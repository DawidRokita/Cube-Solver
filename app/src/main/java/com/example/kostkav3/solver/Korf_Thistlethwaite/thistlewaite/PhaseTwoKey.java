package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import java.util.Arrays;

public class PhaseTwoKey {
    byte[] cornerOrientations;
    boolean[] middleEdgePositions;

    public PhaseTwoKey(byte[] cornerOrientations,boolean[] middleEdgePositions){
        this.cornerOrientations=cornerOrientations;
        this.middleEdgePositions=middleEdgePositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhaseTwoKey that = (PhaseTwoKey) o;
        return Arrays.equals(cornerOrientations, that.cornerOrientations) &&
                Arrays.equals(middleEdgePositions, that.middleEdgePositions);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(cornerOrientations);
        result = 31 * result + Arrays.hashCode(middleEdgePositions);
        return result;
    }

    @Override
    public String toString() {
        return "cube.solver.thistlewaite.PhaseTwoKey{" +
                "cornerOrientations=" + Arrays.toString(cornerOrientations) +
                ", middleEdgePositions=" + Arrays.toString(middleEdgePositions) +
                '}';
    }
}
