package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.adapters.CreateDishTableAdapter;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateDishVM extends ViewModel {
    List<FoodWithCCFPData> products = new ArrayList<>();
    Map<FoodWithCCFPData, Integer> productsMass = new HashMap<>();

    public List<FoodWithCCFPData> getProducts() {
        return products;
    }
    private final CreateDishTableAdapter adapter = new CreateDishTableAdapter();

    public void updateProductList(Resources resources, String packageName) {
        List<Product> newProducts = ProductLogic.getLastUsedProducts();
        List<FoodWithCCFPData> uiProducts = newProducts.stream()
                .map(product -> LogicToUiConverter.getFoodWithCCFPData(product, resources, packageName))
                .collect(Collectors.toList());

        products = uiProducts;
        adapter.replaceFoodList(products);
    }

    public CreateDishTableAdapter getAdapter() {
        adapter.setMassSaver(productsMass::put);
        return adapter;
    }

    public void createDish(String dishName) {
        if (products == null) {
            return;
        }
        Map<Food, Integer> foodWithMass = productsMass.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getFood(), Map.Entry::getValue));

        DishLogic.persistNewDish(dishName, foodWithMass);
    }
}
