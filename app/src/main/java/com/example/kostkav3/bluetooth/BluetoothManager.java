package com.example.kostkav3.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.kostkav3.ui.MainActivity5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {

    private BluetoothSocket socket;
    private InputStream inStream;
    private OutputStream outStream;

    private static BluetoothManager instance = null;

    private BluetoothManager() { }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    public void connect(BluetoothDevice device) throws IOException {
        BluetoothSocket tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(MainActivity5.SERVICE_ID));
        socket = tmp;
        socket.connect();
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();

        new ReceiveThread().start();  // Start receiving data
    }

    public void sendMessage(String message) throws IOException {
        if (socket != null && socket.isConnected()) {
            outStream.write((message + "\r\n").getBytes());
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String line;

            while (!isInterrupted()) {
                try {
                    line = reader.readLine();
                    if (line != null && line.startsWith("CZAS:")) {

                        Log.d("CZAS_DEBUG", "RAW Z ROBOTA: [" + line + "]");

                        String msString = line.replace("CZAS:", "").trim();
                        long totalMs = Long.parseLong(msString);

                        Log.d("CZAS_DEBUG", "ODEBRANE MS: " + totalMs);

                        String sformatowanyCzas = formatujCzas(totalMs);

                        Log.d("CZAS_DEBUG", "PO FORMATOWANIU: " + sformatowanyCzas);

                        MainActivity5.czas_tb.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity5.czas_tb.setText(sformatowanyCzas);
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.d("TEST", "Rozłączono ", e);
                    break;
                } catch (NumberFormatException e) {
                    Log.e("TEST", "Błąd formatu odebranych danych: " + e.getMessage());
                }
            }
        }


        private String formatujCzas(long totalMs) {
            if (totalMs < 1000) {
                return totalMs + " ms";
            }

            long minuty = totalMs / 60000;
            long sekundy = (totalMs % 60000) / 1000;
            long milisekundy = totalMs % 1000;
            long setne = milisekundy / 10;

            if (minuty == 0) {
                return String.format("%d.%02d s", sekundy, setne);
            } else {
                return String.format("%d:%02d.%02d", minuty, sekundy, setne);
            }
        }
    }
}