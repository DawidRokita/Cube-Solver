package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity6 extends AppCompatActivity {

    private TextView textView, textView2;
    private Button pole1,pole2,pole3,pole4,pole5,pole6,pole7,pole8,pole9,pole10,pole11,pole12,pole13,pole14,pole15,pole16,pole17,pole18,pole19,pole20,pole21,pole22,pole23,pole24;
    private String part;
    private String[] cutstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        setupUIViews();

        Button[] tabbtn = {pole1,pole2,pole3,pole4,pole5,pole6,pole7,pole8,pole9,pole10,pole11,pole12,pole13,pole14,pole15,pole16,pole17,pole18,pole19,pole20,pole21,pole22,pole23,pole24};
        String result = getIntent().getExtras().getString("message_key");
        textView.setText(result);

            String str = textView.getText().toString();
            cutstr = str.split(" +");

            for(int i=0; i<cutstr.length; i++){

                part = cutstr[i];

                switch (part){
                    case "R":       tabbtn[i].setBackgroundResource(R.drawable.ruchr);        tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "U":       tabbtn[i].setBackgroundResource(R.drawable.ruchu);        tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "L":       tabbtn[i].setBackgroundResource(R.drawable.ruchl);        tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "D":       tabbtn[i].setBackgroundResource(R.drawable.ruchd);        tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "F":       tabbtn[i].setBackgroundResource(R.drawable.ruchf);        tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "B":       tabbtn[i].setBackgroundResource(R.drawable.ruchb);        tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "R\'":     tabbtn[i].setBackgroundResource(R.drawable.ruchrprim);    tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "U\'":     tabbtn[i].setBackgroundResource(R.drawable.ruchuprim);    tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "L\'":     tabbtn[i].setBackgroundResource(R.drawable.ruchlprim);    tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "D\'":     tabbtn[i].setBackgroundResource(R.drawable.ruchdprim);    tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "F\'":     tabbtn[i].setBackgroundResource(R.drawable.ruchfprim);    tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "B\'":     tabbtn[i].setBackgroundResource(R.drawable.ruchbprim);    tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "R2":      tabbtn[i].setBackgroundResource(R.drawable.ruchrr);       tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "U2":      tabbtn[i].setBackgroundResource(R.drawable.ruchuu);       tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "L2":      tabbtn[i].setBackgroundResource(R.drawable.ruchll);       tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "D2":      tabbtn[i].setBackgroundResource(R.drawable.ruchdd);       tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "F2":      tabbtn[i].setBackgroundResource(R.drawable.ruchff);       tabbtn[i].setVisibility(View.VISIBLE);   break;
                    case "B2":      tabbtn[i].setBackgroundResource(R.drawable.ruchbb);       tabbtn[i].setVisibility(View.VISIBLE);   break;
                    default:        Toast.makeText(MainActivity6.this, "Nie ma rozwiązania dla tego układu kostki", Toast.LENGTH_LONG).show();     break;
                }

            }

        }


    private void setupUIViews(){
        textView = (TextView) findViewById(R.id.textView25);
        textView2 = (TextView) findViewById(R.id.textView26);
        pole1 = (Button) findViewById(R.id.pole1);
        pole2 = (Button) findViewById(R.id.pole2);
        pole3 = (Button) findViewById(R.id.pole3);
        pole4 = (Button) findViewById(R.id.pole4);
        pole5 = (Button) findViewById(R.id.pole5);
        pole6 = (Button) findViewById(R.id.pole6);
        pole7 = (Button) findViewById(R.id.pole7);
        pole8 = (Button) findViewById(R.id.pole8);
        pole9 = (Button) findViewById(R.id.pole9);
        pole10 = (Button) findViewById(R.id.pole10);
        pole11 = (Button) findViewById(R.id.pole11);
        pole12 = (Button) findViewById(R.id.pole12);
        pole13 = (Button) findViewById(R.id.pole13);
        pole14 = (Button) findViewById(R.id.pole14);
        pole15 = (Button) findViewById(R.id.pole15);
        pole16 = (Button) findViewById(R.id.pole16);
        pole17 = (Button) findViewById(R.id.pole17);
        pole18 = (Button) findViewById(R.id.pole18);
        pole19 = (Button) findViewById(R.id.pole19);
        pole20 = (Button) findViewById(R.id.pole20);
        pole21 = (Button) findViewById(R.id.pole21);
        pole22 = (Button) findViewById(R.id.pole22);
        pole23 = (Button) findViewById(R.id.pole23);
        pole24 = (Button) findViewById(R.id.pole24);
    }

}
