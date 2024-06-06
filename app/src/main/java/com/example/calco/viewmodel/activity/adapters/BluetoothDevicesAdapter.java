package com.example.calco.viewmodel.activity.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.ui.products.table.FoodImpactRecordData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.BluetoothDeviceHolder> {

    private BiConsumer<View, BluetoothDevice> dialogHandlerForDeviceSelection;
    static class BluetoothDeviceHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mac;
        View view;
        BluetoothDevice device;
        public BluetoothDeviceHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.bt_device_name);
            mac = itemView.findViewById(R.id.bt_device_mac);
        }
    }

    private List<BluetoothDevice> deviceList = new ArrayList<>();

    @NonNull
    @Override
    public BluetoothDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bt_device_record, parent, false);
        return new BluetoothDeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceHolder holder, int position) {
        BluetoothDevice mdevice = deviceList.get(position);
        holder.device = mdevice;
        holder.view.setOnClickListener(v -> dialogHandlerForDeviceSelection.accept(v, holder.device));
        holder.name.setText(holder.device.getName());
        holder.mac.setText(holder.device.getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void replaceDeviceList(Collection<BluetoothDevice> bluetoothDevices) {
        this.deviceList.clear();
        this.deviceList.addAll(bluetoothDevices);
        notifyDataSetChanged();
    }

    public void setDialogHandlerForDeviceSelection(BiConsumer<View, BluetoothDevice> dialogHandlerForDeviceSelection) {
        this.dialogHandlerForDeviceSelection = dialogHandlerForDeviceSelection;
    }
}
