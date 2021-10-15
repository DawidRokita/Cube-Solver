package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class scramble extends AppCompatActivity {

    private String part, scrambStr;
    private String[] cutstr;
    public Button button, lista, polacz, solve;
    private TextView textView, textView2, textView34;
    public ListView listView;
    public BluetoothAdapter btAdapter;
    public BluetoothDevice btDevice;
    public BluetoothSocket btSocket;
    private String[] ruchy = {"R", "L", "U", "D", "F", "B"};


    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID //nie trzeba zmieniac
    //public static final String SERVICE_ADDRESS = "00:19:10:08:C3:22"; // HC-05 BT ADDRESS TUTAJ JEST ADRES HC-05 D.R.1
    String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scramble);

        setupUIViews();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        implementListeners();



        //btDevice = btAdapter.getRemoteDevice(SERVICE_ADDRESS);


        polacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btDevice = btAdapter.getRemoteDevice(textView34.getText().toString());
                if(btAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth not available", Toast.LENGTH_LONG).show();
                } else {
                    if(!btAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, 3);
                    }else {
                        ConnectThread connectThread = new ConnectThread(btDevice);
                        connectThread.start();
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------------------generowanie scrambla--------------------
                textView.setText("");
                for(int i=0; i<40; i++){
                    int random = (int)(Math.random()*ruchy.length);
                    part = ruchy[random];
                    textView.setText(textView.getText().toString() + part);
                    i++;
                }
                textView.setText(textView.getText().toString() + "E");
                //--------------------------------------------------------------

                if(btSocket != null) {

                    try{
                        OutputStream out = btSocket.getOutputStream();
                        out.write((textView.getText().toString() + "\r\n").getBytes());
                        Toast.makeText(scramble.this, "Wysłano", Toast.LENGTH_SHORT).show();
                    }catch(IOException e) {
                        //Toast.makeText(MainActivity5.this, "Błąd wysyłania", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----------------------------generowanie rozwiązania dla scambla-------------
                scrambStr = textView.getText().toString();
                String rev = "";
                char[] chars = scrambStr.toCharArray();
                int len = chars.length;

                for(int i = len-2; i>=0; i--){
                    rev = rev + chars[i];
                }
                char[] reverse = rev.toCharArray();
                rev="";
                for(int i=0; i<reverse.length; i++){

                    switch (reverse[i]){
                        case 'R':   rev = rev + "r";   break;
                        case 'U':   rev = rev + "u";   break;
                        case 'L':   rev = rev + "l";   break;
                        case 'D':   rev = rev + "d";   break;
                        case 'F':   rev = rev + "f";   break;
                        case 'B':   rev = rev + "b";   break;
                        case 'E':   rev = rev + "E";   break;
                        default:    break;
                    }
                }
                textView.setText(rev + "E");
                //------------------------------------------------------------------------

                if(btSocket != null) {

                    try{
                        OutputStream out = btSocket.getOutputStream();
                        out.write((textView.getText().toString() + "\r\n").getBytes());
                        Toast.makeText(scramble.this, "Wysłano", Toast.LENGTH_SHORT).show();
                    }catch(IOException e) {
                        //Toast.makeText(MainActivity5.this, "Błąd wysyłania", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }
    int licznik = 0;
    private void implementListeners() {

        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licznik==0){
                    listView.setVisibility(View.VISIBLE);
                    licznik=1;
                }else{
                    listView.setVisibility(View.INVISIBLE);
                    licznik=0;
                }

                Set<BluetoothDevice> bt= btAdapter.getBondedDevices();
                strings = new String[bt.size()];
                int index = 0;

                if(bt.size()>0){
                    for(BluetoothDevice device : bt){
                        strings[index] = device.getName() + "/" + device.getAddress();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, strings);
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
                Log.e("TEST", "Can't connect to service");
            }
            thisSocket = tmp;

            Toast.makeText(scramble.this, "Połączono", Toast.LENGTH_SHORT).show();
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            btAdapter.cancelDiscovery();

            try {
                thisSocket.connect();
                Log.d("TESTING", "Connected to shit");
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

    private void setupUIViews(){
        textView = (TextView) findViewById(R.id.textView6);
        textView2 = (TextView) findViewById(R.id.textView31);
        textView34 = (TextView) findViewById(R.id.textView32);
        button = (Button) findViewById(R.id.button13);
        lista = (Button) findViewById(R.id.lista);
        polacz = (Button) findViewById(R.id.button15);
        listView = (ListView) findViewById(R.id.ListView);
        solve = (Button) findViewById(R.id.button17);
    }
}
