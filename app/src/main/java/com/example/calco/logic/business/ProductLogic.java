package com.example.calco.logic.business;

import com.example.calco.logic.persistent.converters.DateTimeConverter;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PHistoryOfProducts;
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
        PHistoryOfProducts history = getPHistoryOfProduct(product, mass, date);
        db.historyOfProductsDao().insertAll(history);
    }

    public static PHistoryOfProducts getPHistoryOfProduct(Product product, int mass, LocalDate date) {
        PHistoryOfProducts history = new PHistoryOfProducts();
        history.productId = product.getId();
        history.utcDateTime = DateTimeConverter.timeToUtcMillis(date.atStartOfDay());
        history.milligrams = mass;
        return history;
    }

    public static List<HistoryOfProducts> getDayHistory(LocalDate day) {
        LocalDate nextDay = day.plusDays(1);
        long thisDayMillis = DateTimeConverter.timeToUtcMillis(day.atStartOfDay());
        long nextDayMillis = DateTimeConverter.timeToUtcMillis(nextDay.atStartOfDay());

        AppDataBase db = AppDataBase.getInstance();

        List<PHistoryOfProducts> pHistory = db.historyOfProductsDao().getHistoryInDateDiapason(thisDayMillis, nextDayMillis-1);
        List<HistoryOfProducts> history = pHistory.stream().map(ProductLogic::getHistoryOfProduct).collect(Collectors.toList());
        return history;
    }

    public static HistoryOfProducts getHistoryOfProduct(PHistoryOfProducts pHistory) {
        AppDataBase db = AppDataBase.getInstance();
        PProduct pProduct = db.productDao().findById(pHistory.productId);
        Product product = getProduct(pProduct);
        LocalDate date = DateTimeConverter.timeFromUtcMillis(pHistory.utcDateTime).toLocalDate();
        return new HistoryOfProducts(pHistory.uid, product, date, pHistory.milligrams);
    }
}
