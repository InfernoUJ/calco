package com.example.calco.viewmodel.activity;

import android.content.res.Resources;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.calco.MainActivity;
import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.network.entities.WebProduct;
import com.example.calco.viewmodel.activity.adapters.HistoryFoodAdapter;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddFoodVM extends ViewModel {
    List<FoodWithCCFPData> products = new ArrayList<>();
    private final HistoryFoodAdapter adapter = new HistoryFoodAdapter();

    public void updateLastUsedFood(Resources resources, String packageName) {
        List<Product> newProducts = ProductLogic.getLastUsedProducts();
        List<Dish> newDishes = DishLogic.getLastUsedDishes();

        Stream<FoodWithCCFPData> uiProducts = newProducts.stream()
                .map(product -> LogicToUiConverter.getFoodWithCCFPData(product, resources, packageName));
        Stream<FoodWithCCFPData> uiDishes = newDishes.stream()
                .map(dish -> LogicToUiConverter.getFoodWithCCFPData(dish, resources, packageName));

        products = Stream.concat(uiProducts, uiDishes).collect(Collectors.toList());

        adapter.replaceFoodList(products);
    }

    public HistoryFoodAdapter getAdapter(BiConsumer<View, Integer> handler) {
        adapter.setDialogHandlerForMassInput(handler);
        return adapter;
    }

    // todo refactor
    public void addFoodToHistory(int index, String massStr, WebProduct webProduct, String dateStr) {
        int mass = Integer.parseInt(massStr) * 1000;
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(MainActivity.dateFormat));
        if (webProduct == null) {
            FoodWithCCFPData food = products.get(index);
            if (food.getFood() instanceof Product) {
                Product product = (Product) food.getFood();
                ProductLogic.persistProductHistory(product, mass, date);
            } else {
                Dish dish = (Dish) food.getFood();
                DishLogic.persistDishHistory(dish, mass, date);
            }
        }
        else {
            Product product = LogicToUiConverter.getProduct(webProduct);
            ProductLogic.persistWebProductInHistory(product, mass, date);
        }
    }
}
