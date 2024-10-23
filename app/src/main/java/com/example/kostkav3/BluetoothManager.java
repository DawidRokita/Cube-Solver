package com.example.kostkav3;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
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
            byte[] buffer = new byte[2];
            int numBytes;

            while (true) {
                try {
                    numBytes = inStream.read(buffer);
                    if (numBytes == 2) {
                        String receivedHexData = bytesToHex(buffer);
                        int receivedDecimalData = Integer.parseInt(receivedHexData, 16);
                        double czas_sekundy = receivedDecimalData / 305.175781;
                        czas_sekundy = czas_sekundy / 100;
                        String timeInSeconds = String.format("%.2f", czas_sekundy);

                        MainActivity5.czas_tb.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity5.czas_tb.setText(timeInSeconds + " s");
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.d("TEST", "Rozłączono ", e);
                    break;
                }
            }
        }

        private String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        }
    }
}
