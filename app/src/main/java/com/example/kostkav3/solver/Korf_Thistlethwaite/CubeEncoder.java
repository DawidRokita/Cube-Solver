package com.example.kostkav3.solver.Korf_Thistlethwaite;

import java.util.*;

public class CubeEncoder{
    final static char[] cubeFaces=new char[]{'R','O','W','Y','G','B'};
    HashMap<CubeColor,Character> cubeColors = new HashMap<>();
    HashMap<Integer, CornerPosition> cornerPositions = new HashMap<>();
    HashMap<Integer, EdgePosition> edgePositions = new HashMap<>();

    HashMap<String, Integer> cornerEncoding  = new HashMap<>();
    HashMap<String, Integer> edgeEncoding = new HashMap<>();

    public static void main(String[] args){
        CubeEncoder encoder=new CubeEncoder();
        encoder.initializeMaps();
        RubiksCube cube= encoder.getCube();
        System.out.println(cube);
    }



    public static class CubeColor {
        public char face;
        public int position;

        public CubeColor(char face, int position) {
            this.face = face;
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CubeColor cubeColor = (CubeColor) o;
            return face == cubeColor.face &&
                    position == cubeColor.position;
        }

        @Override
        public int hashCode() {
            return Objects.hash(face, position);
        }
    }
    public static class CornerPosition{
        CubeColor firstColor;
        CubeColor secondColor;
        CubeColor thirdColor;

        public CornerPosition(char face1, int position1, char face2, int position2, char face3, int position3){
            this.firstColor=new CubeColor(face1,position1);
            this.secondColor=new CubeColor(face2,position2);
            this.thirdColor = new CubeColor(face3, position3);
        }
    }
    public static class EdgePosition{
        CubeColor firstColor;
        CubeColor secondColor;
        public EdgePosition(char face1, int position1, char face2, int position2){
            this.firstColor=new CubeColor(face1, position1);
            this.secondColor=new CubeColor(face2, position2);
        }
    }
    public void initializeCornerPositions(){
        cornerPositions.put(0,new CornerPosition('R',2,'G',6,'W',0));
        cornerPositions.put(1,new CornerPosition('O',0,'W',2,'G',4));
        cornerPositions.put(2,new CornerPosition('O',6,'B',2,'W',4));
        cornerPositions.put(3,new CornerPosition('R',4,'W',6,'B',0));
        cornerPositions.put(4,new CornerPosition('O',4,'Y',2,'B',4));
        cornerPositions.put(5,new CornerPosition('O',2,'G',2,'Y',4));
        cornerPositions.put(6,new CornerPosition('R',0,'Y',6,'G',0));
        cornerPositions.put(7,new CornerPosition('R',6,'B',6,'Y',0));

    }
    public void initializeEdgePositions(){
        edgePositions.put(0, new EdgePosition('W',1,'G',5));
        edgePositions.put(1, new EdgePosition('O',7,'W',3));
        edgePositions.put(2, new EdgePosition('W',5,'B',1));
        edgePositions.put(3, new EdgePosition('R',3,'W',7));
        edgePositions.put(4, new EdgePosition('R',1,'G',7));
        edgePositions.put(5, new EdgePosition('O',1,'G',3));
        edgePositions.put(6, new EdgePosition('O',5,'B',3));
        edgePositions.put(7, new EdgePosition('R',5,'B',7));
        edgePositions.put(8, new EdgePosition('Y',1,'B',5));
        edgePositions.put(9, new EdgePosition('O',3,'Y',3));
        edgePositions.put(10, new EdgePosition('Y',5,'G',1));
        edgePositions.put(11, new EdgePosition('R',7,'Y',7));

    }
    public void initializeCornerEncoding(){
        cornerEncoding.put("RGW",0);
        cornerEncoding.put("WRG",1);
        cornerEncoding.put("GWR",2);

        cornerEncoding.put("OWG",3);
        cornerEncoding.put("GOW",4);
        cornerEncoding.put("WGO",5);

        cornerEncoding.put("OBW",6);
        cornerEncoding.put("WOB",7);
        cornerEncoding.put("BWO",8);

        cornerEncoding.put("RWB",9);
        cornerEncoding.put("BRW",10);
        cornerEncoding.put("WBR",11);

        cornerEncoding.put("OYB",12);
        cornerEncoding.put("BOY",13);
        cornerEncoding.put("YBO",14);

        cornerEncoding.put("OGY",15);
        cornerEncoding.put("YOG",16);
        cornerEncoding.put("GYO",17);

        cornerEncoding.put("RYG",18);
        cornerEncoding.put("GRY",19);
        cornerEncoding.put("YGR",20);

        cornerEncoding.put("RBY",21);
        cornerEncoding.put("YRB",22);
        cornerEncoding.put("BYR",23);
    }
    public void initializeEdgeEncoding(){
        edgeEncoding.put("WG",0);
        edgeEncoding.put("GW",1);

        edgeEncoding.put("OW",2);
        edgeEncoding.put("WO",3);

        edgeEncoding.put("WB",4);
        edgeEncoding.put("BW",5);

        edgeEncoding.put("RW",6);
        edgeEncoding.put("WR",7);

        edgeEncoding.put("RG",8);
        edgeEncoding.put("GR",9);

        edgeEncoding.put("OG",10);
        edgeEncoding.put("GO",11);

        edgeEncoding.put("OB",12);
        edgeEncoding.put("BO",13);

        edgeEncoding.put("RB",14);
        edgeEncoding.put("BR",15);

        edgeEncoding.put("YB",16);
        edgeEncoding.put("BY",17);

        edgeEncoding.put("OY",18);
        edgeEncoding.put("YO",19);

        edgeEncoding.put("YG",20);
        edgeEncoding.put("GY",21);

        edgeEncoding.put("RY",22);
        edgeEncoding.put("YR",23);


    }
    public void initializeMaps(){
        initializeCornerPositions();
        initializeEdgePositions();
        initializeCornerEncoding();
        initializeEdgeEncoding();
    }
    public RubiksCube getCube(){
        Scanner scanner=new Scanner(System.in);
        for(char c:cubeFaces){
            System.out.println("Enter colors for "+c+" face");
            for(int i=0;i<8;i++){
                System.out.print(i+":");
                char color=scanner.next().charAt(0);
                cubeColors.put(new CubeColor(c,i),color);
            }
        }
        RubiksCube cube=new RubiksCube();
        for(Map.Entry<Integer,CornerPosition> entry:cornerPositions.entrySet()){
            String corner=""+cubeColors.get(entry.getValue().firstColor)+
                    cubeColors.get(entry.getValue().secondColor)+
                    cubeColors.get(entry.getValue().thirdColor);
            System.out.println(corner);
            byte encoding=cornerEncoding.get(corner).byteValue();
            cube.corners[entry.getKey()]=encoding;


        }
        for(Map.Entry<Integer,EdgePosition> entry:edgePositions.entrySet()){
            String edge=""+cubeColors.get(entry.getValue().firstColor)+
                    cubeColors.get(entry.getValue().secondColor);

            byte encoding=edgeEncoding.get(edge).byteValue();
            cube.edges[entry.getKey()]=encoding;


        }
        return cube;

    }


}