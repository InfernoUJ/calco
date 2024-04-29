package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.ui.products.table.FoodImpactRecordData;
import com.example.calco.logic.business.entities.FoodComponent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodTableVM extends ViewModel {
    private MutableLiveData<List<FoodImpactRecordData>> foodRecords = new MutableLiveData<>(new ArrayList<>());
    private FoodComponent currentSort = FoodComponent.CALORIES;
    public void updateFoodTable(LocalDate date, Resources resources, String packageName) {
        List<HistoryOfProducts> historyOfProducts = ProductLogic.getDayHistory(date);
        List<HistoryOfDishes> historyOfDishes = DishLogic.getDayHistory(date);
        List<FoodImpactRecordData> foodImpactRecordData = LogicToUiConverter.getProductImpactRecordData(historyOfProducts, historyOfDishes, resources, packageName, currentSort);
        foodImpactRecordData.sort((o1, o2) -> o2.getPercentage().compareTo(o1.getPercentage()));
        foodRecords.setValue(foodImpactRecordData);
    }

    public LiveData<List<FoodImpactRecordData>> getFoodRecords() {
        return foodRecords;
    }
    
    public void sortFoodBy(FoodComponent components, LocalDate date, Resources resources, String packageName) {
        List<FoodImpactRecordData> foodImpactRecordData = foodRecords.getValue();
        if (foodImpactRecordData == null) {
            return;
        }
        currentSort = components;
        updateFoodTable(date, resources, packageName);
    }
}
