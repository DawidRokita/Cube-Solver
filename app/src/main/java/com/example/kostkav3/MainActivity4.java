package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity4 extends AppCompatActivity {

    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    int licznik=0,licznik2=0,licznik3=0,licznik4=0;
    private ImageView img1, img2,img3,img4,img5;

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
        img4 = (ImageView) findViewById(R.id.imageView5);
        img5 = (ImageView) findViewById(R.id.imageView6);
    }

    private void hidetext(){

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik<1){
                    tv2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                }else{
                    tv2.setVisibility(View.GONE);
                    img1.setVisibility(View.GONE);
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
                }else{
                    tv4.setVisibility(View.GONE);
                    tv5.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                    img3.setVisibility(View.GONE);
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
                    img4.setVisibility(View.VISIBLE);
                    img5.setVisibility(View.VISIBLE);
                }else{
                    tv9.setVisibility(View.GONE);
                    img4.setVisibility(View.GONE);
                    img5.setVisibility(View.GONE);
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
