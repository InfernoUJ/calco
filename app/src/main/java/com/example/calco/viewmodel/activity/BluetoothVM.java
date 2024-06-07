package com.example.calco.viewmodel.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.Uri;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.calco.logic.files.JsonFilesCreator;
import com.example.calco.logic.files.JsonZipCreator;
import com.example.calco.viewmodel.activity.adapters.BluetoothDevicesAdapter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BluetoothVM extends ViewModel {
    private Set<BluetoothDevice> devices = new HashSet<>();
    private final BluetoothDevicesAdapter adapter = new BluetoothDevicesAdapter();
    public void addDevice(BluetoothDevice device) {
        devices.add(device);
        adapter.replaceDeviceList(devices);
    }
    public void removeDevice(BluetoothDevice device) {
        devices.remove(device);
        adapter.replaceDeviceList(devices);
    }

    public BluetoothDevicesAdapter getAdapter(BiConsumer<View, BluetoothDevice> dialogHandlerForDeviceSelection) {
        adapter.setDialogHandlerForDeviceSelection(dialogHandlerForDeviceSelection);
        return adapter;
    }

    public OutputStream getHistory(Context context) {
        Uri zipUri = JsonZipCreator.createZip(context, JsonFilesCreator.createJsonFiles());
        return null;
    }
}
