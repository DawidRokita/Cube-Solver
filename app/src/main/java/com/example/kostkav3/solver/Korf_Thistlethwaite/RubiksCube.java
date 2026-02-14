package com.example.kostkav3.solver.Korf_Thistlethwaite;

import com.example.kostkav3.solver.Korf_Thistlethwaite.korf.CornerConfigurationNode;
import com.example.kostkav3.solver.Korf_Thistlethwaite.korf.EdgeConfigurationNode;
import com.example.kostkav3.solver.Korf_Thistlethwaite.korf.PruningTables;

import java.util.Arrays;
import java.util.HashMap;

public class RubiksCube {
    /*Corners
    Array elements:*/
    final static byte urf = 0;
    final static byte ufl = 1;
    final static byte ulb = 2;
    final static byte ubr = 3;
    final static byte dbl = 4;
    final static byte dlf = 5;
    final static byte dfr = 6;
    final static byte drb = 7;

    //Slots and orientation numbers (corner cubies)
    final static byte WRG = 0;
    final static byte GWR = 1;
    final static byte RGW = 2;
    final static byte WGO = 3;
    final static byte OWG = 4;
    final static byte GOW = 5;
    final static byte WOB = 6;
    final static byte BWO = 7;
    final static byte OBW = 8;
    final static byte WBR = 9;
    final static byte RWB = 10;
    final static byte BRW = 11;
    final static byte YBO = 12;
    final static byte OYB = 13;
    final static byte BOY = 14;
    final static byte YOG = 15;
    final static byte GYO = 16;
    final static byte OGY = 17;
    final static byte YGR = 18;
    final static byte RYG = 19;
    final static byte GRY = 20;
    final static byte YRB = 21;
    final static byte BYR = 22;
    final static byte RBY = 23;

    /*Edges;
        Array elements*/
    final static byte uf = 0;
    final static byte ul = 1;
    final static byte ub = 2;
    final static byte ur = 3;
    final static byte fr = 4;
    final static byte fl = 5;
    final static byte bl = 6;
    final static byte br = 7;
    final static byte db = 8;
    final static byte dl = 9;
    final static byte df = 10;
    final static byte dr = 11;

    //Position and orientation numbers (edge cubies)
    final static byte WG = 0;
    final static byte GW = 1;
    final static byte WO = 2;
    final static byte OW = 3;
    final static byte WB = 4;
    final static byte BW = 5;
    final static byte WR = 6;
    final static byte RW = 7;
    final static byte GR = 8;
    final static byte RG = 9;
    final static byte GO = 10;
    final static byte OG = 11;
    final static byte BO = 12;
    final static byte OB = 13;
    final static byte BR = 14;
    final static byte RB = 15;
    final static byte YB = 16;
    final static byte BY = 17;
    final static byte YO = 18;
    final static byte OY = 19;
    final static byte YG = 20;
    final static byte GY = 21;
    final static byte YR = 22;
    final static byte RY = 23;

    //Moves
    public final static byte I = -1;
    public final static byte R = 0;
    public final static byte R_PRIME = 1;
    public final static byte R2 = 2;
    public final static byte L = 3;
    public final static byte L_PRIME = 4;
    public final static byte L2 = 5;
    public final static byte U = 6;
    public final static byte U_PRIME = 7;
    public final static byte U2 = 8;
    public final static byte D = 9;
    public final static byte D_PRIME = 10;
    public final static byte D2 = 11;
    public final static byte F = 12;
    public final static byte F_PRIME = 13;
    public final static byte F2 = 14;
    public final static byte B = 15;
    public final static byte B_PRIME = 16;
    public final static byte B2 = 17;

    public final static HashMap<Byte, byte[]> allowedMovesMap = new HashMap<>();//Allowed moves
    public final static HashMap<Byte, byte[]> phaseTwoAllowedMovesMap = new HashMap<>();
    public final static HashMap<Byte, byte[]> phaseThreeAllowedMovesMap = new HashMap<>();
    public final static HashMap<Byte, byte[]> phaseFourAllowedMovesMap = new HashMap<>();

    public byte[] corners = new byte[8];
    public byte[] edges = new byte[12];

    public RubiksCube() {
        for (byte i = 0; i < 8; i++) {
            corners[i] = (byte) (i * 3);
        }
        for (byte i = 0; i < 12; i++) {
            edges[i] = (byte) (i * 2);
        }
    }

    public RubiksCube(byte[] corners, byte[] edges) {
        this.corners = corners;
        this.edges = edges;
    }

    public RubiksCube(byte[] state, byte partialState) {
        if (partialState == PruningTables.cornerState) {
            this.corners = state.clone();
        } else if (partialState == PruningTables.firstEdgeState) {
            System.arraycopy(state, 0, this.edges, 0, state.length);
        } else if (partialState == PruningTables.secondEdgeState) {
            System.arraycopy(state, 0, this.edges, 6, state.length);
        }
    }

    public void doAlgorithm(byte[] algorithm) {
        for (int i = 0; i < algorithm.length; i++) {
            doMove(algorithm[i]);
        }
    }

    public void doThistlewaiteAlgorithm(byte[] algorithm) {
        for (int i = 0; i < algorithm.length; i++) {
            doThistlewaiteMove(algorithm[i]);
        }
    }

    public void doMove(byte move) {
        switch (move) {
            case R:
                doGenericCornerMove(urf, ubr, drb, dfr,
                        dfr, urf, ubr, drb,
                        2, 1, 2, 1);
                doGenericEdgeMove(ur, br, dr, fr,
                        fr, ur, br, dr,
                        0);
                break;
            case R_PRIME:
                doGenericCornerMove(urf, ubr, drb, dfr,
                        ubr, drb, dfr, urf,
                        2, 1, 2, 1);
                doGenericEdgeMove(ur, br, dr, fr,
                        br, dr, fr, ur,
                        0);
                break;
            case R2:
                doGenericCornerMove(urf, ubr, drb, dfr,
                        drb, dfr, urf, ubr,
                        0, 0, 0, 0);
                doGenericEdgeMove(ur, br, dr, fr,
                        dr, fr, ur, br,
                        0);
                break;
            case L:
                doGenericCornerMove(ufl, dlf, dbl, ulb,
                        ulb, ufl, dlf, dbl,
                        1, 2, 1, 2);
                doGenericEdgeMove(ul, fl, dl, bl,
                        bl, ul, fl, dl,
                        0);
                break;
            case L_PRIME:
                doGenericCornerMove(ufl, dlf, dbl, ulb,
                        dlf, dbl, ulb, ufl,
                        1, 2, 1, 2);
                doGenericEdgeMove(ul, fl, dl, bl,
                        fl, dl, bl, ul,
                        0);
                break;
            case L2:
                doGenericCornerMove(ufl, dlf, dbl, ulb,
                        dbl, ulb, ufl, dlf,
                        0, 0, 0, 0);
                doGenericEdgeMove(ul, fl, dl, bl,
                        dl, bl, ul, fl,
                        0);
                break;
            case U:
                doGenericCornerMove(urf, ufl, ulb, ubr,
                        ubr, urf, ufl, ulb,
                        0, 0, 0, 0);
                doGenericEdgeMove(uf, ul, ub, ur,
                        ur, uf, ul, ub,
                        0);
                break;
            case U_PRIME:
                doGenericCornerMove(urf, ufl, ulb, ubr,
                        ufl, ulb, ubr, urf,
                        0, 0, 0, 0);
                doGenericEdgeMove(uf, ul, ub, ur,
                        ul, ub, ur, uf,
                        0);
                break;
            case U2:
                doGenericCornerMove(urf, ufl, ulb, ubr,
                        ulb, ubr, urf, ufl,
                        0, 0, 0, 0);
                doGenericEdgeMove(uf, ul, ub, ur,
                        ub, ur, uf, ul,
                        0);
                break;
            case D:
                doGenericCornerMove(dbl, dlf, dfr, drb,
                        drb, dbl, dlf, dfr,
                        0, 0, 0, 0);
                doGenericEdgeMove(db, dl, df, dr,
                        dr, db, dl, df,
                        0);
                break;
            case D_PRIME:
                doGenericCornerMove(dbl, dlf, dfr, drb,
                        dlf, dfr, drb, dbl,
                        0, 0, 0, 0);
                doGenericEdgeMove(db, dl, df, dr,
                        dl, df, dr, db,
                        0);
                break;
            case D2:
                doGenericCornerMove(dbl, dlf, dfr, drb,
                        dfr, drb, dbl, dlf,
                        0, 0, 0, 0);
                doGenericEdgeMove(db, dl, df, dr,
                        df, dr, db, dl,
                        0);
                break;
            case F:
                doGenericCornerMove(urf, dfr, dlf, ufl,
                        ufl, urf, dfr, dlf,
                        1, 2, 1, 2);
                doGenericEdgeMove(uf, fr, df, fl,
                        fl, uf, fr, df,
                        1);
                break;
            case F_PRIME:
                doGenericCornerMove(urf, dfr, dlf, ufl,
                        dfr, dlf, ufl, urf,
                        1, 2, 1, 2);
                doGenericEdgeMove(uf, fr, df, fl,
                        fr, df, fl, uf,
                        1);
                break;
            case F2:
                doGenericCornerMove(urf, dfr, dlf, ufl,
                        dlf, ufl, urf, dfr,
                        0, 0, 0, 0);
                doGenericEdgeMove(uf, fr, df, fl,
                        df, fl, uf, fr,
                        0);
                break;
            case B:
                doGenericCornerMove(dbl, drb, ubr, ulb,
                        ulb, dbl, drb, ubr,
                        2, 1, 2, 1);
                doGenericEdgeMove(db, br, ub, bl,
                        bl, db, br, ub,
                        1);
                break;
            case B_PRIME:
                doGenericCornerMove(dbl, drb, ubr, ulb,
                        drb, ubr, ulb, dbl,
                        2, 1, 2, 1);
                doGenericEdgeMove(db, br, ub, bl,
                        br, ub, bl, db,
                        1);
                break;
            case B2:
                doGenericCornerMove(dbl, drb, ubr, ulb,
                        ubr, ulb, dbl, drb,
                        0, 0, 0, 0);
                doGenericEdgeMove(db, br, ub, bl,
                        ub, bl, db, br,
                        0);
                break;
            default:
                // System.out.println("Wrong move");
                break;
        }
    }

    public void doThistlewaiteMove(byte move) {
        switch (move) {
            case R:
                doGenericCornerMove(urf, ubr, drb, dfr,
                        dfr, urf, ubr, drb,
                        0, 0, 0, 0);
                doGenericEdgeMove(ur, br, dr, fr,
                        fr, ur, br, dr,
                        0);
                break;
            case R_PRIME:
                doGenericCornerMove(urf, ubr, drb, dfr,
                        ubr, drb, dfr, urf,
                        0, 0, 0, 0);
                doGenericEdgeMove(ur, br, dr, fr,
                        br, dr, fr, ur,
                        0);
                break;
            case R2:
                doGenericCornerMove(urf, ubr, drb, dfr,
                        drb, dfr, urf, ubr,
                        0, 0, 0, 0);
                doGenericEdgeMove(ur, br, dr, fr,
                        dr, fr, ur, br,
                        0);
                break;
            case L:
                doGenericCornerMove(ufl, dlf, dbl, ulb,
                        ulb, ufl, dlf, dbl,
                        0, 0, 0, 0);
                doGenericEdgeMove(ul, fl, dl, bl,
                        bl, ul, fl, dl,
                        0);
                break;
            case L_PRIME:
                doGenericCornerMove(ufl, dlf, dbl, ulb,
                        dlf, dbl, ulb, ufl,
                        0, 0, 0, 0);
                doGenericEdgeMove(ul, fl, dl, bl,
                        fl, dl, bl, ul,
                        0);
                break;
            case L2:
                doGenericCornerMove(ufl, dlf, dbl, ulb,
                        dbl, ulb, ufl, dlf,
                        0, 0, 0, 0);
                doGenericEdgeMove(ul, fl, dl, bl,
                        dl, bl, ul, fl,
                        0);
                break;
            case U:
                doGenericCornerMove(urf, ufl, ulb, ubr,
                        ubr, urf, ufl, ulb,
                        1, 2, 1, 2);
                doGenericEdgeMove(uf, ul, ub, ur,
                        ur, uf, ul, ub,
                        1);
                break;
            case U_PRIME:
                doGenericCornerMove(urf, ufl, ulb, ubr,
                        ufl, ulb, ubr, urf,
                        1, 2, 1, 2);
                doGenericEdgeMove(uf, ul, ub, ur,
                        ul, ub, ur, uf,
                        1);
                break;
            case U2:
                doGenericCornerMove(urf, ufl, ulb, ubr,
                        ulb, ubr, urf, ufl,
                        0, 0, 0, 0);
                doGenericEdgeMove(uf, ul, ub, ur,
                        ub, ur, uf, ul,
                        0);
                break;
            case D:
                doGenericCornerMove(dbl, dlf, dfr, drb,
                        drb, dbl, dlf, dfr,
                        2, 1, 2, 1);
                doGenericEdgeMove(db, dl, df, dr,
                        dr, db, dl, df,
                        1);
                break;
            case D_PRIME:
                doGenericCornerMove(dbl, dlf, dfr, drb,
                        dlf, dfr, drb, dbl,
                        2, 1, 2, 1);
                doGenericEdgeMove(db, dl, df, dr,
                        dl, df, dr, db,
                        1);
                break;
            case D2:
                doGenericCornerMove(dbl, dlf, dfr, drb,
                        dfr, drb, dbl, dlf,
                        0, 0, 0, 0);
                doGenericEdgeMove(db, dl, df, dr,
                        df, dr, db, dl,
                        0);
                break;
            case F:
                doGenericCornerMove(urf, dfr, dlf, ufl,
                        ufl, urf, dfr, dlf,
                        2, 1, 2, 1);
                doGenericEdgeMove(uf, fr, df, fl,
                        fl, uf, fr, df,
                        0);
                break;
            case F_PRIME:
                doGenericCornerMove(urf, dfr, dlf, ufl,
                        dfr, dlf, ufl, urf,
                        2, 1, 2, 1);
                doGenericEdgeMove(uf, fr, df, fl,
                        fr, df, fl, uf,
                        0);
                break;
            case F2:
                doGenericCornerMove(urf, dfr, dlf, ufl,
                        dlf, ufl, urf, dfr,
                        0, 0, 0, 0);
                doGenericEdgeMove(uf, fr, df, fl,
                        df, fl, uf, fr,
                        0);
                break;
            case B:
                doGenericCornerMove(dbl, drb, ubr, ulb,
                        ulb, dbl, drb, ubr,
                        1, 2, 1, 2);
                doGenericEdgeMove(db, br, ub, bl,
                        bl, db, br, ub,
                        0);
                break;
            case B_PRIME:
                doGenericCornerMove(dbl, drb, ubr, ulb,
                        drb, ubr, ulb, dbl,
                        1, 2, 1, 2);
                doGenericEdgeMove(db, br, ub, bl,
                        br, ub, bl, db,
                        0);
                break;
            case B2:
                doGenericCornerMove(dbl, drb, ubr, ulb,
                        ubr, ulb, dbl, drb,
                        0, 0, 0, 0);
                doGenericEdgeMove(db, br, ub, bl,
                        ub, bl, db, br,
                        0);
                break;
            case I:
                break;
            default:
                // System.out.println("Wrong move");
                break;
        }
    }

    public void doGenericCornerMove(byte d1, byte d2, byte d3, byte d4,
                                    byte s1, byte s2, byte s3, byte s4,
                                    int o1, int o2, int o3, int o4) {
        byte[] oldCorners = corners.clone();
        corners[d1] = (byte) ((oldCorners[s1] - oldCorners[s1] % 3) + ((oldCorners[s1] % 3 + o1) % 3));
        corners[d2] = (byte) ((oldCorners[s2] - oldCorners[s2] % 3) + ((oldCorners[s2] % 3 + o2) % 3));
        corners[d3] = (byte) ((oldCorners[s3] - oldCorners[s3] % 3) + ((oldCorners[s3] % 3 + o3) % 3));
        corners[d4] = (byte) ((oldCorners[s4] - oldCorners[s4] % 3) + ((oldCorners[s4] % 3 + o4) % 3));
    }

    public void doGenericEdgeMove(byte d1, byte d2, byte d3, byte d4,
                                  byte s1, byte s2, byte s3, byte s4,
                                  int o /* 0 or 1*/) {
        byte[] oldEdges = edges.clone();
        edges[d1] = (byte) ((oldEdges[s1] - oldEdges[s1] % 2) + ((oldEdges[s1] % 2 + o) % 2));
        edges[d2] = (byte) ((oldEdges[s2] - oldEdges[s2] % 2) + ((oldEdges[s2] % 2 + o) % 2));
        edges[d3] = (byte) ((oldEdges[s3] - oldEdges[s3] % 2) + ((oldEdges[s3] % 2 + o) % 2));
        edges[d4] = (byte) ((oldEdges[s4] - oldEdges[s4] % 2) + ((oldEdges[s4] % 2 + o) % 2));
    }

    public int getCornerOrientationHash() {
        int hash = 0;
        for (byte i = 0; i < corners.length - 1; i++) {
            byte orientation = (byte) (corners[i] % 3);
            hash += orientation * Math.pow(3, 6 - i);
        }
        return hash;
    }

    public int getCornerPermutationHash() {
        int hash = 0;
        for (byte i = 0; i < corners.length; i++) {
            byte numGreater = 0;
            int piece = (corners[i] / 3);
            for (byte j = 0; j < i; j++) {
                if ((corners[j] / 3) > piece) {
                    numGreater++;
                }
            }
            hash += numGreater * CornerConfigurationNode.factorial(i);
        }
        return hash;
    }

    public int getFirstEdgeOrientationHash() {
        int hash = 0;
        for (int i = 0; i < 6; i++) {
            int orientation = (byte) (edges[i] % 2);
            hash += orientation * Math.pow(2, 5 - i);
        }
        return hash;
    }

    public int getSecondEdgeOrientationHash() {
        int hash = 0;
        for (int i = 6; i < 12; i++) {
            int orientation = (byte) (edges[i] % 2);
            hash += orientation * Math.pow(2, 11 - i);
        }
        return hash;
    }

    public int getFirstEdgePermutationHash() {
        byte[] array = Arrays.copyOfRange(this.edges, 0, 6);
        byte[] permutations = new byte[array.length];
        for (int i = 0; i < permutations.length; i++) {
            permutations[i] = (byte) (array[i] / 2);
        }
        return EdgeConfigurationNode.permutationHashes.get(new ByteArrayWrapper(permutations));
    }

    public int getSecondEdgePermutationHash() {
        byte[] array = Arrays.copyOfRange(this.edges, 6, 12);
        byte[] permutations = new byte[array.length];
        for (int i = 0; i < permutations.length; i++) {
            permutations[i] = (byte) (array[i] / 2);
        }
        return EdgeConfigurationNode.permutationHashes.get(new ByteArrayWrapper(permutations));
    }

    public boolean isSolved() {
        for (int i = 0; i < corners.length; i++) {
            if (corners[i] != (byte) (i * 3)) return false;
        }
        for (int j = 0; j < edges.length; j++) {
            if (edges[j] != (byte) (j * 2)) return false;
        }
        return true;
    }

    public byte getHeuristic() {
        PruningTables tables = PruningTables.getSingletonInstance();
        byte cornerHeuristic = tables.getCornerHeuristic(this);
        byte firstEdgeHeuristic = tables.getFirstEdgeHeuristic(this);
        byte secondEdgeHeuristic = tables.getSecondEdgeHeuristic(this);
        return (byte) Math.max(Math.max(cornerHeuristic, firstEdgeHeuristic), secondEdgeHeuristic);
    }

    @Override
    public RubiksCube clone() {
        RubiksCube rubiksCube = new RubiksCube();
        rubiksCube.corners = this.corners.clone();
        rubiksCube.edges = this.edges.clone();
        return rubiksCube;
    }

    @Override
    public String toString() {
        return "RubiksCube{" +
                "corners=" + Arrays.toString(corners) +
                ", edges=" + Arrays.toString(edges) +
                '}';
    }

    public static void initializeAllowedMovesMaps() {
        allowedMovesMap.put(R, new byte[]{L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(R_PRIME, new byte[]{L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(R2, new byte[]{L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        allowedMovesMap.put(L, new byte[]{U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(L_PRIME, new byte[]{U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(L2, new byte[]{U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        allowedMovesMap.put(U, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(U_PRIME, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(U2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        allowedMovesMap.put(D, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(D_PRIME, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, F, F_PRIME, F2, B, B_PRIME, B2});
        allowedMovesMap.put(D2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, F, F_PRIME, F2, B, B_PRIME, B2});

        allowedMovesMap.put(F, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, B, B_PRIME, B2});
        allowedMovesMap.put(F_PRIME, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, B, B_PRIME, B2});
        allowedMovesMap.put(F2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, B, B_PRIME, B2});

        allowedMovesMap.put(B, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2});
        allowedMovesMap.put(B_PRIME, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2});
        allowedMovesMap.put(B2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2});

        allowedMovesMap.put(I, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U, U_PRIME, U2, D, D_PRIME, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        phaseTwoAllowedMovesMap.put(R, new byte[]{L, L_PRIME, L2, U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        phaseTwoAllowedMovesMap.put(R_PRIME, new byte[]{L, L_PRIME, L2, U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        phaseTwoAllowedMovesMap.put(R2, new byte[]{L, L_PRIME, L2, U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        phaseTwoAllowedMovesMap.put(L, new byte[]{U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        phaseTwoAllowedMovesMap.put(L_PRIME, new byte[]{U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});
        phaseTwoAllowedMovesMap.put(L2, new byte[]{U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        phaseTwoAllowedMovesMap.put(U2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        phaseTwoAllowedMovesMap.put(D2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, F, F_PRIME, F2, B, B_PRIME, B2});

        phaseTwoAllowedMovesMap.put(F, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2, B, B_PRIME, B2});
        phaseTwoAllowedMovesMap.put(F_PRIME, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2, B, B_PRIME, B2});
        phaseTwoAllowedMovesMap.put(F2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2, B, B_PRIME, B2});

        phaseTwoAllowedMovesMap.put(B, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2});
        phaseTwoAllowedMovesMap.put(B_PRIME, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2});
        phaseTwoAllowedMovesMap.put(B2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2});

        phaseTwoAllowedMovesMap.put(I, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2, F, F_PRIME, F2, B, B_PRIME, B2});

        phaseThreeAllowedMovesMap.put(R, new byte[]{L, L_PRIME, L2, U2, D2, F2, B2});
        phaseThreeAllowedMovesMap.put(R_PRIME, new byte[]{L, L_PRIME, L2, U2, D2, F2, B2});
        phaseThreeAllowedMovesMap.put(R2, new byte[]{L, L_PRIME, L2, U2, D2, F2, B2});

        phaseThreeAllowedMovesMap.put(L, new byte[]{U2, D2, F2, B2});
        phaseThreeAllowedMovesMap.put(L_PRIME, new byte[]{U2, D2, F2, B2});
        phaseThreeAllowedMovesMap.put(L2, new byte[]{U2, D2, F2, B2});

        phaseThreeAllowedMovesMap.put(U2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, D2, F2, B2});

        phaseThreeAllowedMovesMap.put(D2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, F2, B2});

        phaseThreeAllowedMovesMap.put(F2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2, B2});

        phaseThreeAllowedMovesMap.put(B2, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2});

        phaseThreeAllowedMovesMap.put(I, new byte[]{R, R_PRIME, R2, L, L_PRIME, L2, U2, D2, F2, B2});

        phaseFourAllowedMovesMap.put(R2, new byte[]{L2, U2, D2, F2, B2});
        phaseFourAllowedMovesMap.put(L2, new byte[]{U2, D2, F2, B2});
        phaseFourAllowedMovesMap.put(U2, new byte[]{R2, L2, D2, F2, B2});
        phaseFourAllowedMovesMap.put(D2, new byte[]{R2, L2, F2, B2});
        phaseFourAllowedMovesMap.put(F2, new byte[]{R2, L2, U2, D2, B2});
        phaseFourAllowedMovesMap.put(B2, new byte[]{R2, L2, U2, D2});
        phaseFourAllowedMovesMap.put(I, new byte[]{R2, L2, U2, D2, F2, B2});
    }

    public static String getMoveString(byte move) {
        switch (move) {
            case R: return "R";
            case R_PRIME: return "R'";
            case R2: return "R2";
            case L: return "L";
            case L_PRIME: return "L'";
            case L2: return "L2";
            case U: return "U";
            case U_PRIME: return "U'";
            case U2: return "U2";
            case D: return "D";
            case D_PRIME: return "D'";
            case D2: return "D2";
            case F: return "F";
            case F_PRIME: return "F'";
            case F2: return "F2";
            case B: return "B";
            case B_PRIME: return "B'";
            case B2: return "B2";
            case I: return "";
            default: return "Wrong move";
        }
    }

    // -------------------------------------------------------------------------
    // Facelets (URFDLB) -> Cubies (corners/edges)
    // -------------------------------------------------------------------------

    private static char mapFaceToColor(char f) {
        // Twoja app: U R F D L B
        // Solver: W R G Y O B (standardowa kolorystyka)
        switch (f) {
            case 'U': return 'W';
            case 'R': return 'R';
            case 'F': return 'G';
            case 'D': return 'Y';
            case 'L': return 'O';
            case 'B': return 'B';
            default: throw new IllegalArgumentException("Nieznany facelet: " + f);
        }
    }

    private static char col(String s, int idx) {
        return mapFaceToColor(s.charAt(idx));
    }

    private static int idxU(int pos) { return pos; }          // 0..8
    private static int idxR(int pos) { return 9 + pos; }      // 9..17
    private static int idxF(int pos) { return 18 + pos; }     // 18..26
    private static int idxD(int pos) { return 27 + pos; }     // 27..35
    private static int idxL(int pos) { return 36 + pos; }     // 36..44
    private static int idxB(int pos) { return 45 + pos; }     // 45..53

    private static byte cornerCode(char a, char b, char c) {
        String key = "" + a + b + c;
        switch (key) {
            case "WRG": return WRG; case "GWR": return GWR; case "RGW": return RGW;
            case "WGO": return WGO; case "OWG": return OWG; case "GOW": return GOW;
            case "WOB": return WOB; case "BWO": return BWO; case "OBW": return OBW;
            case "WBR": return WBR; case "RWB": return RWB; case "BRW": return BRW;
            case "YBO": return YBO; case "OYB": return OYB; case "BOY": return BOY;
            case "YOG": return YOG; case "GYO": return GYO; case "OGY": return OGY;
            case "YGR": return YGR; case "RYG": return RYG; case "GRY": return GRY;
            case "YRB": return YRB; case "BYR": return BYR; case "RBY": return RBY;
            default:
                throw new IllegalArgumentException("Nieznany corner cubie: " + key);
        }
    }

    private static byte edgeCode(char a, char b) {
        String key = "" + a + b;
        switch (key) {
            case "WG": return WG; case "GW": return GW;
            case "WO": return WO; case "OW": return OW;
            case "WB": return WB; case "BW": return BW;
            case "WR": return WR; case "RW": return RW;
            case "GR": return GR; case "RG": return RG;
            case "GO": return GO; case "OG": return OG;
            case "BO": return BO; case "OB": return OB;
            case "BR": return BR; case "RB": return RB;
            case "YB": return YB; case "BY": return BY;
            case "YO": return YO; case "OY": return OY;
            case "YG": return YG; case "GY": return GY;
            case "YR": return YR; case "RY": return RY;
            default:
                throw new IllegalArgumentException("Nieznany edge cubie: " + key);
        }
    }

    /**
     * Tworzy RubiksCube ze stringa 54 znaków w kolejności URFDLB.
     * Zakłada standardowy układ kolorów: U=W, R=R, F=G, D=Y, L=O, B=B.
     * Każda ściana ma 9 faceletów zapisanych wierszami (0..8).
     */
    public static RubiksCube fromURFDLBFacelets(String facelets54) {
        if (facelets54 == null || facelets54.length() != 54) {
            throw new IllegalArgumentException("Stan musi mieć 54 znaki, ma: " +
                    (facelets54 == null ? "null" : facelets54.length()));
        }

        byte[] corners = new byte[8];
        byte[] edges = new byte[12];

        // --- Corners: urf, ufl, ulb, ubr, dbl, dlf, dfr, drb ---
        // URF: U9, R1, F3
        corners[urf] = cornerCode(col(facelets54, idxU(8)), col(facelets54, idxR(0)), col(facelets54, idxF(2)));
        // UFL: U7, F1, L3
        corners[ufl] = cornerCode(col(facelets54, idxU(6)), col(facelets54, idxF(0)), col(facelets54, idxL(2)));
        // ULB: U1, L1, B3
        corners[ulb] = cornerCode(col(facelets54, idxU(0)), col(facelets54, idxL(0)), col(facelets54, idxB(2)));
        // UBR: U3, B1, R3
        corners[ubr] = cornerCode(col(facelets54, idxU(2)), col(facelets54, idxB(0)), col(facelets54, idxR(2)));

        // DBL: D7, B9, L7
        corners[dbl] = cornerCode(col(facelets54, idxD(6)), col(facelets54, idxB(8)), col(facelets54, idxL(6)));
        // DLF: D1, L9, F7
        corners[dlf] = cornerCode(col(facelets54, idxD(0)), col(facelets54, idxL(8)), col(facelets54, idxF(6)));
        // DFR: D3, F9, R7
        corners[dfr] = cornerCode(col(facelets54, idxD(2)), col(facelets54, idxF(8)), col(facelets54, idxR(6)));
        // DRB: D9, R9, B7
        corners[drb] = cornerCode(col(facelets54, idxD(8)), col(facelets54, idxR(8)), col(facelets54, idxB(6)));

        // --- Edges: uf, ul, ub, ur, fr, fl, bl, br, db, dl, df, dr ---
        // UF: U8, F2
        edges[uf] = edgeCode(col(facelets54, idxU(7)), col(facelets54, idxF(1)));
        // UL: U4, L2
        edges[ul] = edgeCode(col(facelets54, idxU(3)), col(facelets54, idxL(1)));
        // UB: U2, B2
        edges[ub] = edgeCode(col(facelets54, idxU(1)), col(facelets54, idxB(1)));
        // UR: U6, R2
        edges[ur] = edgeCode(col(facelets54, idxU(5)), col(facelets54, idxR(1)));

        // FR: F6, R4
        edges[fr] = edgeCode(col(facelets54, idxF(5)), col(facelets54, idxR(3)));
        // FL: F4, L6
        edges[fl] = edgeCode(col(facelets54, idxF(3)), col(facelets54, idxL(5)));
        // BL: B6, L4
        edges[bl] = edgeCode(col(facelets54, idxB(5)), col(facelets54, idxL(3)));
        // BR: B4, R6
        edges[br] = edgeCode(col(facelets54, idxB(3)), col(facelets54, idxR(5)));

        // DB: D8, B8
        edges[db] = edgeCode(col(facelets54, idxD(7)), col(facelets54, idxB(7)));
        // DL: D4, L8
        edges[dl] = edgeCode(col(facelets54, idxD(3)), col(facelets54, idxL(7)));
        // DF: D2, F8
        edges[df] = edgeCode(col(facelets54, idxD(1)), col(facelets54, idxF(7)));
        // DR: D6, R8
        edges[dr] = edgeCode(col(facelets54, idxD(5)), col(facelets54, idxR(7)));

        return new RubiksCube(corners, edges);
    }

    public static byte inverseMove(byte move) {
        switch (move) {
            case R: return R_PRIME;
            case R_PRIME: return R;
            case R2: return R2;

            case L: return L_PRIME;
            case L_PRIME: return L;
            case L2: return L2;

            case U: return U_PRIME;
            case U_PRIME: return U;
            case U2: return U2;

            case D: return D_PRIME;
            case D_PRIME: return D;
            case D2: return D2;

            case F: return F_PRIME;
            case F_PRIME: return F;
            case F2: return F2;

            case B: return B_PRIME;
            case B_PRIME: return B;
            case B2: return B2;

            case I: return I;
            default: return I;
        }
    }

    public boolean isValidState() {
        // 1) corner orientation
        int cornerOriSum = 0;
        for (int i = 0; i < 8; i++) cornerOriSum += (corners[i] % 3);
        if (cornerOriSum % 3 != 0) return false;

        // 2) edge flip
        int edgeFlipSum = 0;
        for (int i = 0; i < 12; i++) edgeFlipSum += (edges[i] % 2);
        if (edgeFlipSum % 2 != 0) return false;

        // 3) parity corners vs edges
        int cornerParity = permutationParityCorners();
        int edgeParity = permutationParityEdges();
        return (cornerParity == edgeParity);
    }

    private int permutationParityCorners() {
        int inversions = 0;
        for (int i = 0; i < 8; i++) {
            int pi = corners[i] / 3;
            for (int j = i + 1; j < 8; j++) {
                int pj = corners[j] / 3;
                if (pi > pj) inversions++;
            }
        }
        return inversions % 2;
    }

    private int permutationParityEdges() {
        int inversions = 0;
        for (int i = 0; i < 12; i++) {
            int pi = edges[i] / 2;
            for (int j = i + 1; j < 12; j++) {
                int pj = edges[j] / 2;
                if (pi > pj) inversions++;
            }
        }
        return inversions % 2;
    }


    public static boolean isInverse(byte a, byte b) {
        if (a == I || b == I) return false;
        return inverseMove(a) == b;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RubiksCube)) return false;
        RubiksCube that = (RubiksCube) o;
        return Arrays.equals(this.corners, that.corners) && Arrays.equals(this.edges, that.edges);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(corners);
        result = 31 * result + Arrays.hashCode(edges);
        return result;
    }
}
