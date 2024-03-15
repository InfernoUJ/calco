package com.example.calco.ui.charts.pie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PieChartViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public PieChartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pie chart fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
