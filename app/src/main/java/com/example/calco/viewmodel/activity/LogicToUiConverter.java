package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import com.example.calco.logic.business.Dish;
import com.example.calco.logic.business.Product;
import com.example.calco.viewmodel.activity.state.DishWithCCFPData;
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

//    public static ProductWithCCFPData getProductWithCCFPData(Product product, Resources resources, String packageName) {
//        FoodWithCCFPData foodWithCCFPData = getFoodWithCCFPData(product, resources, packageName);
//        return new ProductWithCCFPData(product, foodWithCCFPData);
//    }

//    public static Product getProduct(ProductWithCCFPData productWithCCFPData) {
//        return productWithCCFPData.getProduct();
//    }


}
