package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import com.example.calco.logic.business.Product;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;
import com.example.calco.viewmodel.activity.state.ProductWithCCFPData;

public class LogicToUiConverter {
    public static FoodWithCCFPData getFoodWithCCFPData(Product product, Resources resources, String packageName) {
        int resId = resources.getIdentifier(product.getImageName() , "drawable", packageName);

        // todo refactor units short name - add units to settings
        String caloriesString = String.valueOf(product.getCalories()/100f) + " kcal";
        String carbsString = String.valueOf(product.getCarbs()/1000f) + " g";
        String fatsString = String.valueOf(product.getFats()/1000f) + " g";
        String proteinsString = String.valueOf(product.getProteins()/1000f) + " g";

        return new FoodWithCCFPData(product.getName(), resId, caloriesString, carbsString, fatsString, proteinsString);
    }

    public static ProductWithCCFPData getProductWithCCFPData(Product product, Resources resources, String packageName) {
        FoodWithCCFPData foodWithCCFPData = getFoodWithCCFPData(product, resources, packageName);
        return new ProductWithCCFPData(product, foodWithCCFPData);
    }
}
