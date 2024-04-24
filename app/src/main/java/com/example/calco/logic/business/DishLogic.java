package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.entities.DishComponent;
import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.persistent.converters.DateTimeConverter;
import com.example.calco.logic.persistent.dao.PDishComponent;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PHistoryOfDishes;
import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PProduct;
import com.example.calco.logic.persistent.entities.ProductsInDishes;
import com.example.calco.logic.utils.PercentConvertor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DishLogic {

    public static void persistNewDish(String dishName, List<Map.Entry<Food, Integer>> products) {
        if (products.isEmpty()) {
            return;
        }
        // todo change to factory/strategy pattern
        if (!(products.get(0).getKey() instanceof Product)) {
            throw new IllegalArgumentException("Only products can be persisted as dishes' components.");
        }
        List<Map.Entry<Product, Integer>> products2 = new ArrayList<>();
        for (Map.Entry<Food, Integer> entry: products) {
            products2.add(Map.entry((Product)entry.getKey(), entry.getValue()));
        }
        long dishId = persistDish(dishName);
        persistProductsInDish(dishId, getProductsPercentImpact(products2));
    }

    private static long persistDish(String name) {
        PDish pDish = createPDish(name);
        AppDataBase db = AppDataBase.getInstance();
        Long id = db.dishDao().insertAll(pDish).get(0);
        System.out.println("Dish persisted: " + name + " id: "+ id);
        return id;
    }

    private static PDish createPDish(String name) {
        PDish pDish = new PDish();
        pDish.name = name;
        return pDish;
    }

    // todo refactor
    private static List<Map.Entry<Product, Integer>> getProductsPercentImpact(List<Map.Entry<Product, Integer>> productsMass) {
        return PercentConvertor.getPercentImpact(productsMass);
    }

    private static void persistProductsInDish(long dishId, List<Map.Entry<Product, Integer>> productsPercentImpact) {
        productsPercentImpact.forEach(percentImpact ->
                persistOneProductInDish(getProductsInDishes(dishId, percentImpact.getKey().getId(), percentImpact.getValue())));
    }

    private static ProductsInDishes getProductsInDishes(long dishId, long productId, int percent) {
        ProductsInDishes productsInDishes = new ProductsInDishes();
        productsInDishes.dishId = dishId;
        productsInDishes.productId = productId;
        productsInDishes.percentContent = percent;
        return productsInDishes;
    }

    private static void persistOneProductInDish(ProductsInDishes productInDish) {
        AppDataBase db = AppDataBase.getInstance();
        db.productsInDishesDao().insertAll(productInDish);
    }

    private static Dish getDish(PDish pDish) {
        return new Dish(pDish.uid, pDish.name, getDishComponents(pDish.uid), Dish.DEFAULT_IMAGE);
    }

    private static List<DishComponent> getDishComponents(long dishId) {
        AppDataBase db = AppDataBase.getInstance();
        List<PDishComponent> productsInDishes = db.productsInDishesDao().getComponents(dishId);
        return productsInDishes.stream().map(DishLogic::getDishComponent).collect(Collectors.toList());
    }

    public static DishComponent getDishComponent(PDishComponent pDishComponent) {
        AppDataBase db = AppDataBase.getInstance();
        PProduct pProduct = db.productDao().findById(pDishComponent.productId);
        return new DishComponent(ProductLogic.getProduct(pProduct), pDishComponent.percentContent);
    }

    public static List<Dish> getLastUsedDishes() {
        AppDataBase db = AppDataBase.getInstance();
        List<PDish> lastDishes = db.historyOfDishesDao().getLastUsedDishes();
        List<PDish> dishesAlphabetical = db.dishDao().getDishesAlphabetical();

        List<Dish> allDishes = Stream.concat(lastDishes.stream(), dishesAlphabetical.stream()).
                map(DishLogic::getDish).collect(Collectors.toList());

        return allDishes;
    }

    public static void persistDishHistory(Dish dish, int mass, LocalDate date) {
        AppDataBase db = AppDataBase.getInstance();
        PHistoryOfDishes history = getPHistoryOfDish(dish, mass, date);
        db.historyOfDishesDao().insertAll(history);
    }

    private static PHistoryOfDishes getPHistoryOfDish(Dish dish, int mass, LocalDate date) {
        PHistoryOfDishes history = new PHistoryOfDishes();
        history.dishId = dish.getId();
        history.utcDateTime = DateTimeConverter.timeToUtcMillis(date.atStartOfDay());
        history.milligrams = mass;
        return history;
    }

    public static List<HistoryOfDishes> getDayHistory(LocalDate day) {
        LocalDate nextDay = day.plusDays(1);
        long thisDayMillis = DateTimeConverter.timeToUtcMillis(day.atStartOfDay());
        long nextDayMillis = DateTimeConverter.timeToUtcMillis(nextDay.atStartOfDay());

        AppDataBase db = AppDataBase.getInstance();

        List<PHistoryOfDishes> pHistory = db.historyOfDishesDao().getHistoryInDateDiapason(thisDayMillis, nextDayMillis-1);
        List<HistoryOfDishes> history = pHistory.stream().map(DishLogic::getHistoryOfDish).collect(Collectors.toList());
        return history;
    }

    public static HistoryOfDishes getHistoryOfDish(PHistoryOfDishes pHistory) {
        AppDataBase db = AppDataBase.getInstance();
        PDish pDish = db.dishDao().findById(pHistory.dishId);
        Dish dish = getDish(pDish);
        LocalDate date = DateTimeConverter.timeFromUtcMillis(pHistory.utcDateTime).toLocalDate();
        return new HistoryOfDishes(pHistory.uid, dish, date, pHistory.milligrams);
    }
}
