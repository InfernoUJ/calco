package com.example.calco.viewmodel.activity;

import android.bluetooth.BluetoothDevice;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.calco.viewmodel.activity.adapters.BluetoothDevicesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BluetoothVM extends ViewModel {
    private List<BluetoothDevice> devices = new ArrayList<>();
    private final BluetoothDevicesAdapter adapter = new BluetoothDevicesAdapter();
    public void addDevice(BluetoothDevice device) {
        devices.add(device);
        adapter.replaceDeviceList(devices);
    }
    public BluetoothDevicesAdapter getAdapter(Consumer<View> dialogHandlerForDeviceSelection) {
        adapter.setDialogHandlerForDeviceSelection(dialogHandlerForDeviceSelection);
        return adapter;
    }
}
