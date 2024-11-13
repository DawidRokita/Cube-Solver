package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity2 extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private ImageView capturedImage;
    private Bitmap bitmap;
    private TextView[][] colorTextViews;

    int licznik = 0, maxMoves = 21, minProbe = 5000, count_click = 1;
    private Button white, red, green, orange, blue, yellow, divider, reset, lewo, prawo, dol, gora, solve, next, send;
    private Button c1, c2, c3, c4, c5, c6, c7, c8, c9, graphic;
    private TextView textView2, textView3, textView4, textView5, textView10, textView27, textView28;
    String col1, col2, col3, col4, col5, col6, colr7, col8, col9, resultat;
    String result, scrambledCube;
    private EditText liczbaruchow, minproby;
    public Button captureButton;

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

        colorTextViews = new TextView[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int resId = getResources().getIdentifier("color_" + i + j, "id", getPackageName());
                colorTextViews[i][j] = findViewById(resId);
            }
        }

        captureButton = findViewById(R.id.capture_button);

        captureButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                dispatchTakePictureIntent();
            }
        });

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                solve.setVisibility(View.INVISIBLE);

                scrambledCube = textView3.getText().toString();

                String maxMovesText = liczbaruchow.getText().toString();
                String minProbeText = minproby.getText().toString();

                minProbe = Integer.parseInt(minProbeText);
                maxMoves = Integer.parseInt(maxMovesText);

                long startTime = System.currentTimeMillis();    // Zapisanie czasu początkowego

                simpleSolve(scrambledCube, maxMoves, minProbe);

                long endTime = System.currentTimeMillis();  // Zapisanie czasu końcowego
                long duration = endTime - startTime;     // roznica czasu - czas potrzebny do znalezienia rozwiazania

                reset.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);

                textView10.setVisibility(View.INVISIBLE);
                liczbaruchow.setVisibility(View.INVISIBLE);
                textView27.setVisibility(View.INVISIBLE);
                minproby.setVisibility(View.INVISIBLE);
                textView28.setVisibility(View.INVISIBLE);

                graphic.setVisibility(View.VISIBLE);

                if (!result.equals("Brak rozwiązania")) {
                    result = result.replaceAll("\\s+", " ").trim();
                    textView5.setText(result);
                    String[] moves = result.trim().split(" ");
                    int numberOfMoves = moves.length;
                    showToast("Znaleziono rozwiązanie w czasie: " + duration + " ms\nLiczba ruchów: " + numberOfMoves, 5000);
                } else {
                    showToast("Nie znaleziono rozwiązania",2000);
                }

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

        graphic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
                    intent.putExtra("message_key",result);
                    startActivity(intent);
            }
        });

        textView28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count_click == 1){
                    textView10.setVisibility(View.VISIBLE);
                    liczbaruchow.setVisibility(View.VISIBLE);
                    textView27.setVisibility(View.VISIBLE);
                    minproby.setVisibility(View.VISIBLE);
                    textView28.setText("Parametry algorytmu ▼");
                    count_click = 0;
                }else{
                    textView10.setVisibility(View.INVISIBLE);
                    liczbaruchow.setVisibility(View.INVISIBLE);
                    textView27.setVisibility(View.INVISIBLE);
                    minproby.setVisibility(View.INVISIBLE);
                    textView28.setText("Parametry algorytmu ▶");
                    count_click = 1;
                }

            }
        });

        loadData();
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
        graphic = (Button) findViewById(R.id.button12);

        liczbaruchow = (EditText) findViewById(R.id.liczbaruchow);
        minproby = (EditText) findViewById(R.id.minproby);

        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView27 = (TextView) findViewById(R.id.textView27);
        textView28 = (TextView) findViewById(R.id.textView28);

        capturedImage = findViewById(R.id.captured_image);
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorTextViews[i][j].setText("");
            }
        }

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
                graphic.setVisibility(View.INVISIBLE);
//                textView10.setVisibility(View.INVISIBLE);
//                liczbaruchow.setVisibility(View.INVISIBLE);
//                textView27.setVisibility(View.INVISIBLE);
//                minproby.setVisibility(View.INVISIBLE);
                textView28.setVisibility(View.INVISIBLE);
                captureButton.setVisibility(View.VISIBLE);
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

                resultat = col1 + col2 + col3 + col4 + col5 + col6 + colr7 + col8 + col9;

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
//                        textView10.setVisibility(View.VISIBLE);
//                        liczbaruchow.setVisibility(View.VISIBLE);
//                        textView27.setVisibility(View.VISIBLE);
//                        minproby.setVisibility(View.VISIBLE);
                        textView28.setVisibility(View.VISIBLE);
                        captureButton.setVisibility(View.GONE);
                        break;
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {;
                showToast("Brak uprawnień do używania aparatu",3000);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(bitmap);

            // Odczytaj kolory
            readColorsFromBitmap(bitmap);

            // wpisz kolory na panel
            c1.setText(colorTextViews[0][0].getText().toString());
            c2.setText(colorTextViews[0][1].getText().toString());
            c3.setText(colorTextViews[0][2].getText().toString());

            c4.setText(colorTextViews[1][0].getText().toString());
            c6.setText(colorTextViews[1][2].getText().toString());

            c7.setText(colorTextViews[2][0].getText().toString());
            c8.setText(colorTextViews[2][1].getText().toString());
            c9.setText(colorTextViews[2][2].getText().toString());



            switch (c1.getText().toString()){
                case "U":   c1.setTextColor(0xFFB8BBB9);    c1.setBackgroundResource(R.color.white);   break;
                case "R":   c1.setTextColor(0xFFE60A0A);    c1.setBackgroundResource(R.color.red);   break;
                case "F":   c1.setTextColor(0xFF76FF03);    c1.setBackgroundResource(R.color.green);   break;
                case "L":   c1.setTextColor(0xFFFF9100);    c1.setBackgroundResource(R.color.orange);       break;
                case "B":   c1.setTextColor(0xFF00B0FF);    c1.setBackgroundResource(R.color.blue);   break;
                case "D":   c1.setTextColor(0xFFFFEA00);    c1.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c2.getText().toString()){
                case "U":   c2.setTextColor(0xFFB8BBB9);    c2.setBackgroundResource(R.color.white);   break;
                case "R":   c2.setTextColor(0xFFE60A0A);    c2.setBackgroundResource(R.color.red);   break;
                case "F":   c2.setTextColor(0xFF76FF03);    c2.setBackgroundResource(R.color.green);   break;
                case "L":   c2.setTextColor(0xFFFF9100);    c2.setBackgroundResource(R.color.orange);       break;
                case "B":   c2.setTextColor(0xFF00B0FF);    c2.setBackgroundResource(R.color.blue);   break;
                case "D":   c2.setTextColor(0xFFFFEA00);    c2.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c3.getText().toString()){
                case "U":   c3.setTextColor(0xFFB8BBB9);    c3.setBackgroundResource(R.color.white);   break;
                case "R":   c3.setTextColor(0xFFE60A0A);    c3.setBackgroundResource(R.color.red);   break;
                case "F":   c3.setTextColor(0xFF76FF03);    c3.setBackgroundResource(R.color.green);   break;
                case "L":   c3.setTextColor(0xFFFF9100);    c3.setBackgroundResource(R.color.orange);       break;
                case "B":   c3.setTextColor(0xFF00B0FF);    c3.setBackgroundResource(R.color.blue);   break;
                case "D":   c3.setTextColor(0xFFFFEA00);    c3.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c4.getText().toString()){
                case "U":   c4.setTextColor(0xFFB8BBB9);    c4.setBackgroundResource(R.color.white);   break;
                case "R":   c4.setTextColor(0xFFE60A0A);    c4.setBackgroundResource(R.color.red);   break;
                case "F":   c4.setTextColor(0xFF76FF03);    c4.setBackgroundResource(R.color.green);   break;
                case "L":   c4.setTextColor(0xFFFF9100);    c4.setBackgroundResource(R.color.orange);       break;
                case "B":   c4.setTextColor(0xFF00B0FF);    c4.setBackgroundResource(R.color.blue);   break;
                case "D":   c4.setTextColor(0xFFFFEA00);    c4.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c6.getText().toString()){
                case "U":   c6.setTextColor(0xFFB8BBB9);    c6.setBackgroundResource(R.color.white);   break;
                case "R":   c6.setTextColor(0xFFE60A0A);    c6.setBackgroundResource(R.color.red);   break;
                case "F":   c6.setTextColor(0xFF76FF03);    c6.setBackgroundResource(R.color.green);   break;
                case "L":   c6.setTextColor(0xFFFF9100);    c6.setBackgroundResource(R.color.orange);       break;
                case "B":   c6.setTextColor(0xFF00B0FF);    c6.setBackgroundResource(R.color.blue);   break;
                case "D":   c6.setTextColor(0xFFFFEA00);    c6.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c7.getText().toString()){
                case "U":   c7.setTextColor(0xFFB8BBB9);    c7.setBackgroundResource(R.color.white);   break;
                case "R":   c7.setTextColor(0xFFE60A0A);    c7.setBackgroundResource(R.color.red);   break;
                case "F":   c7.setTextColor(0xFF76FF03);    c7.setBackgroundResource(R.color.green);   break;
                case "L":   c7.setTextColor(0xFFFF9100);    c7.setBackgroundResource(R.color.orange);       break;
                case "B":   c7.setTextColor(0xFF00B0FF);    c7.setBackgroundResource(R.color.blue);   break;
                case "D":   c7.setTextColor(0xFFFFEA00);    c7.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c8.getText().toString()){
                case "U":   c8.setTextColor(0xFFB8BBB9);    c8.setBackgroundResource(R.color.white);   break;
                case "R":   c8.setTextColor(0xFFE60A0A);    c8.setBackgroundResource(R.color.red);   break;
                case "F":   c8.setTextColor(0xFF76FF03);    c8.setBackgroundResource(R.color.green);   break;
                case "L":   c8.setTextColor(0xFFFF9100);    c8.setBackgroundResource(R.color.orange);       break;
                case "B":   c8.setTextColor(0xFF00B0FF);    c8.setBackgroundResource(R.color.blue);   break;
                case "D":   c8.setTextColor(0xFFFFEA00);    c8.setBackgroundResource(R.color.yellow);   break;
            }

            switch (c9.getText().toString()){
                case "U":   c9.setTextColor(0xFFB8BBB9);    c9.setBackgroundResource(R.color.white);   break;
                case "R":   c9.setTextColor(0xFFE60A0A);    c9.setBackgroundResource(R.color.red);   break;
                case "F":   c9.setTextColor(0xFF76FF03);    c9.setBackgroundResource(R.color.green);   break;
                case "L":   c9.setTextColor(0xFFFF9100);    c9.setBackgroundResource(R.color.orange);       break;
                case "B":   c9.setTextColor(0xFF00B0FF);    c9.setBackgroundResource(R.color.blue);   break;
                case "D":   c9.setTextColor(0xFFFFEA00);    c9.setBackgroundResource(R.color.yellow);   break;
            }

        }
    }

    private void readColorsFromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            showToast("Pusta bitmapa!", 3000);
            return;
        }

        int width = bitmap.getWidth() / 3;
        int height = bitmap.getHeight() / 3;

        int margin = 5;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Wyznacz granice centralnego obszaru na podstawie marginesu
                int startX = j * width + margin;
                int endX = (j + 1) * width - margin;
                int startY = i * height + margin;
                int endY = (i + 1) * height - margin;

                // Oblicz średnie wartości RGB w centralnym obszarze pola
                int r = 0, g = 0, b = 0;
                int count = 0;
                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        int pixel = bitmap.getPixel(x, y);
                        r += Color.red(pixel);
                        g += Color.green(pixel);
                        b += Color.blue(pixel);
                        count++;
                    }
                }

                // Oblicz średni kolor
                int averageColor = Color.rgb(r / count, g / count, b / count);
                String colorName = getColorName(averageColor);

                // Wyświetl nazwę koloru w odpowiednim polu TextView
                colorTextViews[i][j].setText(colorName);
                Log.d("ColorInfo", "Kolor w polu (" + i + ", " + j + "): " + colorName);
            }
        }
    }


    private String getColorName(int pixelColor) {

        float[] hsv = new float[3];
        Color.colorToHSV(pixelColor, hsv);

        float hue = hsv[0]; //kolor 0-360
        float saturation = hsv[1];  //nasycenie
        float value = hsv[2];   //jasność

        // Przypisanie kolorów na podstawie zakresów HSV
        if (value > 0.45 && saturation < 0.3) {
            return "U"; // Wysoka jasność i niskie nasycenie sugeruje biały
        } else if (hue >= 40 && hue < 70) {
            return "D"; //żółty
        } else if (hue >= 70 && hue < 165) {
            return "F"; //zielony
        } else if (hue < 7 || hue >= 300) {
            return "R"; //czerwony
        } else if (hue >= 165 && hue < 300) {
            return "B"; //niebieski
        } else if (hue >= 7 && hue < 40) {
            return "L"; //pomarańczowy
        } else {
        return "!";
        }
    }

    public void simpleSolve(String scrambledCube, int maxMoves, int minProbe) {
        result = new Search().solution(scrambledCube, maxMoves, 100000000, minProbe, 0);
    }



    private void showToast(String message, int duration) {
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        // Ustawienie opóźnienia, aby zamknąć toast po zadanym czasie
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel(); // Zamknij toast
            }
        }, duration);
    }

}
