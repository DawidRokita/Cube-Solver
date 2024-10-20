package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity4 extends AppCompatActivity {

    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9, nowy1, nowy3, nowy5, nowy6;
    int licznik=0,licznik2=0,licznik3=0,licznik4=0;
    private ImageView img1, img2,img3, nowy2, nowy4, nowy7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        setupUIViews();
        hidetext();


    }

    private void setupUIViews(){
        tv1 = (TextView) findViewById(R.id.textView13);
        tv2 = (TextView) findViewById(R.id.textView14);
        tv3 = (TextView) findViewById(R.id.textView15);
        tv4 = (TextView) findViewById(R.id.textView16);
        tv5 = (TextView) findViewById(R.id.textView17);
        tv6 = (TextView) findViewById(R.id.textView18);
        tv7 = (TextView) findViewById(R.id.textView19);
        tv8 = (TextView) findViewById(R.id.textView20);
        tv9 = (TextView) findViewById(R.id.textView22);
        img1 = (ImageView) findViewById(R.id.imageView2);
        img2 = (ImageView) findViewById(R.id.imageView3);
        img3 = (ImageView) findViewById(R.id.imageView4);
        nowy1 = (TextView) findViewById(R.id.nowy1);
        nowy2 = (ImageView) findViewById(R.id.nowy2);
        nowy3 = (TextView) findViewById(R.id.nowy3);
        nowy4 = (ImageView) findViewById(R.id.nowy4);
        nowy5 = (TextView) findViewById(R.id.nowy5);
        nowy6 = (TextView) findViewById(R.id.nowy6);
        nowy7 = (ImageView) findViewById(R.id.nowy7);
    }

    private void hidetext(){

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik<1){
                    tv2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    nowy1.setVisibility(View.VISIBLE);
                    nowy2.setVisibility(View.VISIBLE);
                    nowy3.setVisibility(View.VISIBLE);
                    nowy4.setVisibility(View.VISIBLE);
                }else{
                    tv2.setVisibility(View.GONE);
                    img1.setVisibility(View.GONE);
                    nowy1.setVisibility(View.GONE);
                    nowy2.setVisibility(View.GONE);
                    nowy3.setVisibility(View.GONE);
                    nowy4.setVisibility(View.GONE);
                }
                if(licznik==0){
                    licznik++;
                }else{
                    licznik=0;
                }
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik2<1){
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.VISIBLE);
                    nowy5.setVisibility(View.VISIBLE);
                    nowy6.setVisibility(View.VISIBLE);
                    nowy7.setVisibility(View.VISIBLE);
                }else{
                    tv4.setVisibility(View.GONE);
                    tv5.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                    img3.setVisibility(View.GONE);
                    nowy5.setVisibility(View.GONE);
                    nowy6.setVisibility(View.GONE);
                    nowy7.setVisibility(View.GONE);
                }
                if(licznik2==0){
                    licznik2++;
                }else{
                    licznik2=0;
                }
            }
        });

        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik3<1){
                    tv7.setVisibility(View.VISIBLE);
                }else{
                    tv7.setVisibility(View.GONE);
                }
                if(licznik3==0){
                    licznik3++;
                }else{
                    licznik3=0;
                }
            }
        });

        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik4<1){
                    tv9.setVisibility(View.VISIBLE);
                }else{
                    tv9.setVisibility(View.GONE);
                }
                if(licznik4==0){
                    licznik4++;
                }else{
                    licznik4=0;
                }
            }
        });

    }
}
