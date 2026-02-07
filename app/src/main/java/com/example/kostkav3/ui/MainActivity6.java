package com.example.kostkav3.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kostkav3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity6 extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerMoves;

    private final Map<String, Integer> moveToDrawable = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        showToast("Kostkę trzymaj tak aby środkowy kolor górnej ściany był biały a przedniej zielony", 5000);

        textView = findViewById(R.id.textView25);
        recyclerMoves = findViewById(R.id.recyclerMoves);

        initMap();

        String result = getIntent().getStringExtra("message_key");
        if (result == null) result = "";
        result = result.trim().replaceAll("\\s+", " ");

        textView.setText(result);

        String[] tokens = result.isEmpty() ? new String[0] : result.split(" ");

        List<Integer> icons = new ArrayList<>();
        for (String t : tokens) {
            Integer resId = moveToDrawable.get(t);
            if (resId != null) {
                icons.add(resId);
            } else {
                showToast("Nieznany ruch: " + t, 1500);
            }
        }

        int columns = 3;
        GridLayoutManager glm = new GridLayoutManager(this, columns);
        recyclerMoves.setLayoutManager(glm);


        MovesAdapter adapter = new MovesAdapter(icons, position -> {
            showToast("Krok: " + (position + 1), 800);
        });

        recyclerMoves.setAdapter(adapter);

        if (icons.isEmpty()) {
            showToast("Nie ma rozwiązania dla tego układu kostki", 2500);
        }
    }

    private void initMap() {
        // podstawowe
        moveToDrawable.put("R",  R.drawable.ruchr);
        moveToDrawable.put("U",  R.drawable.ruchu);
        moveToDrawable.put("L",  R.drawable.ruchl);
        moveToDrawable.put("D",  R.drawable.ruchd);
        moveToDrawable.put("F",  R.drawable.ruchf);
        moveToDrawable.put("B",  R.drawable.ruchb);

        // prim
        moveToDrawable.put("R'", R.drawable.ruchrprim);
        moveToDrawable.put("U'", R.drawable.ruchuprim);
        moveToDrawable.put("L'", R.drawable.ruchlprim);
        moveToDrawable.put("D'", R.drawable.ruchdprim);
        moveToDrawable.put("F'", R.drawable.ruchfprim);
        moveToDrawable.put("B'", R.drawable.ruchbprim);

        // podwójne
        moveToDrawable.put("R2", R.drawable.ruchrr);
        moveToDrawable.put("U2", R.drawable.ruchuu);
        moveToDrawable.put("L2", R.drawable.ruchll);
        moveToDrawable.put("D2", R.drawable.ruchdd);
        moveToDrawable.put("F2", R.drawable.ruchff);
        moveToDrawable.put("B2", R.drawable.ruchbb);
    }

    private void showToast(String message, int duration) {
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(toast::cancel, duration);
    }
}
