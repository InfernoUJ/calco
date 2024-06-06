package com.example.calco;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

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

import com.example.calco.ui.dialogs.WayToChooseImageDialog;
import com.example.calco.ui.products.table.FoodImpactRecordData;
import com.example.calco.viewmodel.activity.BluetoothVM;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothVM bluetoothVM;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private boolean btEnabled = false;

    private static final int REQUEST_ENABLE_BT = 43;
    private static final int REQUEST_CONNECT_BT = 44;

    private BroadcastReceiver bluetoothDeviceSearcher = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothVM.addDevice(device);
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


        if (btEnabled) {
            setUp();
            addAlreadyPairedDevices();
        }
    }

    private void askForPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Bluetooth connect is not granted");
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
        }
        System.out.println("Bluetooth connect is granted");

        if (!bluetoothAdapter.isEnabled()) {
            System.out.println("Bluetooth is not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if(!btEnabled) {
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        btEnabled = true;
        System.out.println("Bluetooth is enabled");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                btEnabled = true;
            }
            System.out.println("Bluetooth enable granted: " + btEnabled);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONNECT_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btEnabled = true;
            }
            System.out.println("Bluetooth connect granted: " + btEnabled);
        }
    }

    private void setUp() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDeviceSearcher, filter);
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