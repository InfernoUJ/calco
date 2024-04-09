package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.DishLogic;
import com.example.calco.logic.business.Food;
import com.example.calco.logic.business.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.state.CreateProductUiState;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateDishVM extends ViewModel {
    MutableLiveData<List<FoodWithCCFPData>> products = new MutableLiveData<>();

    public LiveData<List<FoodWithCCFPData>> getProducts() {
        return products;
    }

    public void updateProductList(Resources resources, String packageName) {
        List<Product> newProducts = ProductLogic.getLastUsedProducts();
        List<FoodWithCCFPData> uiProducts = newProducts.stream()
                .map(product -> LogicToUiConverter.getFoodWithCCFPData(product, resources, packageName))
                .collect(Collectors.toList());

        products.setValue(uiProducts);
    }

    public void createDish(String dishName, List<Map.Entry<Integer, Integer>> chosenProducts) {
        List<FoodWithCCFPData> allProducts = products.getValue();
        if (allProducts == null) {
            return;
        }
        List<Map.Entry<Food, Integer>> products = chosenProducts.stream()
                .map(entry -> Map.entry(allProducts.get(entry.getKey()).getFood(), entry.getValue()))
                .collect(Collectors.toList());

        DishLogic.persistNewDish(dishName, products);
    }
}
