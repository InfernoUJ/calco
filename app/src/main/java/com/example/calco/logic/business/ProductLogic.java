package com.example.calco.logic.business;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Database;
import androidx.room.Transaction;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductLogic {

    // todo add image persistence
    public static void persistNewProduct(Product product) {
        // Save product to database
        PProduct pProduct = getPProduct(product);
        AppDataBase db = AppDataBase.getInstance();
        db.productDao().insertAll(pProduct);
        System.out.println("Product persisted: " + product.getName());
    }

    public static PProduct getPProduct(Product product) {
        PProduct pProduct = new PProduct();
        pProduct.name = product.getName();
        pProduct.calories = product.getCalories();
        pProduct.carbs = product.getCarbs();
        pProduct.fats = product.getFats();
        pProduct.proteins = product.getProteins();
        return pProduct;
    }

    public static Product getProduct(PProduct pProduct) {
        return new Product(pProduct.uid, pProduct.name, pProduct.calories, pProduct.carbs, pProduct.fats, pProduct.proteins, Product.DEFAULT_IMAGE);
    }

    public static List<Product> getLastUsedProducts() {
        AppDataBase db = AppDataBase.getInstance();
        LiveData<List<PProduct>> pproducts = db.historyOfProductsDao().getLastUsedProducts();
        List<PProduct> pproduct2 = db.productDao().getLastUsedProducts();

        // todo can make null-termination it in querry method ?
        //  or create my own annotation for query methods

        LiveData<Stream<Product>> products = Transformations.map(pproducts,
                pproductList -> pproductList.stream().map(ProductLogic::getProduct));

        List<Product> products2 = pproduct2.stream().map(ProductLogic::getProduct).collect(Collectors.toList());
        return products2;
    }
}
