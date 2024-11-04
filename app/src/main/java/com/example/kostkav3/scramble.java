package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class scramble extends AppCompatActivity {

    private String part, scrambStr, prevpart;
    private String[] cutstr;
    public Button button, lista, polacz, solve;
    private TextView textView, textView34, textView9;
    public ListView listView;
    public BluetoothAdapter btAdapter;
    public BluetoothDevice btDevice;
    public BluetoothSocket btSocket;
    private String[] ruchy = {"R", "L", "U", "D", "F", "B", "R\'", "L\'", "U\'", "D\'", "F\'", "B\'"};

    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID
    String[] strings;
    String scrambledCube, result, scramble = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scramble);

        setupUIViews();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        implementListeners();

        polacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btDevice = btAdapter.getRemoteDevice(textView34.getText().toString());
                if (btAdapter == null) {
                    showToast("bluetooth nie dostępny", 1000);
                } else {
                    if (!btAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, 3);
                    } else {
                        ConnectThread connectThread = new ConnectThread(btDevice);
                        connectThread.start();
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");

                prevpart = "";
                scramble = "";
                for (int i = 0; i < 40; i++) {
                    int random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];


                    if(part == prevpart){   //nie wybieraj dwa razy pod rząd tego samego ruchu
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }

                    //nie cofaj poprzedniego ruchu (jeśli jest ruch R to kolejny nie może być R')
                    if((part == "R" && prevpart == "R\'") || (part == "R\'" && prevpart == "R")){
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }
                    if((part == "L" && prevpart == "L\'") || (part == "L\'" && prevpart == "L")){
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }
                    if((part == "U" && prevpart == "U\'") || (part == "U\'" && prevpart == "U")){
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }
                    if((part == "D" && prevpart == "D\'") || (part == "D\'" && prevpart == "D")){
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }
                    if((part == "F" && prevpart == "F\'") || (part == "F\'" && prevpart == "F")){
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }
                    if((part == "B" && prevpart == "B\'") || (part == "B\'" && prevpart == "B")){
                        random = (int) (Math.random() * ruchy.length);
                        part = ruchy[random];
                    }

                    prevpart = part;

                    if(i == 39){ //do ostatniego ruchu nie dodawaj spacji
                        scramble = scramble + part;
                    }else{
                        scramble = scramble + part + " ";
                    }

                    i++;
                }

                textView9.setText(scramble);

                cutstr = scramble.split(" +");
                textView.setText("");
                for(int j=0; j<cutstr.length; j++){
                    part = cutstr[j];
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

                if (btSocket != null) {
                    try {
                        OutputStream out = btSocket.getOutputStream();
                        out.write((textView.getText().toString() + "\r\n").getBytes());
                        showToast("Wysłano", 1000);
                    } catch (IOException e) {
                        // Handle the exception
                    }
                }
            }
        });

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrambStr = textView.getText().toString();
                String rev = "";
                char[] chars = scrambStr.toCharArray();
                int len = chars.length;

                textView9.setText("");
                getScrambledCube(scramble);
                simpleSolve(scrambledCube, 21, 500);
                textView9.setText(result);

                cutstr = result.split(" +");
                textView.setText("");
                for(int j=0; j<cutstr.length; j++){
                    part = cutstr[j];
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

                if (btSocket != null) {
                    try {
                        OutputStream out = btSocket.getOutputStream();
                        out.write((textView.getText().toString() + "\r\n").getBytes());
                        showToast("Wysłano", 1000);
                    } catch (IOException e) {
                        showToast("Błąd wysłania", 1000);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectBluetooth(); // rozłącz z bluetooth po wyjściu
    }

    private void disconnectBluetooth() {
        if (btSocket != null) {
            try {
                btSocket.close();
                btSocket = null;
                showToast("Rozłączono", 1000);
            } catch (IOException e) {
                Log.e("TEST", "Nie da się rozłączyć");
            }
        }
    }

    int licznik = 0;

    private void implementListeners() {
        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (licznik == 0) {
                    listView.setVisibility(View.VISIBLE);
                    licznik = 1;
                } else {
                    listView.setVisibility(View.INVISIBLE);
                    licznik = 0;
                }

                Set<BluetoothDevice> bt = btAdapter.getBondedDevices();
                strings = new String[bt.size()];
                int index = 0;

                if (bt.size() > 0) {
                    for (BluetoothDevice device : bt) {
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

    private class ConnectThread extends Thread {
        private final BluetoothSocket thisSocket;
        private final BluetoothDevice thisDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            thisDevice = device;
            try {
                tmp = thisDevice.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_ID));
            } catch (IOException e) {
                Log.e("TEST", "Can't create socket");
            }
            thisSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            btAdapter.cancelDiscovery();

            try {
                thisSocket.connect();
                Log.d("TESTING", "Connected to Bluetooth device");

                // Show the "Połączono" message after successful connection
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Połączono", 1000);
                    }
                });
            } catch (IOException connectException) {
                try {
                    thisSocket.close();
                } catch (IOException closeException) {
                    Log.e("TEST", "Can't close socket");
                }
                return;
            }

            btSocket = thisSocket;
        }

        public void cancel() {
            try {
                thisSocket.close();
            } catch (IOException e) {
                Log.e("TEST", "Can't close socket");
            }
        }
    }

    private void setupUIViews() {
        textView = findViewById(R.id.textView6);
        textView34 = findViewById(R.id.textView32);
        button = findViewById(R.id.button13);
        lista = findViewById(R.id.lista);
        polacz = findViewById(R.id.button15);
        listView = findViewById(R.id.ListView);
        solve = findViewById(R.id.button17);
        textView9 = findViewById(R.id.textView9);
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

    public void getScrambledCube(String scramble) {
        scrambledCube = Tools.fromScramble(scramble);
    }

    public void simpleSolve(String scrambledCube, int maxMoves, int minProbe) {
        result = new Search().solution(scrambledCube, maxMoves, 100000000, minProbe, 0);
    }



}
