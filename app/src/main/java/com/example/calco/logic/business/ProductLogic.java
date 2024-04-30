package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.HistoryOfProducts;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.logic.persistent.converters.DateTimeConverter;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.Image;
import com.example.calco.logic.persistent.entities.PHistoryOfProducts;
import com.example.calco.logic.persistent.entities.PProduct;
import com.example.calco.logic.persistent.entities.ProductImages;
import com.example.calco.network.entities.WebProduct;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductLogic {

    // todo add image persistence
    public static long persistNewProduct(Product product) {
        // Save product to database
        PProduct pProduct = getPProduct(product);
        AppDataBase db = AppDataBase.getInstance();
        long uid = db.productDao().insertAll(pProduct).get(0);
        System.out.println("Product persisted: " + product.getName());
        return uid;
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
        Image image = getImageForProduct(pProduct);
        if (image != null) {
            return new Product(pProduct.uid, pProduct.name, pProduct.calories, pProduct.carbs, pProduct.fats, pProduct.proteins, image.name);
        }
        return new Product(pProduct.uid, pProduct.name, pProduct.calories, pProduct.carbs, pProduct.fats, pProduct.proteins, ImageLogic.DEFAULT_IMAGE);
    }

    public static List<Product> getLastUsedProducts() {
        AppDataBase db = AppDataBase.getInstance();
        List<PProduct> lastProducts = db.historyOfProductsDao().getLastUsedProducts();
        List<PProduct> productsAlphabetical = db.productDao().getProductsAlphabetical();

        // todo can make null-termination it in querry method ?
        //  or create my own annotation for query methods

        List<Product> allProducts = Stream.concat(lastProducts.stream(), productsAlphabetical.stream()).
                distinct().map(ProductLogic::getProduct).collect(Collectors.toList());

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
        long nextDayMillis = DateTimeConverter.timeToUtcMillis(nextDay.atStartOfDay()) - 1;

        List<HistoryOfProducts> history = getHistoryForPeriod(thisDayMillis, nextDayMillis);
        List<HistoryOfProducts> unitedHistory = FoodLogic.uniteSameFood(history);
        return unitedHistory;
    }

    public static List<HistoryOfProducts> getHistoryForPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate dayAfterEnd = endDate.plusDays(1);
        long startMillis = DateTimeConverter.timeToUtcMillis(startDate.atStartOfDay());
        long endMillis = DateTimeConverter.timeToUtcMillis(dayAfterEnd.atStartOfDay()) - 1;

        List<HistoryOfProducts> history = getHistoryForPeriod(startMillis, endMillis);
        List<HistoryOfProducts> unitedHistory = FoodLogic.uniteSameFood(history);
        return unitedHistory;
    }

    private static List<HistoryOfProducts> getHistoryForPeriod(long startMillis, long endMillis) {
        AppDataBase db = AppDataBase.getInstance();
        List<PHistoryOfProducts> pHistory = db.historyOfProductsDao().getHistoryInDateDiapason(startMillis, endMillis);
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

    public static void persistWebProductInHistory(Product product, int mass, LocalDate date) {
        long productId = isProductExist(product);
        if (productId == -1) {
            productId = persistNewProduct(product);
        }
        persistProductHistory(new Product(productId, product), mass, date);
    }

    private static long isProductExist(Product product) {
        AppDataBase db = AppDataBase.getInstance();
        List<PProduct> products = db.productDao().findByAll(product.getName(), product.getCalories(), product.getCarbs(), product.getFats(), product.getProteins());
        return products.size() > 0 ? products.get(0).uid : -1;
    }

    public static void setImage(long productId, String path) {
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

        ProductImages currentProductImage = AppDataBase.getInstance().productImagesDao().getProductImages(productId);
        ProductImages newProductImage = getProductImage(productId, imageId);
        if (currentProductImage == null) {
            AppDataBase.getInstance().productImagesDao().insertAll(newProductImage);
        } else {
            AppDataBase.getInstance().productImagesDao().update(newProductImage.imageId, newProductImage.productId);
        }
    }

    public static ProductImages getProductImage(long productId, long imageId) {
        ProductImages productImage = new ProductImages();
        productImage.productId = productId;
        productImage.imageId = imageId;
        return productImage;
    }

    public static Image getImageForProduct(PProduct pProduct) {
        ProductImages productImages = AppDataBase.getInstance().productImagesDao().getProductImages(pProduct.uid);
        if (productImages == null) {
            return null;
        }
        Image image = AppDataBase.getInstance().imageDao().findById(productImages.imageId);
        return image;
    }
}
