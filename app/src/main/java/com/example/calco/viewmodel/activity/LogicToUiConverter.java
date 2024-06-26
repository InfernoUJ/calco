package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import com.example.calco.logic.business.FoodLogic;
import com.example.calco.logic.business.ImageLogic;
import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.entities.FoodComponent;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.HistoryOfFood;
import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.utils.PercentConvertor;
import com.example.calco.network.entities.WebProduct;
import com.example.calco.ui.products.table.FoodImpactRecordData;
import com.example.calco.viewmodel.activity.state.DishWithCCFPData;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;
import com.example.calco.viewmodel.activity.state.ImageTwoTypesFactory;
import com.example.calco.viewmodel.activity.state.ProductWithCCFPData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogicToUiConverter {
    public static FoodWithCCFPData getFoodWithCCFPData(Product product, Resources resources, String packageName) {
        int resId = resources.getIdentifier(product.getImageName() , "drawable", packageName);

        // todo refactor units short name - add units to settings
        String caloriesString = String.valueOf(product.getCalories()/1000f) + " kcal";
        String carbsString = String.valueOf(product.getCarbs()/1000f) + " g";
        String fatsString = String.valueOf(product.getFats()/1000f) + " g";
        String proteinsString = String.valueOf(product.getProteins()/1000f) + " g";

        return ImageTwoTypesFactory.createImageTwoTypes(ProductWithCCFPData.class, resources, packageName, product, product.getName(), caloriesString, carbsString, fatsString, proteinsString);
    }

    public static FoodWithCCFPData getFoodWithCCFPData(Dish dish, Resources resources, String packageName) {
        int resId = resources.getIdentifier(dish.getImageName() , "drawable", packageName);

        String caloriesString = String.valueOf(dish.getCalories()/1000f) + " kcal";
        String carbsString = String.valueOf(dish.getCarbs()/1000f) + " g";
        String fatsString = String.valueOf(dish.getFats()/1000f) + " g";
        String proteinsString = String.valueOf(dish.getProteins()/1000f) + " g";

        return ImageTwoTypesFactory.createImageTwoTypes(DishWithCCFPData.class, resources, packageName, dish, dish.getName(), caloriesString, carbsString, fatsString, proteinsString);
    }

    public static List<FoodImpactRecordData> getProductImpactRecordData(List<HistoryOfProducts> products, List<HistoryOfDishes> dishes, Resources resources, String packageName, FoodComponent sortComponent) {
        List<HistoryOfFood> food = new ArrayList<>(products);
        food.addAll(dishes);
        List<Map.Entry<HistoryOfFood, Integer>> foodWithPercents = PercentConvertor.getPercentImpact(food,
                foodHistory -> FoodLogic.getComponentAbsolute(foodHistory, sortComponent));

        List<FoodImpactRecordData> allRecords = foodWithPercents.stream().map(entry -> {
            HistoryOfFood history = entry.getKey();
            int mmillis = history.getMilligrams();
            int percentage = entry.getValue();

            return ImageTwoTypesFactory.createImageTwoTypes(FoodImpactRecordData.class, resources, packageName,history.getFood(), history.getFood().getName(), percentage, mmillis/1000);
        }).collect(Collectors.toList());

        return allRecords;
    }

    public static Product getProduct(WebProduct webProduct) {
        int calories = (int)(webProduct.getCalories()*1000);
        int carbs = (int)(webProduct.getCarbs()*1000);
        int fats = (int)(webProduct.getFats()*1000);
        int proteins = (int)(webProduct.getProteins()*1000);
        return new Product(0, webProduct.getName(), calories, carbs, fats, proteins, ImageLogic.DEFAULT_IMAGE);
    }
}
