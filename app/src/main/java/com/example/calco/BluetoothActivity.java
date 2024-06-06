package com.example.calco;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.viewmodel.activity.BluetoothVM;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothVM bluetoothVM;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private boolean btEnabled = false;
    private boolean btScan = false;
    private boolean btConn = false;
    private boolean btAdv = false;
    private boolean fineLoc = false;
    private boolean coarseLoc = false;
    private boolean btOk = false;
    private boolean locationEnabled = false;

    private static final int REQUEST_ENABLE_BT = 43;
    private static final int REQUEST_CONNECT_BT = 44;
    private static final int REQUEST_SCAN_BT = 45;
    private static final int REQUEST_ADV_BT = 46;
    private static final int REQUEST_FINE_LOC = 47;
    private static final int REQUEST_COARSE_LOC = 48;
    private static final int REQUEST_LOCATION = 49;

    private BroadcastReceiver bluetoothDeviceSearcher = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("Bluetooth device found: " + device.getName() + " " + device.getAddress());
                bluetoothVM.addDevice(device);
            }

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("Discovery finished");
                bluetoothVM.removeDevice(device);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bluetooth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bluetoothVM = new ViewModelProvider(this).get(BluetoothVM.class);
        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        setListAdapter();
        askForPermission();
        askForLocation();


        if (btOk) {
            setUp();
            setHandlers();
            addAlreadyPairedDevices();
        }
    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Bluetooth connect is not granted");
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CONNECT_BT);
            }
            else{
                btConn = true;
            }
            System.out.println("Bluetooth connect is granted");

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Bluetooth scan is not granted");
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_SCAN}, REQUEST_SCAN_BT);
            }
            else {
                btScan = true;
            }
            System.out.println("Bluetooth scan is granted");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Bluetooth advertise is not granted");
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADVERTISE}, REQUEST_ADV_BT);
            }
            else {
                btAdv = true;
            }
            System.out.println("Bluetooth advertise is granted");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Fine location is not granted");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOC);
        }
        else {
            fineLoc = true;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Coarse location is not granted");
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOC);
        }
        else {
            coarseLoc = true;
        }

        if (!bluetoothAdapter.isEnabled()) {
            System.out.println("Bluetooth is not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            btEnabled = true;
        }

        setBtOk();

        System.out.println("Bluetooth is enabled? (" + btEnabled + ") scan? (" + btScan + ") connect? (" + btConn + ") advertise? (" + btAdv
                + ") fine location? (" + fineLoc + ") coarse location? (" + coarseLoc + ") location? (" + locationEnabled + ")");
        System.out.println("Bluetooth is ok? " + btOk);
    }

    private void askForLocation() {
        if(!isLocationEnabled()) {
            System.out.println("Location is disabled");
            Intent enableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLocIntent, REQUEST_LOCATION);
        }
        else {
            locationEnabled = true;
        }
        setBtOk();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isLocationEnabled();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                btEnabled = true;
                setBtOk();
            }
            System.out.println("Bluetooth enable granted: " + btEnabled);
        }

        if (requestCode == REQUEST_LOCATION) {
            if (isLocationEnabled()) {
                System.out.println("Location is enabled");
                locationEnabled = true;
                setBtOk();
            }
            else {
                System.out.println("Location is still disabled");
                locationEnabled = false;
                setBtOk();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SCAN_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btScan = true;
                setBtOk();
            }
            System.out.println("Bluetooth scan granted: " + btScan);
        }
        if (requestCode == REQUEST_CONNECT_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btConn = true;
                setBtOk();
            }
            System.out.println("Bluetooth connect granted: " + btConn);
        }
        if (requestCode == REQUEST_ADV_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btAdv = true;
                setBtOk();
            }
            System.out.println("Bluetooth advertise granted: " + btAdv);
        }
        if (requestCode == REQUEST_FINE_LOC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fineLoc = true;
                setBtOk();
            }
            System.out.println("Fine location granted: " + fineLoc);
        }
        if (requestCode == REQUEST_COARSE_LOC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                coarseLoc = true;
                setBtOk();
            }
            System.out.println("Coarse location granted: " + coarseLoc);
        }
    }

    private boolean setBtOk() {
        btOk = btEnabled && btScan && btConn && btAdv && fineLoc && coarseLoc && locationEnabled;
        return btOk;
    }
    private boolean updateBtOk() {
        askForPermission();
        askForLocation();
        setBtOk();
        System.out.println("[UPD] Bluetooth is enabled? (" + btEnabled + ") scan? (" + btScan + ") connect? (" + btConn + ") advertise? (" + btAdv
                + ") fine location? (" + fineLoc + ") coarse location? (" + coarseLoc + ") location? (" + locationEnabled + ")");
        System.out.println("[UPD] Bluetooth is ok? " + btOk);
        return btOk;
    }

    private void setUp() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDeviceSearcher, filter);
    }

    private void setHandlers() {
        setDiscoveryStartHandler();
        setDiscoveryFinishHandler();
    }

    private void setDiscoveryStartHandler() {
        Button button = findViewById(R.id.startDiscoveryBtn);

            button.setOnClickListener(v -> {
                if (updateBtOk()) {
                    System.out.println("state: " + bluetoothAdapter.getState());
                    boolean res = bluetoothAdapter.startDiscovery();
                    System.out.println("Discovery started: " + res);
                }
                else {
                    Toast.makeText(this, "Please allow all services", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void setDiscoveryFinishHandler() {
        Button button = findViewById(R.id.finishDiscoveryBtn);
            button.setOnClickListener(v -> {
                if (updateBtOk()) {
                    boolean res = bluetoothAdapter.cancelDiscovery();
                    System.out.println("Discovery finished: " + res);
                }
                else {
                    Toast.makeText(this, "Please allow all services", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothDeviceSearcher);
    }

    private void addAlreadyPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            bluetoothVM.addDevice(device);
        }
    }

    private void setListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.bt_device_list);
        recyclerView.setAdapter(bluetoothVM.getAdapter(this::setDialogHandlerForDeviceSelection));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDialogHandlerForDeviceSelection(View view) {
//        view.setOnClickListener(v -> {
//            DialogFragment dialog = new WayToChooseImageDialog(food);
//            dialog.show(getParentFragmentManager(), "wayToChooseImageDialog");
//        });
    }
}