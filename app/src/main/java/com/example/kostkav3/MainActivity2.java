package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity2 extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;

    int licznik = 0;
    private Button white, red, green, orange, blue, yellow, divider, reset, lewo, prawo, dol, gora, solve, next, send;
    private Button c1, c2, c3, c4, c5, c6, c7, c8, c9;
    private TextView textView2, textView3, textView4, textView5;
    String col1, col2, col3, col4, col5, col6, colr7, col8, col9,resultat;
    String result, scrambledCube;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setupUIViews();
        choicecolor();
        cubesym(c1);
        cubesym(c2);
        cubesym(c3);
        cubesym(c4);
        cubesym(c6);
        cubesym(c7);
        cubesym(c8);
        cubesym(c9);
        reset();
        nextbutton();

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrambledCube = textView3.getText().toString();
                simpleSolve(scrambledCube);
                textView5.setText(result);

                solve.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);

                clearcube();
                saveData();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity5.class);
                intent.putExtra("message_key",result);
                startActivity(intent);
            }
        });

        loadData();
        updateViews();


    }

    public void saveData() {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TEXT, textView5.getText().toString());
            editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    public void updateViews(){
        textView5.setText(text);
    }

    private void setupUIViews(){
        white = (Button) findViewById(R.id.button);
        red = (Button) findViewById(R.id.button2);
        green = (Button) findViewById(R.id.button3);
        orange = (Button) findViewById(R.id.button4);
        blue = (Button) findViewById(R.id.button5);
        yellow = (Button) findViewById(R.id.button6);
        divider = (Button) findViewById(R.id.divider);
        send = (Button) findViewById(R.id.button11);
        c1 = (Button) findViewById(R.id.c1);
        c2 = (Button) findViewById(R.id.c2);
        c3 = (Button) findViewById(R.id.c3);
        c4 = (Button) findViewById(R.id.c4);
        c5 = (Button) findViewById(R.id.c5);
        c6 = (Button) findViewById(R.id.c6);
        c7 = (Button) findViewById(R.id.c7);
        c8 = (Button) findViewById(R.id.c8);
        c9 = (Button) findViewById(R.id.c9);
        next = (Button) findViewById(R.id.next);
        reset = (Button) findViewById(R.id.reset);
        lewo = (Button) findViewById(R.id.lewo);
        prawo = (Button) findViewById(R.id.prawo);
        dol = (Button) findViewById(R.id.dol);
        gora = (Button) findViewById(R.id.button8);
        solve = (Button) findViewById(R.id.solve);

        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);

    }

    private void choicecolor(){
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("white");
                divider.setBackgroundResource(R.color.white);
                white.setText("✔");
                red.setText("");
                green.setText("");
                orange.setText("");
                blue.setText("");
                yellow.setText("");
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("red");
                divider.setBackgroundResource(R.color.red);
                white.setText("");
                red.setText("✔");
                green.setText("");
                orange.setText("");
                blue.setText("");
                yellow.setText("");
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("green");
                divider.setBackgroundResource(R.color.green);
                white.setText("");
                red.setText("");
                green.setText("✔");
                orange.setText("");
                blue.setText("");
                yellow.setText("");
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("orange");
                divider.setBackgroundResource(R.color.orange);
                white.setText("");
                red.setText("");
                green.setText("");
                orange.setText("✔");
                blue.setText("");
                yellow.setText("");
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("blue");
                divider.setBackgroundResource(R.color.blue);
                white.setText("");
                red.setText("");
                green.setText("");
                orange.setText("");
                blue.setText("✔");
                yellow.setText("");
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("yellow");
                divider.setBackgroundResource(R.color.yellow);
                white.setText("");
                red.setText("");
                green.setText("");
                orange.setText("");
                blue.setText("");
                yellow.setText("✔");
            }
        });
    }

    private void textcube(){

        switch(c1.getText().toString()){
            case "U":   col1 = "U";   break;
            case "R":   col1 = "R";   break;
            case "F":   col1 = "F";   break;
            case "L":   col1 = "L";   break;
            case "B":   col1 = "B";   break;
            case "D":   col1 = "D";   break;
            default:    col1 = "!";   break;
        }

        switch(c2.getText().toString()){
            case "U":   col2 = "U";   break;
            case "R":   col2 = "R";   break;
            case "F":   col2 = "F";   break;
            case "L":   col2 = "L";   break;
            case "B":   col2 = "B";   break;
            case "D":   col2 = "D";   break;
            default:    col2 = "!";   break;
        }

        switch(c3.getText().toString()){
            case "U":   col3 = "U";   break;
            case "R":   col3 = "R";   break;
            case "F":   col3 = "F";   break;
            case "L":   col3 = "L";   break;
            case "B":   col3 = "B";   break;
            case "D":   col3 = "D";   break;
            default:    col3 = "!";   break;
        }

        switch(c4.getText().toString()){
            case "U":   col4 = "U";   break;
            case "R":   col4 = "R";   break;
            case "F":   col4 = "F";   break;
            case "L":   col4 = "L";   break;
            case "B":   col4 = "B";   break;
            case "D":   col4 = "D";   break;
            default:    col4 = "!";   break;
        }

        switch(c5.getText().toString()){
            case "U":   col5 = "U";   break;
            case "R":   col5 = "R";   break;
            case "F":   col5 = "F";   break;
            case "L":   col5 = "L";   break;
            case "B":   col5 = "B";   break;
            case "D":   col5 = "D";   break;
            default:    col5 = "!";   break;
        }

        switch(c6.getText().toString()){
            case "U":   col6 = "U";   break;
            case "R":   col6 = "R";   break;
            case "F":   col6 = "F";   break;
            case "L":   col6 = "L";   break;
            case "B":   col6 = "B";   break;
            case "D":   col6 = "D";   break;
            default:    col6 = "!";   break;
        }

        switch(c7.getText().toString()){
            case "U":   colr7 = "U";   break;
            case "R":   colr7 = "R";   break;
            case "F":   colr7 = "F";   break;
            case "L":   colr7 = "L";   break;
            case "B":   colr7 = "B";   break;
            case "D":   colr7 = "D";   break;
            default:    colr7 = "!";   break;
        }

        switch(c8.getText().toString()){
            case "U":   col8 = "U";   break;
            case "R":   col8 = "R";   break;
            case "F":   col8 = "F";   break;
            case "L":   col8 = "L";   break;
            case "B":   col8 = "B";   break;
            case "D":   col8 = "D";   break;
            default:    col8 = "!";   break;
        }

        switch(c9.getText().toString()){
            case "U":   col9 = "U";   break;
            case "R":   col9 = "R";   break;
            case "F":   col9 = "F";   break;
            case "L":   col9 = "L";   break;
            case "B":   col9 = "B";   break;
            case "D":   col9 = "D";   break;
            default:    col9 = "!";   break;
        }

    }

    private void clearcube(){
        c1.setText("");
        c1.setBackgroundResource(R.color.black);

        c2.setText("");
        c2.setBackgroundResource(R.color.black);

        c3.setText("");
        c3.setBackgroundResource(R.color.black);

        c4.setText("");
        c4.setBackgroundResource(R.color.black);

        c6.setText("");
        c6.setBackgroundResource(R.color.black);

        c7.setText("");
        c7.setBackgroundResource(R.color.black);

        c8.setText("");
        c8.setBackgroundResource(R.color.black);

        c9.setText("");
        c9.setBackgroundResource(R.color.black);
    }

    private void reset(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearcube();
                textView3.setText("");
                textView2.setText("");
                divider.setBackgroundResource(R.color.clear);
                c5.setText("U");
                c5.setBackgroundResource(R.color.white);
                licznik=0;
                textView4.setText("wprowadź stan górnej ściany");
                lewo.setBackgroundResource(R.color.orange);
                prawo.setBackgroundResource(R.color.red);
                dol.setBackgroundResource(R.color.green);
                gora.setBackgroundResource(R.color.blue);
                c5.setTextColor(0xFFB8BBB9);
                next.setClickable(true);
                c1.setClickable(true);
                c2.setClickable(true);
                c3.setClickable(true);
                c4.setClickable(true);
                c6.setClickable(true);
                c7.setClickable(true);
                c8.setClickable(true);
                c9.setClickable(true);
                white.setText("");
                red.setText("");
                green.setText("");
                orange.setText("");
                blue.setText("");
                yellow.setText("");
                textView5.setText("");
                solve.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                send.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void cubesym (final Button btn){

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    switch (textView2.getText().toString()) {
                        case "white":
                            btn.setBackgroundResource(R.color.white);
                            btn.setText("U");
                            btn.setTextColor(0xFFB8BBB9);
                            break;
                        case "red":
                            btn.setBackgroundResource(R.color.red);
                            btn.setText("R");
                            btn.setTextColor(0xFFE60A0A);
                            break;
                        case "green":
                            btn.setBackgroundResource(R.color.green);
                            btn.setText("F");
                            btn.setTextColor(0xFF76FF03);
                            break;
                        case "orange":
                            btn.setBackgroundResource(R.color.orange);
                            btn.setText("L");
                            btn.setTextColor(0xFFFF9100);
                            break;
                        case "blue":
                            btn.setBackgroundResource(R.color.blue);
                            btn.setText("B");
                            btn.setTextColor(0xFF00B0FF);
                            break;
                        case "yellow":
                            btn.setBackgroundResource(R.color.yellow);
                            btn.setText("D");
                            btn.setTextColor(0xFFFFEA00);
                            break;
                    }

            }
        });
    }

    private void nextbutton(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                licznik++;
                textcube();

                resultat = col1 + col2  + col3+ col4+ col5 + col6 + colr7 + col8 + col9;

                textView3.setText(textView3.getText().toString() + resultat);

                clearcube();

                switch(licznik){
                    case 1:
                        c5.setText("R");
                        c5.setBackgroundResource(R.color.red);
                        textView4.setText("wprowadź stan prawej ściany");
                        lewo.setBackgroundResource(R.color.green);
                        prawo.setBackgroundResource(R.color.blue);
                        dol.setBackgroundResource(R.color.yellow);
                        gora.setBackgroundResource(R.color.white);
                        c5.setTextColor(0xFFE60A0A);
                        break;
                    case 2:
                        c5.setText("F");
                        c5.setBackgroundResource(R.color.green);
                        textView4.setText("wprowadź stan przedniej ściany");
                        lewo.setBackgroundResource(R.color.orange);
                        prawo.setBackgroundResource(R.color.red);
                        dol.setBackgroundResource(R.color.yellow);
                        gora.setBackgroundResource(R.color.white);
                        c5.setTextColor(0xFF76FF03);
                        break;
                    case 3:
                        c5.setText("D");
                        c5.setBackgroundResource(R.color.yellow);
                        textView4.setText("wprowadź stan dolnej ściany");
                        lewo.setBackgroundResource(R.color.orange);
                        prawo.setBackgroundResource(R.color.red);
                        dol.setBackgroundResource(R.color.blue);
                        gora.setBackgroundResource(R.color.green);
                        c5.setTextColor(0xFFFFEA00);
                        break;
                    case 4:
                        c5.setText("L");
                        c5.setBackgroundResource(R.color.orange);
                        textView4.setText("wprowadź stan lewej ściany");
                        lewo.setBackgroundResource(R.color.blue);
                        prawo.setBackgroundResource(R.color.green);
                        dol.setBackgroundResource(R.color.yellow);
                        gora.setBackgroundResource(R.color.white);
                        c5.setTextColor(0xFFFF9100);
                        break;
                    case 5:
                        c5.setText("B");
                        c5.setBackgroundResource(R.color.blue);
                        textView4.setText("wprowadź stan tylnej ściany");
                        lewo.setBackgroundResource(R.color.red);
                        prawo.setBackgroundResource(R.color.orange);
                        dol.setBackgroundResource(R.color.yellow);
                        gora.setBackgroundResource(R.color.white);
                        c5.setTextColor(0xFF00B0FF);
                        break;
                    default:
                        c5.setText("");
                        c5.setBackgroundResource(R.color.black);
                        textView4.setText("dzięki za wprowadzenie stanu kostki");
                        lewo.setBackgroundResource(R.color.white);
                        prawo.setBackgroundResource(R.color.white);
                        dol.setBackgroundResource(R.color.white);
                        gora.setBackgroundResource(R.color.white);
                        next.setClickable(false);
                        c1.setClickable(false);
                        c2.setClickable(false);
                        c3.setClickable(false);
                        c4.setClickable(false);
                        c6.setClickable(false);
                        c7.setClickable(false);
                        c8.setClickable(false);
                        c9.setClickable(false);
                        solve.setVisibility(View.VISIBLE);
                        next.setVisibility(View.INVISIBLE);
                        reset.setVisibility(View.INVISIBLE);
                        send.setVisibility(View.INVISIBLE);
                        break;

                }

            }
        });
    }

    public void simpleSolve(String scrambledCube) {
        result = new Search().solution(scrambledCube, 21, 100000000, 0, 0);
    }
}
