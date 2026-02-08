package com.example.kostkav3.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kostkav3.R;
import com.example.kostkav3.solver.twophase.Search;
import com.example.kostkav3.solver.twophase.Tools;

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

    private final String[] ruchy = {"R", "L", "U", "D", "F", "B", "R\'", "L\'", "U\'", "D\'", "F\'", "B\'"};

    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID

    String[] strings;
    String scrambledCube, result, scramble = "";

    int licznik = 0;

    // ===== Android 12+ Bluetooth runtime perms =====
    private ActivityResultLauncher<String[]> btPermsLauncher;
    private Runnable pendingBtAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scramble);

        setupUIViews();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        btPermsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                resultMap -> {
                    boolean connectOk = Boolean.TRUE.equals(resultMap.get(Manifest.permission.BLUETOOTH_CONNECT));
                    boolean scanOk = Boolean.TRUE.equals(resultMap.get(Manifest.permission.BLUETOOTH_SCAN));

                    if (connectOk && scanOk) {
                        if (pendingBtAction != null) pendingBtAction.run();
                    } else {
                        showToast("Brak uprawnień Bluetooth (CONNECT/SCAN)", 2500);
                    }
                    pendingBtAction = null;
                }
        );

        implementListeners();

        polacz.setOnClickListener(v -> {
            Runnable action = () -> {
                if (btAdapter == null) {
                    showToast("Bluetooth nie dostępny", 1500);
                    return;
                }

                if (!btAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, 3);
                    return;
                }

                String addr = textView34.getText().toString().trim();
                if (addr.isEmpty()) {
                    showToast("Wybierz urządzenie z listy", 1500);
                    return;
                }

                btDevice = btAdapter.getRemoteDevice(addr);
                new ConnectThread(btDevice).start();
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!ensureBtConnectAndScan(action)) return;
            }
            action.run();
        });

        button.setOnClickListener(v -> {
            textView.setText("");

            prevpart = "";
            scramble = "";

            // UWAGA: u Ciebie było "i++" w środku pętli - to skracało liczbę ruchów o połowę.
            // Zostawiam normalne 40 ruchów.
            for (int i = 0; i < 40; i++) {
                int random = (int) (Math.random() * ruchy.length);
                part = ruchy[random];

                // nie wybieraj dwa razy pod rząd tego samego ruchu
                if (part.equals(prevpart)) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }

                // nie cofaj poprzedniego ruchu (jeśli jest ruch R to kolejny nie może być R')
                if ((part.equals("R") && prevpart.equals("R\'")) || (part.equals("R\'") && prevpart.equals("R"))) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }
                if ((part.equals("L") && prevpart.equals("L\'")) || (part.equals("L\'") && prevpart.equals("L"))) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }
                if ((part.equals("U") && prevpart.equals("U\'")) || (part.equals("U\'") && prevpart.equals("U"))) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }
                if ((part.equals("D") && prevpart.equals("D\'")) || (part.equals("D\'") && prevpart.equals("D"))) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }
                if ((part.equals("F") && prevpart.equals("F\'")) || (part.equals("F\'") && prevpart.equals("F"))) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }
                if ((part.equals("B") && prevpart.equals("B\'")) || (part.equals("B\'") && prevpart.equals("B"))) {
                    random = (int) (Math.random() * ruchy.length);
                    part = ruchy[random];
                }

                prevpart = part;

                if (i == 39) scramble = scramble + part;
                else scramble = scramble + part + " ";
            }

            textView9.setText(scramble);

            cutstr = scramble.split(" +");
            textView.setText("");
            for (int j = 0; j < cutstr.length; j++) {
                part = cutstr[j];
                switch (part) {
                    case "R":   textView.setText(textView.getText().toString() + "R");  break;
                    case "U":   textView.setText(textView.getText().toString() + "U");  break;
                    case "L":   textView.setText(textView.getText().toString() + "L");  break;
                    case "D":   textView.setText(textView.getText().toString() + "D");  break;
                    case "F":   textView.setText(textView.getText().toString() + "F");  break;
                    case "B":   textView.setText(textView.getText().toString() + "B");  break;
                    case "R\'": textView.setText(textView.getText().toString() + "r");  break;
                    case "U\'": textView.setText(textView.getText().toString() + "u");  break;
                    case "L\'": textView.setText(textView.getText().toString() + "l");  break;
                    case "D\'": textView.setText(textView.getText().toString() + "d");  break;
                    case "F\'": textView.setText(textView.getText().toString() + "f");  break;
                    case "B\'": textView.setText(textView.getText().toString() + "b");  break;
                    case "R2":  textView.setText(textView.getText().toString() + "RR"); break;
                    case "U2":  textView.setText(textView.getText().toString() + "UU"); break;
                    case "L2":  textView.setText(textView.getText().toString() + "LL"); break;
                    case "D2":  textView.setText(textView.getText().toString() + "DD"); break;
                    case "F2":  textView.setText(textView.getText().toString() + "FF"); break;
                    case "B2":  textView.setText(textView.getText().toString() + "BB"); break;
                    default: break;
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
        });

        solve.setOnClickListener(v -> {
            scrambStr = textView.getText().toString();

            textView9.setText("");
            getScrambledCube(scramble);
            simpleSolve(scrambledCube, 21, 500);
            textView9.setText(result);

            cutstr = result.split(" +");
            textView.setText("");
            for (int j = 0; j < cutstr.length; j++) {
                part = cutstr[j];
                switch (part) {
                    case "R":   textView.setText(textView.getText().toString() + "R");  break;
                    case "U":   textView.setText(textView.getText().toString() + "U");  break;
                    case "L":   textView.setText(textView.getText().toString() + "L");  break;
                    case "D":   textView.setText(textView.getText().toString() + "D");  break;
                    case "F":   textView.setText(textView.getText().toString() + "F");  break;
                    case "B":   textView.setText(textView.getText().toString() + "B");  break;
                    case "R\'": textView.setText(textView.getText().toString() + "r");  break;
                    case "U\'": textView.setText(textView.getText().toString() + "u");  break;
                    case "L\'": textView.setText(textView.getText().toString() + "l");  break;
                    case "D\'": textView.setText(textView.getText().toString() + "d");  break;
                    case "F\'": textView.setText(textView.getText().toString() + "f");  break;
                    case "B\'": textView.setText(textView.getText().toString() + "b");  break;
                    case "R2":  textView.setText(textView.getText().toString() + "RR"); break;
                    case "U2":  textView.setText(textView.getText().toString() + "UU"); break;
                    case "L2":  textView.setText(textView.getText().toString() + "LL"); break;
                    case "D2":  textView.setText(textView.getText().toString() + "DD"); break;
                    case "F2":  textView.setText(textView.getText().toString() + "FF"); break;
                    case "B2":  textView.setText(textView.getText().toString() + "BB"); break;
                    default: break;
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

    // ====== LISTA URZĄDZEŃ (bonded devices) ======
    private void implementListeners() {
        lista.setOnClickListener(v -> {

            if (licznik == 0) {
                listView.setVisibility(View.VISIBLE);
                licznik = 1;
            } else {
                listView.setVisibility(View.INVISIBLE);
                licznik = 0;
            }

            Runnable action = this::showBondedDevices;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!ensureBtConnectAndScan(action)) return;
            }
            action.run();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String temp = strings[position];
            String[] parts = temp.split("/");
            textView34.setText(parts[1]);
        });
    }

    private void showBondedDevices() {
        if (btAdapter == null) {
            showToast("Bluetooth nie dostępny", 1500);
            return;
        }

        Set<BluetoothDevice> bt = btAdapter.getBondedDevices(); // wymaga CONNECT na Android 12+
        strings = new String[bt.size()];
        int index = 0;

        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                String name = device.getName();
                String addr = device.getAddress();
                strings[index] = (name == null ? "Unknown" : name) + "/" + addr;
                index++;
            }
            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, strings);
            listView.setAdapter(arrayAdapter);
        } else {
            showToast("Brak sparowanych urządzeń", 1500);
        }
    }

    // ===== Android 12+ permission helper =====
    private boolean ensureBtConnectAndScan(Runnable actionIfGranted) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true;

        boolean connectGranted = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        boolean scanGranted = checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;

        if (connectGranted && scanGranted) return true;

        pendingBtAction = actionIfGranted;
        btPermsLauncher.launch(new String[]{
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
        });
        return false;
    }

    // ====== CONNECT THREAD ======
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

        @Override
        public void run() {
            // cancelDiscovery wymaga BLUETOOTH_SCAN na Android 12+
            if (btAdapter != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                        checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                    btAdapter.cancelDiscovery();
                }
            }

            try {
                if (thisSocket == null) throw new IOException("Socket is null");
                thisSocket.connect();
                Log.d("TESTING", "Connected to Bluetooth device");

                runOnUiThread(() -> showToast("Połączono", 1000));

            } catch (IOException connectException) {
                try {
                    if (thisSocket != null) thisSocket.close();
                } catch (IOException closeException) {
                    Log.e("TEST", "Can't close socket");
                }
                runOnUiThread(() -> showToast("Nie udało się połączyć", 1500));
                return;
            }

            btSocket = thisSocket;
        }

        public void cancel() {
            try {
                if (thisSocket != null) thisSocket.close();
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

        new Handler().postDelayed(toast::cancel, duration);
    }

    public void getScrambledCube(String scramble) {
        scrambledCube = Tools.fromScramble(scramble);
    }

    public void simpleSolve(String scrambledCube, int maxMoves, int minProbe) {
        result = new Search().solution(scrambledCube, maxMoves, 100000000, minProbe, 0);
    }
}
