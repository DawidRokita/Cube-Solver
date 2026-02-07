package com.example.kostkav3.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.kostkav3.R;
import com.example.kostkav3.bluetooth.BluetoothManager;

import java.io.IOException;
import java.util.Set;

public class MainActivity5 extends AppCompatActivity {

    private String part;
    private String[] cutstr;
    public Button button, lista, polacz;
    private TextView textView, textView2, textView34;
    public static TextView czas_tb;  // To jest globalny TextView, zaktualizowany przez BluetoothManager
    public ListView listView;
    public BluetoothAdapter btAdapter;

    public String[] strings;

    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        String result = getIntent().getExtras().getString("message_key");
        setupUIViews();

        textView2.setText(result);
        String str = textView2.getText().toString();
        cutstr = str.split(" +");
        textView.setText("");
        for(int i=0; i<cutstr.length; i++){
            part = cutstr[i];
            switch (part){
                case "R":   textView.setText(textView.getText().toString() + "R");   break;
                case "U":   textView.setText(textView.getText().toString() + "U");   break;
                case "L":   textView.setText(textView.getText().toString() + "L");   break;
                case "D":   textView.setText(textView.getText().toString() + "D");   break;
                case "F":   textView.setText(textView.getText().toString() + "F");   break;
                case "B":   textView.setText(textView.getText().toString() + "B");   break;
                case "R\'": textView.setText(textView.getText().toString() + "r");   break;
                case "U\'": textView.setText(textView.getText().toString() + "u");   break;
                case "L\'": textView.setText(textView.getText().toString() + "l");   break;
                case "D\'": textView.setText(textView.getText().toString() + "d");   break;
                case "F\'": textView.setText(textView.getText().toString() + "f");   break;
                case "B\'": textView.setText(textView.getText().toString() + "b");   break;
                case "R2":  textView.setText(textView.getText().toString() + "RR");  break;
                case "U2":  textView.setText(textView.getText().toString() + "UU");  break;
                case "L2":  textView.setText(textView.getText().toString() + "LL");  break;
                case "D2":  textView.setText(textView.getText().toString() + "DD");  break;
                case "F2":  textView.setText(textView.getText().toString() + "FF");  break;
                case "B2":  textView.setText(textView.getText().toString() + "BB");  break;
                default:    break;
            }
        }

        textView.setText(textView.getText().toString() + "E");

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        implementListeners();

        polacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.INVISIBLE);
                licznik = 0; // Resetuj licznik

                BluetoothDevice btDevice = btAdapter.getRemoteDevice(textView34.getText().toString());
                if(btAdapter == null) {
                    showToast("bluetooth nie dostępny", 1000);
                } else {
                    if(!btAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, 3);
                    }else {
                        try {
                            BluetoothManager.getInstance().connect(btDevice);
                            showToast("Połączono", 1000);
                        } catch (IOException e) {
                            showToast("Nie połączono", 1000);
                        }
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BluetoothManager.getInstance().sendMessage(textView.getText().toString());
                    showToast("Wysłano", 1000);
                } catch (IOException e) {
                    showToast("Nie wysłano", 1000);
                }
            }
        });
    }

    int licznik = 0;
    private void implementListeners() {
        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik == 0){
                    listView.setVisibility(View.VISIBLE);
                    licznik = 1;
                }else{
                    listView.setVisibility(View.INVISIBLE);
                    licznik = 0;
                }

                Set<BluetoothDevice> bt = btAdapter.getBondedDevices();
                strings = new String[bt.size()];
                int index = 0;

                if(bt.size() > 0){
                    for(BluetoothDevice device : bt){
                        strings[index] = device.getName() + "/" + device.getAddress();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = strings[position].toString();
                String[] parts = temp.split("/");
                textView34.setText(parts[1]);
            }
        });
    }

    private void setupUIViews(){
        textView = findViewById(R.id.textView6);
        textView2 = findViewById(R.id.textView31);
        textView34 = findViewById(R.id.textView32);
        czas_tb = findViewById(R.id.czas_tb);
        button = findViewById(R.id.button13);
        lista = findViewById(R.id.lista);
        polacz = findViewById(R.id.button15);
        listView = findViewById(R.id.ListView);
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
