package com.example.calco.logic.business;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.ProductsInDishes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DishLogic {

    public static void persistNewDish(String dishName, List<Map.Entry<Product, Integer>> products) {
        long dishId = persistDish(dishName);
        persistProductsInDish(dishId, getProductsPercentImpact(products));
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
}
