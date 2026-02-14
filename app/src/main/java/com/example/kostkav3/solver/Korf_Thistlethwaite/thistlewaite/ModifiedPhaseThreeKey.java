package com.example.kostkav3.solver.Korf_Thistlethwaite.thistlewaite;

import java.util.Arrays;

public class ModifiedPhaseThreeKey {
    byte[] corners;
    byte[] edges;

    public ModifiedPhaseThreeKey(byte[] corners, byte[] edges){
        this.corners=corners;
        this.edges=edges;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedPhaseThreeKey that = (ModifiedPhaseThreeKey) o;
        return Arrays.equals(corners, that.corners) &&
                Arrays.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(corners);
        result = 31 * result + Arrays.hashCode(edges);
        return result;
    }
}
