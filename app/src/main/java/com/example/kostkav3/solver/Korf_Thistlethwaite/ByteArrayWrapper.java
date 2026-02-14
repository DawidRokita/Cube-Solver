package com.example.kostkav3.solver.Korf_Thistlethwaite;

import java.util.Arrays;

public class ByteArrayWrapper {
    byte[] data;
    public ByteArrayWrapper(byte[] data){
        this.data=data.clone();
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        return Arrays.equals(data,((ByteArrayWrapper)obj).data);
    }

    @Override
    public String toString(){
        return Arrays.toString(data);
    }
}
