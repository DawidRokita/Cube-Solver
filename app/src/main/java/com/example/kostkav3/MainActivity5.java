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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity5 extends AppCompatActivity {

    private String part;
    private String[] cutstr;
    public Button button, lista, polacz;
    private TextView textView, textView2, textView34, czas_tb;
    public ListView listView;
    public BluetoothAdapter btAdapter;
    public BluetoothDevice btDevice;
    public BluetoothSocket btSocket;

    public double czas_sekundy;

    public int czas_liczba;

    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID //nie trzeba zmieniac
    //public static final String SERVICE_ADDRESS = "00:19:10:08:C3:22"; // HC-05 BT ADDRESS TUTAJ JEST ADRES HC-05 D.R.1
    String[] strings;

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
                listView.setVisibility(View.INVISIBLE); //schowaj liste z urzadzeniami bt
                licznik = 0; //ustaw licznik na 0 aby mozna bylo otworzyc liste bt

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
                if(btSocket != null) {
                    try{
                        OutputStream out = btSocket.getOutputStream();
                        out.write((textView.getText().toString() + "\r\n").getBytes());
                        Toast.makeText(MainActivity5.this, "Wysłano", Toast.LENGTH_SHORT).show();
                    }catch(IOException e) {
                        Toast.makeText(MainActivity5.this, "Błąd wysyłania", Toast.LENGTH_LONG).show();
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
                Log.e("TEST", "Nie da sie polaczyc");
            }
            thisSocket = tmp;

            Toast.makeText(MainActivity5.this, "Połączono", Toast.LENGTH_SHORT).show();
        }

        public void run() {

            btAdapter.cancelDiscovery();

            try {
                thisSocket.connect();
                Log.d("TESTING", "Connected");
                btSocket = thisSocket;
                // Start the data receiving thread
                ReceiveThread receiveThread = new ReceiveThread(btSocket);
                receiveThread.start();
            } catch (IOException connectException) {
                try {
                    thisSocket.close();
                } catch (IOException closeException) {
                    Log.e("TEST", "Can't close socket");
                }
                return;
            }
        }
    }

    private class ReceiveThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inStream;

        public ReceiveThread(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tmpIn = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e("TEST", "Error occurred when creating input stream", e);
            }
            inStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[2]; // Buffor dla odebranych bajtow
            int numBytes; // zmienna do ktorej wpisujemy bajty

            while (true) {
                try {
                    numBytes = inStream.read(buffer);   // czytaj dane z InputStream
                    if (numBytes == 2) {  // odbieramy tylko dwa bajty bo robot zawsze wysyla dwa bajty
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String receivedHexData = bytesToHex(buffer);    // konwersja odebranych bajtow do stringa hex
                                int receivedDecimalData = Integer.parseInt(receivedHexData, 16);    // konwersja hex do dec
                                czas_sekundy = receivedDecimalData/305.175781;  //zmiana odebranej liczby pulsow z robota na sekundy
                                czas_sekundy = czas_sekundy/100;
                                // zmiana obliczonego czasu na stinga z czasen z dokladnoscia do dwuch cyfr po pzecinku
                                String timeInSeconds = String.format("%.2f", czas_sekundy);
                                czas_tb.setText(timeInSeconds + " s");  // wyswietl czas w textView
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.d("TEST", "Rozlaczono ", e);
                    break;
                }
            }
        }

        // funkcja zamieniajaca bajty na hex
        private String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        }

    }

    private void setupUIViews(){
        textView = (TextView) findViewById(R.id.textView6);
        textView2 = (TextView) findViewById(R.id.textView31);
        textView34 = (TextView) findViewById(R.id.textView32);
        czas_tb = (TextView) findViewById(R.id.czas_tb);
        button = (Button) findViewById(R.id.button13);
        lista = (Button) findViewById(R.id.lista);
        polacz = (Button) findViewById(R.id.button15);
        listView = (ListView) findViewById(R.id.ListView);
    }
}
