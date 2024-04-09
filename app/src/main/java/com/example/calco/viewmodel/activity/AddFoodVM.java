package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.Dish;
import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddFoodVM extends ViewModel {
    // todo make layout and class repr of this layout(consider using one repr for all food (both product and dish) like FoodWithCCFPData
    MutableLiveData<List<FoodWithCCFPData>> products = new MutableLiveData<>(new ArrayList<>());

    public void updateLastUsedFood(Resources resources, String packageName) {
        List<Product> newProducts = ProductLogic.getLastUsedProducts();
        List<Dish> newDishes = DishLogic.getLastUsedDishes();

        Stream<FoodWithCCFPData> uiProducts = newProducts.stream()
                .map(product -> LogicToUiConverter.getFoodWithCCFPData(product, resources, packageName));
        Stream<FoodWithCCFPData> uiDishes = newDishes.stream()
                .map(dish -> LogicToUiConverter.getFoodWithCCFPData(dish, resources, packageName));

        // todo order from history by last used
        List<FoodWithCCFPData> allFood = Stream.concat(uiProducts, uiDishes).collect(Collectors.toList());

        products.setValue(allFood);
    }

    public LiveData<List<FoodWithCCFPData>> getFood() {
        return products;
    }
}
