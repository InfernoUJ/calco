package com.example.calco.ui.pickers.data;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerListener {
        void onDialogPositiveClick(DatePickerFragment dialog, int year, int month, int day, DateType type);
        void onDialogNegativeClick(DatePickerFragment dialog);
    }

    public enum DateType {
        START_DATE,
        END_DATE
    }

    DatePickerFragment.DatePickerListener listener;
    private final DateType dateType;

    public DatePickerFragment(DateType dateType) {
        super();
        this.dateType = dateType;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = (DatePickerFragment.DatePickerListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface. Throw exception.
            throw new ClassCastException(context
                    + " must implement DatePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("calco", "Date set: " + year + "/" + month + "/" + dayOfMonth);
        listener.onDialogPositiveClick(DatePickerFragment.this, year, month+1, dayOfMonth, dateType);
    }

}