package com.example.kostkav3.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kostkav3.R;
import com.example.kostkav3.solver.lbl.LblSolverFacade;
import com.example.kostkav3.solver.lbl.MoveNormalizer;
import com.example.kostkav3.solver.twophase.Search;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private ImageView capturedImage;
    private Bitmap bitmap;
    private TextView[][] colorTextViews;

    int licznik = 0, maxMoves = 21, minProbe = 5000, count_click = 1;
    private Button white, red, green, orange, blue, yellow, divider, reset, lewo, prawo, dol, gora, solve, next, send;
    private Button c1, c2, c3, c4, c5, c6, c7, c8, c9, graphic;
    private Button btnSelectTwoPhase, btnLBL, btnCFOP, btnTwoPhaseExt, btnMetaSolver;
    private TextView textView2, textView3, textView4, textView5, textView10, textView27, textView28;
    private CheckBox checkBoxNormalizeLBL;
    private TextView textNormalizeLBL, textParametryLBL;
    String col1, col2, col3, col4, col5, col6, colr7, col8, col9, resultat;
    String result, scrambledCube;
    private EditText liczbaruchow, minproby;
    public Button captureButton;

    public int selectedAlgorithm = 1;

    private ActivityResultLauncher<Intent> cameraXLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setupUIViews();

        cameraXLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if (activityResult.getResultCode() == RESULT_OK && activityResult.getData() != null) {

                        String path = activityResult.getData().getStringExtra(CameraXActivity.EXTRA_IMAGE_PATH);

                        if (path != null) {
                            bitmap = BitmapFactory.decodeFile(path);
                            if (bitmap == null) {
                                showToast("Nie udało się wczytać zdjęcia", 2500);
                                return;
                            }

                            capturedImage.setImageBitmap(bitmap);

                            readColorsFromBitmap(bitmap);   // Odczytaj kolory

                            // Wpisz litery (U/R/F/L/B/D) na panel
                            c1.setText(colorTextViews[0][0].getText().toString());
                            c2.setText(colorTextViews[0][1].getText().toString());
                            c3.setText(colorTextViews[0][2].getText().toString());

                            c4.setText(colorTextViews[1][0].getText().toString());
                            c6.setText(colorTextViews[1][2].getText().toString());

                            c7.setText(colorTextViews[2][0].getText().toString());
                            c8.setText(colorTextViews[2][1].getText().toString());
                            c9.setText(colorTextViews[2][2].getText().toString());

                            // Ustaw kolory przycisków
                            applyStickerStyle(c1);
                            applyStickerStyle(c2);
                            applyStickerStyle(c3);
                            applyStickerStyle(c4);
                            applyStickerStyle(c6);
                            applyStickerStyle(c7);
                            applyStickerStyle(c8);
                            applyStickerStyle(c9);
                        }
                    }
                }
        );

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

        captureButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                Intent intent = new Intent(MainActivity2.this, CameraXActivity.class);

                intent.putExtra(CameraXActivity.EXTRA_LEFT_COLOR,  getColorFromButton(lewo));
                intent.putExtra(CameraXActivity.EXTRA_RIGHT_COLOR, getColorFromButton(prawo));
                intent.putExtra(CameraXActivity.EXTRA_DOWN_COLOR,  getColorFromButton(dol));
                intent.putExtra(CameraXActivity.EXTRA_UP_COLOR,    getColorFromButton(gora));
                intent.putExtra(CameraXActivity.EXTRA_C5_COLOR, getColorFromButton(c5));

                cameraXLauncher.launch(intent);
            }
        });

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                solve.setEnabled(false);
                solve.setVisibility(View.INVISIBLE);

                scrambledCube = textView3.getText().toString();

                String maxMovesText = liczbaruchow.getText().toString().trim();
                String minProbeText = minproby.getText().toString().trim();

                int localMaxMoves = 21;
                int localMinProbe = 5000;
                try {
                    if (!maxMovesText.isEmpty()) localMaxMoves = Integer.parseInt(maxMovesText);
                    if (!minProbeText.isEmpty()) localMinProbe = Integer.parseInt(minProbeText);
                } catch (NumberFormatException e) {
                    solve.setEnabled(true);
                    solve.setVisibility(View.VISIBLE);
                    showSolutionToast("Błędne parametry (maxMoves / minProbe)", 2500);
                    return;
                }

                maxMoves = localMaxMoves;
                minProbe = localMinProbe;

                new Thread(() -> {
                    long startTime = System.currentTimeMillis();

                    result = solveSelectedAlgorithm(scrambledCube, maxMoves, minProbe);

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    runOnUiThread(() -> {

                        reset.setVisibility(View.VISIBLE);
                        send.setVisibility(View.VISIBLE);

                        textView10.setVisibility(View.INVISIBLE);
                        liczbaruchow.setVisibility(View.INVISIBLE);
                        textView27.setVisibility(View.INVISIBLE);
                        minproby.setVisibility(View.INVISIBLE);
                        textView28.setVisibility(View.INVISIBLE);

                        btnSelectTwoPhase.setVisibility(View.INVISIBLE);
                        btnLBL.setVisibility(View.INVISIBLE);
                        btnCFOP.setVisibility(View.INVISIBLE);
                        btnTwoPhaseExt.setVisibility(View.INVISIBLE);
                        btnMetaSolver.setVisibility(View.INVISIBLE);

                        textParametryLBL.setVisibility(View.INVISIBLE);
                        textNormalizeLBL.setVisibility(View.INVISIBLE);
                        checkBoxNormalizeLBL.setVisibility(View.INVISIBLE);

                        btnSelectTwoPhase.setBackgroundResource(R.drawable.selected_button_background);
                        btnLBL.setBackgroundResource(R.drawable.button2_background);
                        btnTwoPhaseExt.setBackgroundResource(R.drawable.button2_background);
                        btnCFOP.setBackgroundResource(R.drawable.button2_background);
                        btnMetaSolver.setBackgroundResource(R.drawable.button2_background);

                        graphic.setVisibility(View.VISIBLE);

                        if (result != null && !result.equals("Brak rozwiązania") && !result.startsWith("Błąd")) {
                            result = result.replaceAll("\\s+", " ").trim();
                            textView5.setText(result);

                            int numberOfMoves = result.isEmpty() ? 0 : result.split(" ").length;

                            showSolutionToast(
                                    "Znaleziono rozwiązanie\n"
                                            + "Czas generowania: " + duration + " ms\n"
                                            + "Liczba ruchów (HTM): " + numberOfMoves,
                                    5000
                            );
                        } else {
                            textView5.setText(result == null ? "" : result);
                            showSolutionToast("Nie znaleziono rozwiązania", 2000);
                        }

                        clearcube();
                        saveData();

                        solve.setEnabled(true);
                    });

                }).start();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity5.class);
                intent.putExtra("message_key", result);
                startActivity(intent);
            }
        });

        graphic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
                intent.putExtra("message_key", result);
                startActivity(intent);
            }
        });

        btnSelectTwoPhase.setOnClickListener(v -> {
            selectedAlgorithm = 1;
            setButtonSelection(btnSelectTwoPhase);
            setTwoPhaseParamsVisible(true);
            setLBLParamsVisible(false);
        });

        btnLBL.setOnClickListener(v -> {
            selectedAlgorithm = 2;
            setButtonSelection(btnLBL);
            setTwoPhaseParamsVisible(false);
            setLBLParamsVisible(true);
        });

        btnCFOP.setOnClickListener(v -> {
            selectedAlgorithm = 3;
            setButtonSelection(btnCFOP);
            setTwoPhaseParamsVisible(false);
            setLBLParamsVisible(false);
        });

        btnTwoPhaseExt.setOnClickListener(v -> {
            selectedAlgorithm = 4;
            setButtonSelection(btnTwoPhaseExt);
            setTwoPhaseParamsVisible(false);
            setLBLParamsVisible(false);
        });

        btnMetaSolver.setOnClickListener(v -> {
            selectedAlgorithm = 5;
            setButtonSelection(btnMetaSolver);
            setTwoPhaseParamsVisible(false);
            setLBLParamsVisible(false);
        });

        loadData();
    }

    private void setButtonSelection(Button selected) {
        btnSelectTwoPhase.setBackgroundResource(R.drawable.button2_background);
        btnLBL.setBackgroundResource(R.drawable.button2_background);
        btnCFOP.setBackgroundResource(R.drawable.button2_background);
        btnTwoPhaseExt.setBackgroundResource(R.drawable.button2_background);
        btnMetaSolver.setBackgroundResource(R.drawable.button2_background);

        selected.setBackgroundResource(R.drawable.selected_button_background);
    }

    private void setTwoPhaseParamsVisible(boolean visible) {
        int state = visible ? View.VISIBLE : View.INVISIBLE;
        textView28.setVisibility(state);
        textView10.setVisibility(state);
        liczbaruchow.setVisibility(state);
        textView27.setVisibility(state);
        minproby.setVisibility(state);
    }

    private void setLBLParamsVisible(boolean visible) {
        int state = visible ? View.VISIBLE : View.INVISIBLE;
        textParametryLBL.setVisibility(state);
        textNormalizeLBL.setVisibility(state);
        checkBoxNormalizeLBL.setVisibility(state);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MainActivity2.this, CameraXActivity.class);
                cameraXLauncher.launch(intent);
            } else {
                showToast("Brak uprawnień do używania aparatu", 3000);
            }
        }
    }

    private void showSolutionToast(String text, int durationMs) {
        View layout = getLayoutInflater().inflate(R.layout.toast_solution, null);
        TextView tv = layout.findViewById(R.id.toastText);
        tv.setText(text);

        Toast toast = new Toast(this);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

        new Handler().postDelayed(toast::cancel, durationMs);
    }

    private int getColorFromButton(Button btn) {
        if (btn.getBackground() instanceof android.graphics.drawable.ColorDrawable) {
            return ((android.graphics.drawable.ColorDrawable) btn.getBackground()).getColor();
        }
        return 0xFF000000;
    }

    private void applyStickerStyle(Button btn) {
        String t = btn.getText().toString();

        switch (t) {
            case "U":
                btn.setTextColor(0xFFB8BBB9);
                btn.setBackgroundResource(R.color.white);
                break;
            case "R":
                btn.setTextColor(0xFFE60A0A);
                btn.setBackgroundResource(R.color.red);
                break;
            case "F":
                btn.setTextColor(0xFF76FF03);
                btn.setBackgroundResource(R.color.green);
                break;
            case "L":
                btn.setTextColor(0xFFFF9100);
                btn.setBackgroundResource(R.color.orange);
                break;
            case "B":
                btn.setTextColor(0xFF00B0FF);
                btn.setBackgroundResource(R.color.blue);
                break;
            case "D":
                btn.setTextColor(0xFFFFEA00);
                btn.setBackgroundResource(R.color.yellow);
                break;
            default:
                btn.setBackgroundResource(R.color.black);
                btn.setTextColor(Color.WHITE);
                break;
        }
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, textView5.getText().toString());
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    private void setupUIViews() {
        white = findViewById(R.id.button);
        red = findViewById(R.id.button2);
        green = findViewById(R.id.button3);
        orange = findViewById(R.id.button4);
        blue = findViewById(R.id.button5);
        yellow = findViewById(R.id.button6);
        divider = findViewById(R.id.divider);
        send = findViewById(R.id.button11);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        c9 = findViewById(R.id.c9);
        next = findViewById(R.id.next);
        reset = findViewById(R.id.reset);
        lewo = findViewById(R.id.lewo);
        prawo = findViewById(R.id.prawo);
        dol = findViewById(R.id.dol);
        gora = findViewById(R.id.button8);
        solve = findViewById(R.id.solve);
        graphic = findViewById(R.id.button12);

        liczbaruchow = findViewById(R.id.liczbaruchow);
        minproby = findViewById(R.id.minproby);

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView10 = findViewById(R.id.textView10);
        textView27 = findViewById(R.id.textView27);
        textView28 = findViewById(R.id.textView28);

        capturedImage = findViewById(R.id.captured_image);
        captureButton = findViewById(R.id.capture_button);

        btnSelectTwoPhase = findViewById(R.id.btnSelectTwoPhase);
        btnLBL = findViewById(R.id.btnLBL);
        btnCFOP = findViewById(R.id.btnCFOP);
        btnTwoPhaseExt = findViewById(R.id.btnTwoPhaseExt);
        btnMetaSolver = findViewById(R.id.btnMetaSolver);

        checkBoxNormalizeLBL = findViewById(R.id.checkBoxNormalizeLBL);
        textNormalizeLBL = findViewById(R.id.textNormalizeLBL);
        textParametryLBL = findViewById(R.id.textParametryLBL);
    }

    private void choicecolor() {
        white.setOnClickListener(v -> setSelectedColor("white", divider, white, red, green, orange, blue, yellow));
        red.setOnClickListener(v -> setSelectedColor("red", divider, red, white, green, orange, blue, yellow));
        green.setOnClickListener(v -> setSelectedColor("green", divider, green, white, red, orange, blue, yellow));
        orange.setOnClickListener(v -> setSelectedColor("orange", divider, orange, white, red, green, blue, yellow));
        blue.setOnClickListener(v -> setSelectedColor("blue", divider, blue, white, red, green, orange, yellow));
        yellow.setOnClickListener(v -> setSelectedColor("yellow", divider, yellow, white, red, green, orange, blue));
    }

    private void setSelectedColor(String colorName, View divider, Button activeBtn, Button... inactiveBtns) {
        textView2.setText(colorName);
        switch (colorName) {
            case "white": divider.setBackgroundResource(R.color.white); break;
            case "red": divider.setBackgroundResource(R.color.red); break;
            case "green": divider.setBackgroundResource(R.color.green); break;
            case "orange": divider.setBackgroundResource(R.color.orange); break;
            case "blue": divider.setBackgroundResource(R.color.blue); break;
            case "yellow": divider.setBackgroundResource(R.color.yellow); break;
        }
        activeBtn.setText("✔");
        for (Button b : inactiveBtns) {
            b.setText("");
        }
    }

    private void cubesym(final Button btn) {
        btn.setOnClickListener(v -> {
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
        });
    }

    private void textcube() {
        col1 = getStickerLetter(c1);
        col2 = getStickerLetter(c2);
        col3 = getStickerLetter(c3);
        col4 = getStickerLetter(c4);
        col5 = getStickerLetter(c5);
        col6 = getStickerLetter(c6);
        colr7 = getStickerLetter(c7);
        col8 = getStickerLetter(c8);
        col9 = getStickerLetter(c9);
    }

    private String getStickerLetter(Button btn) {
        String val = btn.getText().toString();
        if ("U".equals(val) || "R".equals(val) || "F".equals(val) || "L".equals(val) || "B".equals(val) || "D".equals(val)) {
            return val;
        }
        return "!";
    }

    private void clearcube() {
        Button[] buttons = {c1, c2, c3, c4, c6, c7, c8, c9};
        for (Button b : buttons) {
            b.setText("");
            b.setBackgroundResource(R.color.black);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorTextViews[i][j].setText("");
            }
        }
    }

    private void reset() {
        reset.setOnClickListener(v -> {
            clearcube();
            textView3.setText("");
            textView2.setText("");
            divider.setBackgroundResource(R.color.clear);
            c5.setText("U");
            c5.setBackgroundResource(R.color.white);
            licznik = 0;
            textView4.setText("wprowadź stan górnej ściany");
            lewo.setBackgroundResource(R.color.orange);
            prawo.setBackgroundResource(R.color.red);
            dol.setBackgroundResource(R.color.green);
            gora.setBackgroundResource(R.color.blue);
            c5.setTextColor(0xFFB8BBB9);
            next.setClickable(true);
            setStickersClickable(true);

            white.setText(""); red.setText(""); green.setText("");
            orange.setText(""); blue.setText(""); yellow.setText("");

            textView5.setText("");
            solve.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            send.setVisibility(View.INVISIBLE);
            graphic.setVisibility(View.INVISIBLE);
            captureButton.setVisibility(View.VISIBLE);

            btnSelectTwoPhase.setVisibility(View.INVISIBLE);
            btnLBL.setVisibility(View.INVISIBLE);
            btnCFOP.setVisibility(View.INVISIBLE);
            btnTwoPhaseExt.setVisibility(View.INVISIBLE);
            btnMetaSolver.setVisibility(View.INVISIBLE);

            setTwoPhaseParamsVisible(false);
            setLBLParamsVisible(false);

            setButtonSelection(btnSelectTwoPhase);
            selectedAlgorithm = 1;
        });
    }

    private void setStickersClickable(boolean clickable) {
        c1.setClickable(clickable); c2.setClickable(clickable); c3.setClickable(clickable);
        c4.setClickable(clickable); c6.setClickable(clickable); c7.setClickable(clickable);
        c8.setClickable(clickable); c9.setClickable(clickable);
    }

    private void nextbutton() {
        next.setOnClickListener(v -> {
            licznik++;
            textcube();

            resultat = col1 + col2 + col3 + col4 + col5 + col6 + colr7 + col8 + col9;
            textView3.setText(textView3.getText().toString() + resultat);

            clearcube();

            switch (licznik) {
                case 1:
                    c5.setText("R"); c5.setBackgroundResource(R.color.red);
                    textView4.setText("wprowadź stan prawej ściany");
                    lewo.setBackgroundResource(R.color.green); prawo.setBackgroundResource(R.color.blue);
                    dol.setBackgroundResource(R.color.yellow); gora.setBackgroundResource(R.color.white);
                    c5.setTextColor(0xFFE60A0A);
                    break;
                case 2:
                    c5.setText("F"); c5.setBackgroundResource(R.color.green);
                    textView4.setText("wprowadź stan przedniej ściany");
                    lewo.setBackgroundResource(R.color.orange); prawo.setBackgroundResource(R.color.red);
                    dol.setBackgroundResource(R.color.yellow); gora.setBackgroundResource(R.color.white);
                    c5.setTextColor(0xFF76FF03);
                    break;
                case 3:
                    c5.setText("D"); c5.setBackgroundResource(R.color.yellow);
                    textView4.setText("wprowadź stan dolnej ściany");
                    lewo.setBackgroundResource(R.color.orange); prawo.setBackgroundResource(R.color.red);
                    dol.setBackgroundResource(R.color.blue); gora.setBackgroundResource(R.color.green);
                    c5.setTextColor(0xFFFFEA00);
                    break;
                case 4:
                    c5.setText("L"); c5.setBackgroundResource(R.color.orange);
                    textView4.setText("wprowadź stan lewej ściany");
                    lewo.setBackgroundResource(R.color.blue); prawo.setBackgroundResource(R.color.green);
                    dol.setBackgroundResource(R.color.yellow); gora.setBackgroundResource(R.color.white);
                    c5.setTextColor(0xFFFF9100);
                    break;
                case 5:
                    c5.setText("B"); c5.setBackgroundResource(R.color.blue);
                    textView4.setText("wprowadź stan tylnej ściany");
                    lewo.setBackgroundResource(R.color.red); prawo.setBackgroundResource(R.color.orange);
                    dol.setBackgroundResource(R.color.yellow); gora.setBackgroundResource(R.color.white);
                    c5.setTextColor(0xFF00B0FF);
                    break;
                default:
                    c5.setText(""); c5.setBackgroundResource(R.color.black);
                    textView4.setText("dzięki za wprowadzenie stanu kostki");
                    lewo.setBackgroundResource(R.color.white); prawo.setBackgroundResource(R.color.white);
                    dol.setBackgroundResource(R.color.white); gora.setBackgroundResource(R.color.white);
                    next.setClickable(false);
                    setStickersClickable(false);

                    solve.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.INVISIBLE);
                    send.setVisibility(View.INVISIBLE);
                    captureButton.setVisibility(View.GONE);

                    btnSelectTwoPhase.setVisibility(View.VISIBLE);
                    btnLBL.setVisibility(View.VISIBLE);
                    btnCFOP.setVisibility(View.VISIBLE);
                    btnTwoPhaseExt.setVisibility(View.VISIBLE);
                    btnMetaSolver.setVisibility(View.VISIBLE);

                    setButtonSelection(btnSelectTwoPhase);
                    setTwoPhaseParamsVisible(true);
                    selectedAlgorithm = 1;
                    break;
            }
        });
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
                int startX = j * width + margin;
                int endX = (j + 1) * width - margin;
                int startY = i * height + margin;
                int endY = (i + 1) * height - margin;

                int r = 0, g = 0, b = 0, count = 0;
                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        int pixel = bitmap.getPixel(x, y);
                        r += Color.red(pixel);
                        g += Color.green(pixel);
                        b += Color.blue(pixel);
                        count++;
                    }
                }

                int averageColor = Color.rgb(r / count, g / count, b / count);
                String colorName = getColorName(averageColor);

                colorTextViews[i][j].setText(colorName);
            }
        }
    }

    private String getColorName(int pixelColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(pixelColor, hsv);

        float hue = hsv[0];
        float saturation = hsv[1];
        float value = hsv[2];

        if (value > 0.45 && saturation < 0.3) return "U";
        else if (hue >= 40 && hue < 70) return "D";
        else if (hue >= 70 && hue < 165) return "F";
        else if (hue < 7 || hue >= 300) return "R";
        else if (hue >= 165 && hue < 300) return "B";
        else if (hue >= 7 && hue < 40) return "L";
        else return "!";
    }

    private String solveSelectedAlgorithm(String scrambledCube, int maxMoves, int minProbe) {
        switch (selectedAlgorithm) {
            case 1: // TwoPhase Standard
                return new Search().solution(scrambledCube, maxMoves, 100000000, minProbe, 0);

            case 2: { // LBL
                String lblResult = LblSolverFacade.solveFromURFDLB(scrambledCube);
                if (checkBoxNormalizeLBL.isChecked()) {
                    lblResult = MoveNormalizer.normalize(lblResult);
                }
                return lblResult;
            }

            case 3: { // CFOP
                String cfopReport = com.example.kostkav3.solver.cfop.CfopSolver.solveFromURFDLB(scrambledCube);
                return com.example.kostkav3.solver.cfop.CfopSolver.extractMovesFromReport(cfopReport);
            }

            case 4: // TwoPhase Extended
                return new Search().solution(scrambledCube, 25, 100000000, 100000, 0);

            case 5: // Hardware-Aware MetaSolver
                return solveHardwareOptimalMetaSolver(scrambledCube);

            default:
                return "Nie wybrano algorytmu";
        }
    }

    // ZAAWANSOWANY METASOLVER (RÓŻNORODNE PRÓBY I STRATEGIE PRZESZUKIWANIA)
    private static class SolutionCandidate {
        String strategyName;
        String solution;
        int moveCount;            // HTM (Half-Turn Metric)
        int parallelBatches;      // Liczba wygenerowanych cykli podwójnych
        double estimatedTimeMs;   // Szacowany czas fizyczny wykonania na robocie w ms
        long generationTimeMs;    // Czas generowania w ms
    }

    private String solveHardwareOptimalMetaSolver(String scrambledCube) {
        List<SolutionCandidate> candidates = new ArrayList<>();
        StringBuilder logSb = new StringBuilder();

        logSb.append("\n=================== METASOLVER (STRATEGIE DIVERSIFICATION) ===================\n");

        Search search = new Search();

        // Strategie o skrajnie różnych limitach minProbe i głębokości:
        // Szybkie pętle (minProbe = 10, 20) dają dłuższą sekwencję (22-24 ruchy), ale z DUŻĄ ilością ruchów równoległych!
        // Głębokie pętle (minProbe = 2000, 5000) dają krótką sekwencję (20-21 ruchów), ale z MAŁĄ ilością ruchów równoległych!
        int[][] strategyParams = {
                {24, 10, 0},    // 0: Fast Direct (Przerwanie na 1. dopasowaniu -> wysoka równoległość)
                {22, 100, 0},   // 1: Medium Direct
                {21, 5000, 0},  // 2: Deep Direct (Optymalizm HTM)
                {24, 10, 2},    // 3: Fast Inverse (Przerwanie odwrotne)
                {22, 100, 2},   // 4: Medium Inverse
                {21, 5000, 2}   // 5: Deep Inverse (Optymalizm HTM odwrotny)
        };

        String[] strategyNames = {
                "Fast Direct (Wysoka Równoległość)",
                "Medium Direct",
                "Deep Direct (Optimum HTM)",
                "Fast Inverse (Wysoka Równoległość)",
                "Medium Inverse",
                "Deep Inverse (Optimum HTM)"
        };

        for (int k = 0; k < strategyParams.length; k++) {
            try {
                long start = System.currentTimeMillis();

                int maxM = strategyParams[k][0];
                int prb = strategyParams[k][1];
                int opt = strategyParams[k][2];

                // Wywołanie solvera z daną strategią
                String rawSolution = search.solution(scrambledCube, maxM, 100000000, prb, opt);
                long genTime = System.currentTimeMillis() - start;

                // Rygorystyczna weryfikacja poprawności
                if (isValidSolution(rawSolution)) {
                    rawSolution = rawSolution.replaceAll("\\s+", " ").trim();

                    // Odrzucenie duplikatów sekwencji
                    boolean isDuplicate = false;
                    for (SolutionCandidate existing : candidates) {
                        if (existing.solution.equals(rawSolution)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (!isDuplicate) {
                        SolutionCandidate candidate = evaluateHardwareCost(rawSolution, strategyNames[k], genTime);
                        candidates.add(candidate);
                    }
                }
            } catch (Exception e) {
                Log.e("META_SOLVER", "Błąd dla strategii " + strategyNames[k], e);
            }
        }

        if (candidates.isEmpty()) {
            return "Brak rozwiązania";
        }

        // Wypisanie unikalnych wyników do Logcat
        for (SolutionCandidate c : candidates) {
            logSb.append(String.format("Strategia: %-32s | GenTime: %3dms | HTM: %2d | Pary Równoległe: %d | Czas T_exec: %6.1f ms\n",
                    c.strategyName, c.generationTimeMs, c.moveCount, c.parallelBatches, c.estimatedTimeMs));
            logSb.append("   -> Sekwencja: ").append(c.solution).append("\n");
        }

        // Wybór rozwiązania o NAJKRÓTSZYM CZASIE FIZYCZNYM ROBOTA
        SolutionCandidate bestCandidate = candidates.get(0);
        for (SolutionCandidate c : candidates) {
            if (c.estimatedTimeMs < bestCandidate.estimatedTimeMs) {
                bestCandidate = c;
            }
        }

        logSb.append("---------------------------------------------------------------------------------------------------\n");
        logSb.append(String.format("WYBRANO NAJLEPSZE DLA ROBOTA: Strategia=%s | HTM=%d | Pary Równoległe=%d | Czas T_exec=%.1f ms\n",
                bestCandidate.strategyName, bestCandidate.moveCount, bestCandidate.parallelBatches, bestCandidate.estimatedTimeMs));
        logSb.append("================================================================================-------------------\n");

        Log.d("META_ROBOT_LOG", logSb.toString());

        return bestCandidate.solution;
    }

    /**
     * Sprawdza, czy wynik solvera jest poprawnym ciągiem ruchów kostki
     */
    private boolean isValidSolution(String sol) {
        if (sol == null || sol.trim().isEmpty()) return false;
        if (sol.contains("Error") || sol.contains("Brak") || sol.contains("błąd")) return false;

        String[] moves = sol.trim().split("\\s+");
        for (String m : moves) {
            if (!m.matches("^[RLUDFB][2']?$")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Zcalibrowany model matematyczny T_exec odpowiadający fizycznym pomiarom robota
     */
    private SolutionCandidate evaluateHardwareCost(String solution, String strategyName, long genTime) {
        SolutionCandidate candidate = new SolutionCandidate();
        candidate.strategyName = strategyName;
        candidate.solution = solution;
        candidate.generationTimeMs = genTime;

        String[] moves = solution.split(" +");
        candidate.moveCount = moves.length;

        // ZMIERZONE STAŁE CZASOWE SPRZĘTU (w ms):
        final double T_QUARTER = 130.0; // Średni czas obrotu o 90° = 0,13 s
        final double T_HALF = 230.0;    // Średni czas obrotu o 180° = 0,23 s
        final double T_PAUSE = 5.0;     // Przerwa między kolejnymi grupami = 0,006 s

        double totalTimeMs = 0.0;
        int parallelCount = 0;

        int i = 0;
        while (i < moves.length) {

            // Jeżeli dwa kolejne ruchy dotyczą przeciwległych ścian,
            // mogą zostać wykonane współbieżnie.
            if (i + 1 < moves.length && areOppositeFaces(moves[i], moves[i + 1])) {

                double time1 = moves[i].contains("2") ? T_HALF : T_QUARTER;
                double time2 = moves[i + 1].contains("2") ? T_HALF : T_QUARTER;

                // Czas grupy współbieżnej wyznacza ruch trwający dłużej.
                totalTimeMs += Math.max(time1, time2);

                parallelCount++;
                i += 2;

            } else {

                // Pojedynczy ruch tworzy osobną grupę wykonawczą.
                totalTimeMs += moves[i].contains("2") ? T_HALF : T_QUARTER;
                i++;
            }

            // Przerwa występuje pomiędzy kolejnymi grupami wykonawczymi.
            if (i < moves.length) {
                totalTimeMs += T_PAUSE;
            }
        }

        candidate.estimatedTimeMs = totalTimeMs;
        candidate.parallelBatches = parallelCount;

        return candidate;
    }

    private boolean areOppositeFaces(String m1, String m2) {
        char s1 = m1.charAt(0);
        char s2 = m2.charAt(0);

        return (s1 == 'R' && s2 == 'L') || (s1 == 'L' && s2 == 'R') ||
                (s1 == 'U' && s2 == 'D') || (s1 == 'D' && s2 == 'U') ||
                (s1 == 'F' && s2 == 'B') || (s1 == 'B' && s2 == 'F');
    }

    private void showToast(String message, int duration) {
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(toast::cancel, duration);
    }
}