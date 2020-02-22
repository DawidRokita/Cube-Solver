package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity5 extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        String result = getIntent().getExtras().getString("message_key");
        setupUIViews();

        textView.setText(result);
    }

    private void setupUIViews(){
        textView = (TextView) findViewById(R.id.textView6);
    }

}
