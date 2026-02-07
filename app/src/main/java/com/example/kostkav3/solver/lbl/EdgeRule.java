package com.example.kostkav3.solver.lbl;

public class EdgeRule {
    private char sideType;
    private int sideNo;
    private boolean reverseOrder;

    public EdgeRule(char sideType, int sideNo, boolean reverseOrder) {
        if (sideType != 'C' && sideType != 'R') {
            throw new IllegalArgumentException("sideType must be either 'C' or 'R'.");
        }

        this.sideType = sideType;
        this.sideNo = sideNo;
        this.reverseOrder = reverseOrder;
    }

    public char getSideType() { return sideType; }
    public int getSideNo() { return sideNo; }
    public boolean getReverseOrder() { return reverseOrder; }
}
