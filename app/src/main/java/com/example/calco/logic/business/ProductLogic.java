package com.example.calco.logic.business;

import com.example.calco.logic.persistent.converters.DateTimeConverter;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.HistoryOfProducts;
import com.example.calco.logic.persistent.entities.PProduct;

import java.time.LocalDate;
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
        List<PProduct> lastProducts = db.historyOfProductsDao().getLastUsedProducts();
        List<PProduct> productsAlphabetical = db.productDao().getProductsAlphabetical();

        // todo can make null-termination it in querry method ?
        //  or create my own annotation for query methods

        // todo maybe remove duplicates
        List<Product> allProducts = Stream.concat(lastProducts.stream(), productsAlphabetical.stream()).
                map(ProductLogic::getProduct).collect(Collectors.toList());

        return allProducts;
    }


    public static void persistProductHistory(Product product, int mass, LocalDate date) {
        AppDataBase db = AppDataBase.getInstance();
        HistoryOfProducts history = getHistoryOfProduct(product, mass, date);
        db.historyOfProductsDao().insertAll(history);
    }

    public static HistoryOfProducts getHistoryOfProduct(Product product, int mass, LocalDate date) {
        HistoryOfProducts history = new HistoryOfProducts();
        history.productId = product.getId();
        history.utcDateTime = DateTimeConverter.timeToUtcMillis(date.atStartOfDay());
        history.milligrams = mass;
        return history;
    }
}
