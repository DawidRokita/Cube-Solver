package com.example.kostkav3.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

import com.example.kostkav3.R;
import com.example.kostkav3.bluetooth.BluetoothManager;

import java.io.IOException;
import java.util.Set;

public class MainActivity5 extends AppCompatActivity {

    private String part;
    private String[] cutstr;

    public Button button, lista, polacz;
    private TextView textView, textView2, textView34;
    public static TextView czas_tb; // aktualizowany przez BluetoothManager
    public ListView listView;

    public BluetoothAdapter btAdapter;
    public String[] strings;

    public static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID

    int licznik = 0;

    // ===== Android 12+ Bluetooth runtime perms =====
    private ActivityResultLauncher<String[]> btPermsLauncher;
    private Runnable pendingBtAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        String result = getIntent().getExtras().getString("message_key");

        setupUIViews();

        // zamiana ruchów na format dla robota
        textView2.setText(result);
        String str = textView2.getText().toString();
        cutstr = str.split(" +");
        textView.setText("");
        for (int i = 0; i < cutstr.length; i++) {
            part = cutstr[i];
            switch (part) {
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
                default: break;
            }
        }
        textView.setText(textView.getText().toString() + "E");

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
                listView.setVisibility(View.INVISIBLE);
                licznik = 0;

                if (btAdapter == null) {
                    showToast("Bluetooth nie dostępny", 1200);
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

                BluetoothDevice btDevice = btAdapter.getRemoteDevice(addr);

                try {
                    BluetoothManager.getInstance().connect(btDevice);
                    showToast("Połączono", 1000);
                } catch (IOException e) {
                    showToast("Nie połączono", 1200);
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!ensureBtConnectAndScan(action)) return;
            }
            action.run();
        });

        button.setOnClickListener(v -> {
            Runnable action = () -> {
                try {
                    BluetoothManager.getInstance().sendMessage(textView.getText().toString());
                    showToast("Wysłano", 1000);
                } catch (IOException e) {
                    showToast("Nie wysłano", 1200);
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!ensureBtConnectAndScan(action)) return;
            }
            action.run();
        });
    }

    private void implementListeners() {
        lista.setOnClickListener(v -> {
            Runnable action = () -> {
                if (licznik == 0) {
                    listView.setVisibility(View.VISIBLE);
                    licznik = 1;
                } else {
                    listView.setVisibility(View.INVISIBLE);
                    licznik = 0;
                }

                if (btAdapter == null) {
                    showToast("Bluetooth nie dostępny", 1200);
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_selectable_list_item,
                            strings
                    );
                    listView.setAdapter(arrayAdapter);
                } else {
                    showToast("Brak sparowanych urządzeń", 1500);
                }
            };

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

    private void setupUIViews() {
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
        new Handler().postDelayed(toast::cancel, duration);
    }
}
