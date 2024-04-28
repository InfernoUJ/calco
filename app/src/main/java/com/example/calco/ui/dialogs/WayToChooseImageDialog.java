package com.example.calco.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.calco.ui.products.table.FoodImpactRecordData;

public class WayToChooseImageDialog extends DialogFragment {

    public interface WayToChooseImageDialogListener {
        void onDialogPositiveClick(WayToChooseImageDialog dialog);
        void onDialogNegativeClick(WayToChooseImageDialog dialog);
        void onDialogNeutralClick(WayToChooseImageDialog dialog);
    }

    WayToChooseImageDialogListener listener;
    FoodImpactRecordData food;

    public FoodImpactRecordData getFood() {
        return food;
    }

    public WayToChooseImageDialog(FoodImpactRecordData food) {
        super();
        this.food = food;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = (WayToChooseImageDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface. Throw exception.
            throw new ClassCastException(context
                    + " must implement WayToChooseImageDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Take a photo", (dialog, which) -> {
            listener.onDialogPositiveClick(WayToChooseImageDialog.this);
        })
        .setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            listener.onDialogNegativeClick(WayToChooseImageDialog.this);
        })
        .setNeutralButton("Choose from gallery", (dialog, which) -> {
            listener.onDialogNeutralClick(WayToChooseImageDialog.this);
        });

        return builder.create();
    }
}
