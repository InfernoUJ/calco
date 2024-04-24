package com.example.calco.viewmodel.activity;

import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.entities.Limit;
import com.example.calco.logic.business.entities.LimitType;
import com.example.calco.logic.business.entities.LimitsLogic;

public class LimitsVM extends ViewModel {
    public void setLimit(LimitType type, String calories, String carbs, String fats, String proteins) {
        Integer caloriesInt = Integer.parseInt(calories)*1000;
        Integer fatsInt = Integer.parseInt(fats)*1000;
        Integer carbsInt = Integer.parseInt(carbs)*1000;
        Integer proteinsInt = Integer.parseInt(proteins)*1000;
        LimitsLogic.setLimit(new Limit(type, caloriesInt, carbsInt, fatsInt, proteinsInt));
    }
}
