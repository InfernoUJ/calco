package com.example.calco.viewmodel.activity;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.state.CreateProductUiState;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;
import com.example.calco.viewmodel.activity.state.ProductWithCCFPData;

import java.util.List;
import java.util.stream.Collectors;

public class CreateDishVM extends ViewModel {
    MutableLiveData<List<ProductWithCCFPData>> products = new MutableLiveData<>();

    public LiveData<List<ProductWithCCFPData>> getProducts() {
        return products;
    }

    public void updateProductList(Resources resources, String packageName) {
        List<Product> newProducts = ProductLogic.getLastUsedProducts();
        List<ProductWithCCFPData> uiProducts = newProducts.stream()
                .map(product -> LogicToUiConverter.getProductWithCCFPData(product, resources, packageName))
                .collect(Collectors.toList());

        products.setValue(uiProducts);
    }
}
