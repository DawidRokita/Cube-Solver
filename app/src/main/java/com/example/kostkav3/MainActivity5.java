package com.example.kostkav3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainActivity5 extends AppCompatActivity {

    private String part;
    private String[] cutstr;
    public Button button, lista, polacz;
    private TextView textView, textView2;
    public ListView listView;
    public BluetoothAdapter btAdapter;
    public BluetoothDevice btDevice;
    public BluetoothSocket btSocket;

    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID //nie trzeba zmieniac
    public static final String SERVICE_ADDRESS = "00:19:10:08:C3:22"; // HC-05 BT ADDRESS TUTAJ JEST ADRES HC-05 D.R.1


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
        btDevice = btAdapter.getRemoteDevice(SERVICE_ADDRESS);


         polacz.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
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
                if(btSocket != null) {

                    try{
                        OutputStream out = btSocket.getOutputStream();
                        out.write((textView.getText().toString() + "\r\n").getBytes());
                        Toast.makeText(MainActivity5.this, "Wysłano", Toast.LENGTH_SHORT).show();
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
                String[] strings = new String[bt.size()];
                int index = 0;

                if(bt.size()>0){
                    for(BluetoothDevice device : bt){
                        strings[index] = device.getName() + " " + device.getAddress();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, strings);
                    listView.setAdapter(arrayAdapter);
                }
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

            Toast.makeText(MainActivity5.this, "Połączono", Toast.LENGTH_SHORT).show();
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
        button = (Button) findViewById(R.id.button13);
        lista = (Button) findViewById(R.id.lista);
        polacz = (Button) findViewById(R.id.button15);
        listView = (ListView) findViewById(R.id.ListView);
    }

}
