package com.example.calco;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.ui.dialogs.BluetoothTransferDialog;
import com.example.calco.viewmodel.activity.BluetoothVM;

import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements BluetoothTransferDialog.BluetoothTransferDialogListener {

    private BluetoothVM bluetoothVM;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private boolean btBt = false;
    private boolean btAdmin = false;
    private boolean btEnabled = false;
    private boolean btScan = false;
    private boolean btConn = false;
    private boolean btAdv = false;
    private boolean fineLoc = false;
    private boolean coarseLoc = false;
    private boolean btOk = false;
    private boolean locationEnabled = false;

    private static final int REQUEST_BLUETOOTH_BT = 41;
    private static final int REQUEST_ADMIN_BT = 42;
    private static final int REQUEST_ENABLE_BT = 43;
    private static final int REQUEST_CONNECT_BT = 44;
    private static final int REQUEST_SCAN_BT = 45;
    private static final int REQUEST_ADV_BT = 46;
    private static final int REQUEST_FINE_LOC = 47;
    private static final int REQUEST_COARSE_LOC = 48;
    private static final int REQUEST_LOCATION = 49;
    private static final UUID mUUID = UUID.fromString("36bba861-2ba2-4672-8368-055801cf78ad");
    private static String SDP_SERVICE_NAME = "Calco";

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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Bluetooth is not granted");
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_BT);
            }
            else {
                btBt = true;
            }
            System.out.println("Bluetooth is granted");

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Bluetooth admin is not granted");
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ADMIN_BT);
            }
            else {
                btAdmin = true;
            }
            System.out.println("Bluetooth admin is granted");
        }
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

        System.out.println("SDK ver: "+Build.VERSION.SDK_INT);
        System.out.println("Bluetooth is enabled? (" + btEnabled + ") scan? (" + btScan + ") connect? (" + btConn + ") advertise? (" + btAdv
                + ") fine location? (" + fineLoc + ") coarse location? (" + coarseLoc + ") location? (" + locationEnabled +
                ") bluetooth? (" + btBt + ") admin? (" + btAdmin + ")");
        System.out.println("Bluetooth is ok? " + btOk);
    }

    private void askForLocation() {
        if(!isLocationEnabled()) {
            System.out.println("Location is disabled");
            Toast.makeText(getApplicationContext(), "Location is disabled", Toast.LENGTH_LONG).show();
            Intent enableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLocIntent, REQUEST_LOCATION);
        }
        else {
            Toast.makeText(getApplicationContext(), "Location is enabled", Toast.LENGTH_LONG).show();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            btOk = btEnabled && btScan && btConn && btAdv && fineLoc && coarseLoc && locationEnabled;
            return btOk;
        }
        btOk = btEnabled && btBt && btAdmin && fineLoc && coarseLoc && locationEnabled;
        return btOk;
    }
    private boolean updateBtOk() {
        askForPermission();
        askForLocation();
        setBtOk();
        System.out.println("SDK ver: "+Build.VERSION.SDK_INT);
        System.out.println("[UPD] Bluetooth is enabled? (" + btEnabled + ") scan? (" + btScan + ") connect? (" + btConn + ") advertise? (" + btAdv
                + ") fine location? (" + fineLoc + ") coarse location? (" + coarseLoc + ") location? (" + locationEnabled +
                ") bluetooth? (" + btBt + ") admin? (" + btAdmin + ")");
        Toast.makeText(getApplicationContext(), "[UPD] Bluetooth is enabled? (" + btEnabled + ") scan? (" + btScan + ") connect? (" + btConn + ") advertise? (" + btAdv
                        + ") fine location? (" + fineLoc + ") coarse location? (" + coarseLoc + ") location? (" + locationEnabled +
                        ") bluetooth? (" + btBt + ") admin? (" + btAdmin + ")", Toast.LENGTH_LONG).show();
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

    private void setDialogHandlerForDeviceSelection(View view, BluetoothDevice device) {
        view.setOnClickListener(v -> {
            DialogFragment dialog = new BluetoothTransferDialog(device);
            dialog.show(getSupportFragmentManager(), "serverOrClientDialog");
        });
    }

    // I want to export to another device - i am a server
    @Override
    public void onDialogPositiveClick(BluetoothTransferDialog dialog) {
        Toast.makeText(this, "Server", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.cancelDiscovery();
        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }

    // I want to import from another device - i am a client
    @Override
    public void onDialogNegativeClick(BluetoothTransferDialog dialog) {
        Toast.makeText(this, "Client", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.cancelDiscovery();
        ClientThread clientThread = new ClientThread(dialog.getDevice());
        clientThread.start();
    }

    class ServerThread extends Thread {
        public final BluetoothServerSocket serverSocket;

        public ServerThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(SDP_SERVICE_NAME, mUUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            serverSocket = tmp;
            System.out.println("Server thread created");
            Toast.makeText(getApplicationContext(), "Server thread created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                if (socket != null) {
                    // todo
                    System.out.println("Server thread connected");
                    Toast.makeText(getApplicationContext(), "Server thread connected", Toast.LENGTH_LONG).show();
                    try {
                        serverSocket.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ClientThread extends Thread {
        public final BluetoothSocket socket;
        public final BluetoothDevice device;

        public ClientThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            this.device = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(mUUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = tmp;
            System.out.println("Client thread created");
            Toast.makeText(getApplicationContext(), "Client thread created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void run() {
            try {
                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("Client thread connected");
            Toast.makeText(getApplicationContext(), "Client thread connected", Toast.LENGTH_LONG).show();
            // todo do stuff
        }

        public void cancel() {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}