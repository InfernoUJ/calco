package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import com.example.calco.logic.business.Dish;
import com.example.calco.logic.business.HistoryOfProducts;
import com.example.calco.logic.business.Product;
import com.example.calco.ui.products.table.ProductImpactRecordData;
import com.example.calco.viewmodel.activity.state.DishWithCCFPData;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;
import com.example.calco.viewmodel.activity.state.ProductWithCCFPData;

import java.util.List;
import java.util.stream.Collectors;

public class LogicToUiConverter {
    public static FoodWithCCFPData getFoodWithCCFPData(Product product, Resources resources, String packageName) {
        int resId = resources.getIdentifier(product.getImageName() , "drawable", packageName);

        // todo refactor units short name - add units to settings
        String caloriesString = String.valueOf(product.getCalories()/100f) + " kcal";
        String carbsString = String.valueOf(product.getCarbs()/1000f) + " g";
        String fatsString = String.valueOf(product.getFats()/1000f) + " g";
        String proteinsString = String.valueOf(product.getProteins()/1000f) + " g";

        return new ProductWithCCFPData(product, product.getName(), resId, caloriesString, carbsString, fatsString, proteinsString);
    }

    public static FoodWithCCFPData getFoodWithCCFPData(Dish dish, Resources resources, String packageName) {
        int resId = resources.getIdentifier(dish.getImageName() , "drawable", packageName);

        String caloriesString = String.valueOf(dish.getCalories()/100f) + " kcal";
        String carbsString = String.valueOf(dish.getCarbs()/1000f) + " g";
        String fatsString = String.valueOf(dish.getFats()/1000f) + " g";
        String proteinsString = String.valueOf(dish.getProteins()/1000f) + " g";

        return new DishWithCCFPData(dish, dish.getName(), resId, caloriesString, carbsString, fatsString, proteinsString);
    }

    public static List<ProductImpactRecordData> getProductImpactRecordData(List<HistoryOfProducts> product, Resources resources, String packageName) {
        int mass = 0;
        for (HistoryOfProducts historyOfProducts : product) {
            mass += historyOfProducts.getMilligrams();
        }
        int totalMass = mass;

        return product.stream().map(history -> {
            int mmillis = history.getMilligrams();
            int percentage = (int) ((mmillis / (float) totalMass) * 100);
            int resId = resources.getIdentifier(history.getProduct().getImageName() , "drawable", packageName);
            return new ProductImpactRecordData(history.getProduct().getName(), percentage, mmillis/1000, resId);
        }).collect(Collectors.toList());
    }

}
