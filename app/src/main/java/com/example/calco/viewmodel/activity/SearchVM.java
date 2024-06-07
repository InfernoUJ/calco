package com.example.calco.viewmodel.activity;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.calco.network.WebServiceFactory;
import com.example.calco.network.entities.WebDishes;
import com.example.calco.network.entities.WebProduct;
import com.example.calco.network.service.ProductService;
import com.example.calco.viewmodel.activity.adapters.SearchingResultsAdapter;

import java.util.function.BiConsumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVM extends ViewModel {

    private final SearchingResultsAdapter adapter = new SearchingResultsAdapter();

    static class UpdatRecyclerViewCallback implements Callback<WebDishes> {
        private final SearchingResultsAdapter adapter;
        public UpdatRecyclerViewCallback(SearchingResultsAdapter adapter) {
            this.adapter = adapter;
        }
        @Override
        public void onResponse(Call<WebDishes> call, Response<WebDishes> response) {
            if (response.isSuccessful()) {
                adapter.replaceProducts(response.body().getProducts());
            }
            else {
                System.out.println( "Error: " + response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<WebDishes> call, Throwable throwable) {
            System.out.println( "Error: " + throwable.getMessage());
        }
    }

    public SearchingResultsAdapter getAdapter(BiConsumer<View, WebProduct> handler) {
        adapter.setDialogHandlerForSearchingRow(handler);
        return adapter;
    }

    public void searchFood(String name) {
        ProductService productService = WebServiceFactory.createService(ProductService.class);
        Call<WebDishes> call = productService.getDishes(name);
        call.enqueue(new UpdatRecyclerViewCallback(adapter));
    }
}
