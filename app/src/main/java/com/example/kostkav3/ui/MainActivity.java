package com.example.kostkav3.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kostkav3.R;

public class MainActivity extends AppCompatActivity {

    private Button solve,info,instuct, scramble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        widgety();

        //przejscie do innego activity
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

        instuct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });

        scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openscramble();
            }
        });


    }

    private void widgety(){
        solve = (Button) findViewById(R.id.button7);
        instuct = (Button) findViewById(R.id.button9);
        info = (Button) findViewById(R.id.button10);
        scramble = (Button) findViewById(R.id.button14);
    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void openActivity3(){
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    public void openActivity4(){
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }

    public void openscramble(){
        Intent intent = new Intent(this, scramble.class);
        startActivity(intent);
    }


}
