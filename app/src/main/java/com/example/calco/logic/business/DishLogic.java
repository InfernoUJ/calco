package com.example.calco.logic.business;

import com.example.calco.logic.persistent.converters.DateTimeConverter;
import com.example.calco.logic.persistent.dao.PDishComponent;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.HistoryOfDishes;
import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PProduct;
import com.example.calco.logic.persistent.entities.ProductsInDishes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        int n = productsMass.size();
        int totalMass = 0;
        for(int i = 0; i < n; i++) {
            totalMass += productsMass.get(i).getValue();
        }
        List<Integer> productsPercents = new ArrayList<>(n);
        int currMassDifference = 0;
        for (int i = 0; i < n; i++) {
            int mass = productsMass.get(i).getValue();
            double percent = (double) mass / totalMass * 100;
            if (percent < 1) {
                productsPercents.add(1);
                currMassDifference += 1;
            }
            else {
                if (currMassDifference >= 0) {
                    productsPercents.add((int)Math.floor(percent));
                    currMassDifference -= 1;
                }
                else {
                    productsPercents.add((int)Math.ceil(percent));
                    currMassDifference += 1;
                }
            }
        }

        // subtract extra weight from the heaviest
        List<Integer> indices = IntStream.range(0, n)
                .boxed()
                .sorted((i1, i2) -> Integer.compare(productsMass.get(i2).getValue(), productsMass.get(i1).getValue()))
                .limit(currMassDifference)
                .collect(Collectors.toList());
        for (int index: indices) {
            productsPercents.set(index, productsPercents.get(index)-1);
        }

        List<Map.Entry<Product, Integer>> productsWithPercents = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (productsPercents.get(i) != 0) {
                productsWithPercents.add(Map.entry(productsMass.get(i).getKey(), productsPercents.get(i)));
            }
        }

        return productsWithPercents;
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
        HistoryOfDishes history = getHistoryOfDish(dish, mass, date);
        db.historyOfDishesDao().insertAll(history);
    }

    private static HistoryOfDishes getHistoryOfDish(Dish dish, int mass, LocalDate date) {
        HistoryOfDishes history = new HistoryOfDishes();
        history.dishId = dish.getId();
        history.utcDateTime = DateTimeConverter.timeToUtcMillis(date.atStartOfDay());
        history.milligrams = mass;
        return history;
    }
}
