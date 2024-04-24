package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.ui.products.table.ProductImpactRecordData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodTableVM extends ViewModel {
    private MutableLiveData<List<ProductImpactRecordData>> foodRecords = new MutableLiveData<>(new ArrayList<>());
    public void updateFoodTable(LocalDate date, Resources resources, String packageName) {
        List<HistoryOfProducts> historyOfProducts = ProductLogic.getDayHistory(date);
        List<HistoryOfDishes> historyOfDishes = DishLogic.getDayHistory(date);
        List<ProductImpactRecordData> productImpactRecordData = LogicToUiConverter.getProductImpactRecordData(historyOfProducts, historyOfDishes, resources, packageName);
        foodRecords.setValue(productImpactRecordData);
    }

    public LiveData<List<ProductImpactRecordData>> getFoodRecords() {
        return foodRecords;
    }
}
