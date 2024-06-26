package com.example.calco.viewmodel.activity;

import android.content.res.Resources;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.ui.products.table.FoodImpactRecordData;
import com.example.calco.logic.business.entities.FoodComponent;
import com.example.calco.viewmodel.activity.adapters.FoodForPeriodAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class FoodTableVM extends ViewModel {
    private List<FoodImpactRecordData> foodRecords = new ArrayList<>();
    private FoodComponent currentSort = FoodComponent.CALORIES;
    private final FoodForPeriodAdapter adapter = new FoodForPeriodAdapter();
    public void updateFoodTable(LocalDate startDate, LocalDate endDate, Resources resources, String packageName) {
        List<HistoryOfProducts> historyOfProducts = ProductLogic.getHistoryForPeriod(startDate, endDate);
        List<HistoryOfDishes> historyOfDishes = DishLogic.getHistoryForPeriod(startDate, endDate);
        List<FoodImpactRecordData> foodImpactRecordData = LogicToUiConverter.getProductImpactRecordData(historyOfProducts, historyOfDishes, resources, packageName, currentSort);
        foodImpactRecordData.sort((o1, o2) -> o2.getPercentage().compareTo(o1.getPercentage()));
        foodRecords = foodImpactRecordData;

        adapter.replaceFoodList(foodRecords);
    }

    public FoodForPeriodAdapter getAdapter(BiConsumer<View, FoodImpactRecordData> dialogHandlerForImageSelection) {
        adapter.setDialogHandlerForImageSelection(dialogHandlerForImageSelection);
        return adapter;
    }
    
    public void sortFoodBy(FoodComponent components, LocalDate startDate, LocalDate endDate, Resources resources, String packageName) {
        currentSort = components;
        updateFoodTable(startDate, endDate, resources, packageName);
    }
}
