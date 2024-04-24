package com.example.calco.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.calco.logic.business.entities.LimitType;

public class SetLimitsDialog extends DialogFragment {
    public interface SetLimitsDialogListener {
        void onSetLimitsDialogPositiveClick(DialogFragment dialog, LimitType type, String calories, String carbs, String fats, String proteins);
        void onSetLimitsDialogNegativeClick(DialogFragment dialog);
    }

    SetLimitsDialogListener listener;
    private LimitType type;

    public SetLimitsDialog(LimitType type) {
        super();
        this.type = type;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = (SetLimitsDialog.SetLimitsDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface. Throw exception.
            throw new ClassCastException(context
                    + " must implement SetLimitsDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout limitsInputContainer = createInputsContainer();

        builder.setView(limitsInputContainer)
                .setPositiveButton("Add", (dialog, which) -> {
                    listener.onSetLimitsDialogPositiveClick(SetLimitsDialog.this,
                            type,
                            ((EditText)limitsInputContainer.getChildAt(0)).getText().toString(),
                            ((EditText)limitsInputContainer.getChildAt(1)).getText().toString(),
                            ((EditText)limitsInputContainer.getChildAt(2)).getText().toString(),
                            ((EditText)limitsInputContainer.getChildAt(3)).getText().toString());
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                    listener.onSetLimitsDialogNegativeClick(SetLimitsDialog.this);
                });

        return builder.create();
    }

    private LinearLayout createInputsContainer() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        layout.addView(createMassInput("Calories limit"));
        layout.addView(createMassInput("Carbs limit"));
        layout.addView(createMassInput("Fats limit"));
        layout.addView(createMassInput("Proteins limit"));
        return layout;
    }

    private EditText createMassInput(String hintText) {
        EditText massInput = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16); // Example margin values
        massInput.setLayoutParams(params);
        massInput.setHint(hintText);
        massInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        return massInput;
    }

}
