package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.entities.DishComponent;
import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.HistoryOfDishes;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.persistent.converters.DateTimeConverter;
import com.example.calco.logic.persistent.dao.PDishComponent;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.DishImages;
import com.example.calco.logic.persistent.entities.Image;
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

    public static void persistNewDish(String dishName, Map<Food, Integer> products) {
        if (products.isEmpty()) {
            return;
        }
        // todo change to factory/strategy pattern
        if (!(products.keySet().toArray()[0] instanceof Product)) {
            throw new IllegalArgumentException("Only products can be persisted as dishes' components.");
        }
        List<Map.Entry<Product, Integer>> products2 = new ArrayList<>();
        for (Map.Entry<Food, Integer> entry: products.entrySet()) {
            products2.add(Map.entry((Product)entry.getKey(), entry.getValue()));
        }
        long dishId = persistDish(dishName);
        persistProductsInDish(dishId, getProductsPercentImpact(products2));
    }

    private static long persistDish(String name) {
        PDish pDish = createPDish(name);
        AppDataBase db = AppDataBase.getInstance();
        Long id = db.dishDao().insertAll(pDish).get(0);
        System.out.println( "Dish persisted: " + name + " id: "+ id);
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
        Image image = getImageForDish(pDish);
        if (image != null) {
            return new Dish(pDish.uid, pDish.name, getDishComponents(pDish.uid), image.name);
        }
        return new Dish(pDish.uid, pDish.name, getDishComponents(pDish.uid), ImageLogic.DEFAULT_IMAGE);
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
                distinct().map(DishLogic::getDish).collect(Collectors.toList());

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
        long nextDayMillis = DateTimeConverter.timeToUtcMillis(nextDay.atStartOfDay()) - 1;

        List<HistoryOfDishes> history = getHistoryForPeriod(thisDayMillis, nextDayMillis);
        List<HistoryOfDishes> unitedHistory = FoodLogic.uniteSameFood(history);
        return unitedHistory;
    }

    public static List<HistoryOfDishes> getHistoryForPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate dayAfterEnd = endDate.plusDays(1);
        long startMillis = DateTimeConverter.timeToUtcMillis(startDate.atStartOfDay());
        long endMillis = DateTimeConverter.timeToUtcMillis(dayAfterEnd.atStartOfDay()) - 1;

        List<HistoryOfDishes> history = getHistoryForPeriod(startMillis, endMillis);
        List<HistoryOfDishes> unitedHistory = FoodLogic.uniteSameFood(history);
        return unitedHistory;
    }

    private static List<HistoryOfDishes> getHistoryForPeriod(long startMillis, long endMillis) {
        AppDataBase db = AppDataBase.getInstance();

        List<PHistoryOfDishes> pHistory = db.historyOfDishesDao().getHistoryInDateDiapason(startMillis, endMillis);
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

    public static void setImage(long dishId, String path) {
        List<Image> images = AppDataBase.getInstance().imageDao().getImageByPath(path);
        long imageId = 0;
        if (images.isEmpty()) {
            Image image = new Image();
            image.name = path;
            imageId = AppDataBase.getInstance().imageDao().insertAll(image).get(0);
        }
        else {
            imageId = images.get(0).uid;
        }

        DishImages currentDishImage = AppDataBase.getInstance().dishImagesDao().getDishImages(dishId);
        DishImages newDishImage = getDishImage(dishId, imageId);
        if (currentDishImage == null) {
            AppDataBase.getInstance().dishImagesDao().insertAll(newDishImage);
        } else {
            AppDataBase.getInstance().dishImagesDao().update(newDishImage.imageId, newDishImage.dishId);
        }
    }

    public static DishImages getDishImage(long dishId, long imageId) {
        DishImages dishImage = new DishImages();
        dishImage.dishId = dishId;
        dishImage.imageId = imageId;
        return dishImage;
    }

    public static Image getImageForDish(PDish pDish) {
        DishImages dishImage = AppDataBase.getInstance().dishImagesDao().getDishImages(pDish.uid);
        if (dishImage == null) {
            return null;
        }
        Image image = AppDataBase.getInstance().imageDao().findById(dishImage.imageId);
        return image;
    }
}
