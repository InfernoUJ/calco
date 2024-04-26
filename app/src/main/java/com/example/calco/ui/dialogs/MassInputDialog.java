package com.example.calco.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.calco.network.entities.WebProduct;

public class MassInputDialog extends DialogFragment {

    public interface MassInputDialogListener {
        void onDialogPositiveClick(MassInputDialog dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    MassInputDialogListener listener;
    private int index;
    private WebProduct product;

    private String mass;

    public int getIndex() {
        return index;
    }

    public WebProduct getProduct() {
        return product;
    }

    public String getMass() {
        return mass;
    }

    public MassInputDialog(int index) {
        super();
        this.index = index;
    }

    public MassInputDialog(WebProduct product) {
        super();
        this.product = product;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = (MassInputDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface. Throw exception.
            throw new ClassCastException(context
                    + " must implement MassInputDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText massInput = createMassInput();

        builder.setView(massInput)
        .setPositiveButton("Add", (dialog, which) -> {
            mass = massInput.getText().toString();
            listener.onDialogPositiveClick(MassInputDialog.this);
        })
        .setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            listener.onDialogNegativeClick(MassInputDialog.this);
        });

        return builder.create();
    }

    private EditText createMassInput() {
        EditText massInput = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16); // Example margin values
        massInput.setLayoutParams(params);
        massInput.setHint("Enter food mass");
        massInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        return massInput;
    }
}
