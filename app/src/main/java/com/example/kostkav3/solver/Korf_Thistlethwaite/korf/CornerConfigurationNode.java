package com.example.kostkav3.solver.Korf_Thistlethwaite.korf;

import com.example.kostkav3.solver.Korf_Thistlethwaite.RubiksCube;

import java.util.Arrays;

public class CornerConfigurationNode {
    final static byte urf=0;
    final static byte ufl=1;
    final static byte ulb=2;
    final static byte ubr=3;
    final static byte dbl=4;
    final static byte dlf=5;
    final static byte dfr=6;
    final static byte drb=7;
    
    byte[] cornerConfiguration;
    byte previousMove;
    byte distance;

    public CornerConfigurationNode(){

        this.cornerConfiguration=new byte[]{0,3,6,9,12,15,18,21};
        this.previousMove= RubiksCube.I;
        this.distance=0;

    }

    public CornerConfigurationNode doMove(byte move) {
        CornerConfigurationNode node=this.clone();
        switch (move) {
            case RubiksCube.R:
                node.doGenericCornerMove(urf, ubr, drb, dfr,
                        dfr, urf, ubr, drb,
                        2, 1, 2, 1);
                
                break;
            case RubiksCube.R_PRIME:
                node.doGenericCornerMove(urf, ubr, drb, dfr,
                        ubr, drb, dfr, urf,
                        2, 1, 2, 1);
                
                break;
            case RubiksCube.R2:
                node.doGenericCornerMove(urf, ubr, drb, dfr,
                        drb, dfr, urf, ubr,
                        0, 0, 0, 0);
               
                break;
            case RubiksCube.L:
                node.doGenericCornerMove(ufl, dlf, dbl, ulb,
                        ulb, ufl, dlf, dbl,
                        1, 2, 1, 2
                );
                
                break;
            case RubiksCube.L_PRIME:
                node.doGenericCornerMove(ufl, dlf, dbl, ulb,
                        dlf, dbl, ulb, ufl,
                        1, 2, 1, 2);
                
                break;
            case RubiksCube.L2:
                node.doGenericCornerMove(ufl, dlf, dbl, ulb,
                        dbl, ulb, ufl, dlf,
                        0, 0, 0, 0);
               
                break;
            case RubiksCube.U:
                node.doGenericCornerMove(urf, ufl, ulb, ubr,
                        ubr, urf, ufl, ulb,
                        0, 0, 0, 0);
                
                break;
            case RubiksCube.U_PRIME:
                node.doGenericCornerMove(urf, ufl, ulb, ubr,
                        ufl, ulb, ubr, urf,
                        0, 0, 0, 0);
                
                break;
            case RubiksCube.U2:
                node.doGenericCornerMove(urf, ufl, ulb, ubr,
                        ulb, ubr, urf, ufl,
                        0, 0, 0, 0);
                
                break;
            case RubiksCube.D:
                node.doGenericCornerMove(dbl, dlf, dfr, drb,
                        drb, dbl, dlf, dfr,
                        0, 0, 0, 0);
                
                break;
            case RubiksCube.D_PRIME:
                node.doGenericCornerMove(dbl, dlf, dfr, drb,
                        dlf, dfr, drb, dbl,
                        0, 0, 0, 0);
               
                break;
            case RubiksCube.D2:
                node.doGenericCornerMove(dbl, dlf, dfr, drb,
                        dfr, drb, dbl, dlf,
                        0, 0, 0, 0);
                
                break;
            case RubiksCube.F:
                node.doGenericCornerMove(urf, dfr, dlf, ufl,
                        ufl, urf, dfr, dlf,
                        1, 2, 1, 2);
               
                break;
            case RubiksCube.F_PRIME:
                node.doGenericCornerMove(urf, dfr, dlf, ufl,
                        dfr, dlf, ufl, urf,
                        1, 2, 1, 2);
                
                break;
            case RubiksCube.F2:
                node.doGenericCornerMove(urf, dfr, dlf, ufl,
                        dlf, ufl, urf, dfr,
                        0, 0, 0, 0);
                
                break;
            case RubiksCube.B:
                node.doGenericCornerMove(dbl, drb, ubr, ulb,
                        ulb, dbl, drb, ubr,
                        2, 1, 2, 1);
                
                break;
            case RubiksCube.B_PRIME:
                node.doGenericCornerMove(dbl, drb, ubr, ulb,
                        drb, ubr, ulb, dbl,
                        2, 1, 2, 1);
                
                break;
            case RubiksCube.B2:
                node.doGenericCornerMove(dbl, drb, ubr, ulb,
                        ubr, ulb, dbl, drb,
                        0, 0, 0, 0);
                
                break;
            default:
                System.out.println("Wrong move");
                break;

        }
        node.previousMove=move;
        node.distance=(byte)(this.distance+1);
        
        return node;
    }
    public void doGenericCornerMove(byte d1, byte d2, byte d3, byte d4,
                                    byte s1 , byte s2,byte s3,byte s4,
                                    int o1, int o2, int o3, int o4){
        byte[] oldCorners =cornerConfiguration.clone();
        cornerConfiguration[d1]=(byte)((oldCorners[s1]-oldCorners[s1]%3)+((oldCorners[s1]%3+o1)%3));
        cornerConfiguration[d2]=(byte)((oldCorners[s2]-oldCorners[s2]%3)+((oldCorners[s2]%3+o2)%3));
        cornerConfiguration[d3]=(byte)((oldCorners[s3]-oldCorners[s3]%3)+((oldCorners[s3]%3+o3)%3));
        cornerConfiguration[d4]=(byte)((oldCorners[s4]-oldCorners[s4]%3)+((oldCorners[s4]%3+o4)%3));


    }

    @Override
    public String toString() {
        return "cube.solver.korf.CornerConfigurationNode{" +
                "cornerConfiguration=" + Arrays.toString(cornerConfiguration) +
                ", previousMove=" + previousMove +
                ", distance=" + distance +
                '}';
    }
    public CornerConfigurationNode clone(){
        CornerConfigurationNode node=new CornerConfigurationNode();
        node.cornerConfiguration=this.cornerConfiguration.clone();
        node.distance=this.distance;
        node.previousMove=this.previousMove;
        return node;
    }
    public int getOrientationHash(){
        int hash=0;
        for(byte i=0;i<cornerConfiguration.length-1;i++){
            byte orientation=(byte)(cornerConfiguration[i]%3);
            hash+=orientation*Math.pow(3,6-i);
        }
        return hash;
    }
    public int getPermutationHash(){
        int hash=0;
        for(byte i=0;i<cornerConfiguration.length;i++){
            byte numGreater=0;
            int piece=(cornerConfiguration[i]/3);
            for(byte j=0;j<i;j++){
                if((cornerConfiguration[j]/3)>piece){
                    numGreater++;
                }
            }
            hash+=numGreater*factorial(i);
        }
        return hash;
    }
    public static int factorial(int number){
        if(number==1||number==0){
            return 1;
        }else{
            return number*factorial(number-1);
        }
    }


}
