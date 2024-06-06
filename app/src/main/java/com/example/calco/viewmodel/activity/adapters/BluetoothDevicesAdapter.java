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
import java.util.List;
import java.util.function.Consumer;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.BluetoothDeviceHolder> {

    private Consumer<View> dialogHandlerForDeviceSelection;
    static class BluetoothDeviceHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mac;
        View view;
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
        BluetoothDevice device = deviceList.get(position);
        holder.view.setOnClickListener(v -> dialogHandlerForDeviceSelection.accept(v));
        holder.name.setText(device.getName());
        holder.mac.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void replaceDeviceList(List<BluetoothDevice> bluetoothDevices) {
        this.deviceList.clear();
        this.deviceList.addAll(bluetoothDevices);
        notifyDataSetChanged();
    }

    public void setDialogHandlerForDeviceSelection(Consumer<View> dialogHandlerForDeviceSelection) {
        this.dialogHandlerForDeviceSelection = dialogHandlerForDeviceSelection;
    }
}
