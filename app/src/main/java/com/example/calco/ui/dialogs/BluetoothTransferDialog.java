package com.example.calco.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BluetoothTransferDialog extends DialogFragment {
    public interface BluetoothTransferDialogListener {
        void onDialogPositiveClick(BluetoothTransferDialog dialog);
        void onDialogNegativeClick(BluetoothTransferDialog dialog);
    }

    BluetoothTransferDialogListener listener;
    private BluetoothDevice device;
    public BluetoothDevice getDevice() {
        return device;
    }
    public BluetoothTransferDialog(BluetoothDevice device) {
        super();
        this.device = device;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BluetoothTransferDialog.BluetoothTransferDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BluetoothTransferDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Export to device", (dialog, which) -> {
            listener.onDialogPositiveClick(BluetoothTransferDialog.this);
        })
        .setNegativeButton("Import from device", (dialog, which) -> {
            dialog.cancel();
            listener.onDialogNegativeClick(BluetoothTransferDialog.this);
        });

        return builder.create();
    }
}
