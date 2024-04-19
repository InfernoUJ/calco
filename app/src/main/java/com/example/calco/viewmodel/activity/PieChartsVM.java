package com.example.calco.viewmodel.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.FoodLogic;
import com.example.calco.ui.charts.pie.PieChartsPercents;

import java.time.LocalDate;

public class PieChartsVM extends ViewModel {
    private MutableLiveData<PieChartsPercents> percents = new MutableLiveData<>(new PieChartsPercents());

    public void updatePercents(LocalDate date) {
        percents.setValue(FoodLogic.calculateGoalCompletion(date));
    }

    public LiveData<PieChartsPercents> getPercents() {
        return percents;
    }
}
