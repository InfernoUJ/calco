package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.MainActivity;
import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddFoodVM extends ViewModel {
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

    // todo refactor
    public void addFoodToHistory(int index, String massStr, String dateStr) {
        FoodWithCCFPData food = products.getValue().get(index);
        int mass = Integer.parseInt(massStr)*1000;
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(MainActivity.dateFormat));

        if (food.getFood() instanceof Product) {
            Product product = (Product)food.getFood();
            ProductLogic.persistProductHistory(product, mass, date);
        } else {
            Dish dish = (Dish)food.getFood();
            DishLogic.persistDishHistory(dish, mass, date);
        }
    }
}
