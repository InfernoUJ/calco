package com.example.calco.viewmodel.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.ImageLogic;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.business.ProductLogic;
import com.example.calco.viewmodel.activity.state.CreateProductUiState;

public class CreateProductVM extends ViewModel {
    private final MutableLiveData<CreateProductUiState> uiState = new MutableLiveData<>();

//    во вью апдейтим дату
//    тут делаем колбек на апдейт базы
    public LiveData<CreateProductUiState> getUiState() {
        return uiState;
    }

    public void createNewProduct(String name, String calories, String carbs, String fats, String proteins) {
        Product product = new Product(-1, name, 1000*Integer.parseInt(calories), 1000*Integer.parseInt(carbs),
                1000*Integer.parseInt(fats), 1000*Integer.parseInt(proteins), ImageLogic.DEFAULT_IMAGE);
        ProductLogic.persistNewProduct(product);
    }

}
