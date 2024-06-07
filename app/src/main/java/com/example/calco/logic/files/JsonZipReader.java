package com.example.calco.logic.files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import android.content.Context;
import android.net.Uri;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PHistoryOfDishes;
import com.example.calco.logic.persistent.entities.PHistoryOfProducts;
import com.example.calco.logic.persistent.entities.PLimit;
import com.example.calco.logic.persistent.entities.PProduct;
import com.example.calco.logic.persistent.entities.ProductsInDishes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class JsonZipReader {
    public static <T> List<T> deserialize(Class<T> clazz, ZipFile zipFile) {
        try {
            Type listClass = TypeToken.getParameterized(List.class, clazz).getType();
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String nameWithoutExtension = entry.getName().replace(".json", "");
                if (nameWithoutExtension.equals(JsonFileName.getName(clazz))) {
                    InputStream inputStream = zipFile.getInputStream(entry);
                    byte[] bytes = toByteArray(inputStream);
                    return new Gson().fromJson(new String(bytes, StandardCharsets.UTF_8), listClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static void addHistory(Context context, Uri zipUri) {
        AppDataBase db = AppDataBase.getInstance();
        db.runInTransaction(() -> {
            ZipFile zipFile = null;
            try {
                zipFile = getZipFile(context, zipUri);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Map<Long, Long> productIds = addProducts(zipFile);
            Map<Long, Long> dishesIds = addDishes(zipFile);
            addHistoryOfProducts(zipFile, productIds);
            addHistoryOfDishes(zipFile, dishesIds);
            addLimits(zipFile);
            addProductsInDishes(zipFile, productIds, dishesIds);
            System.out.println( "History imported");
        });
    }

    private static ZipFile getZipFile(Context context, Uri uri) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("temp_history"+ LocalDateTime.now(), ".zip", outputDir);

        InputStream is = context.getContentResolver().openInputStream(uri);
        Files.copy(is, outputFile.toPath(), REPLACE_EXISTING);

        return new ZipFile(outputFile);
    }

    private static Map<Long, Long> addProducts(ZipFile zipFile) {
        List<PProduct> products = deserialize(PProduct.class, zipFile);
        Map<Long, Long> productIds = new HashMap<>();
        AppDataBase db = AppDataBase.getInstance();

        for (PProduct product : products) {
            long previousUid = product.uid;
            product.uid = 0;
            long newUid = db.productDao().insertAll(product).get(0);
            productIds.put(previousUid, newUid);
        }

        return productIds;
    }

    private static Map<Long, Long> addDishes(ZipFile zipFile) {
        List<PDish> dishes = deserialize(PDish.class, zipFile);
        Map<Long, Long> dishesIds = new HashMap<>();
        AppDataBase db = AppDataBase.getInstance();

        for (PDish dish : dishes) {
            long previousUid = dish.uid;
            dish.uid = 0;
            long newUid = db.dishDao().insertAll(dish).get(0);
            dishesIds.put(previousUid, newUid);
        }

        return dishesIds;
    }

    private static void addLimits(ZipFile zipFile) {
        List<PLimit> limits = deserialize(PLimit.class, zipFile);
        AppDataBase db = AppDataBase.getInstance();

        limits.forEach(limit -> limit.uid = 0);
        db.limitDao().insertAll(limits.toArray(new PLimit[0]));
    }

    private static void addHistoryOfDishes(ZipFile zipFile, Map<Long, Long> dishesIds) {
        List<PHistoryOfDishes> dishHistory = deserialize(PHistoryOfDishes.class, zipFile);
        AppDataBase db = AppDataBase.getInstance();

        dishHistory.forEach(history -> {history.dishId = dishesIds.get(history.dishId);
                                        history.uid = 0;});
        db.historyOfDishesDao().insertAll(dishHistory.toArray(new PHistoryOfDishes[0]));
    }

    private static void addHistoryOfProducts(ZipFile zipFile, Map<Long, Long> productIds) {
        List<PHistoryOfProducts> productHistory = deserialize(PHistoryOfProducts.class, zipFile);
        AppDataBase db = AppDataBase.getInstance();

        productHistory.forEach(history -> {history.productId = productIds.get(history.productId);
                                            history.uid = 0;});
        db.historyOfProductsDao().insertAll(productHistory.toArray(new PHistoryOfProducts[0]));
    }

    private static void addProductsInDishes(ZipFile zipFile, Map<Long, Long> productIds, Map<Long, Long> dishesIds) {
        List<ProductsInDishes> productsInDishes = deserialize(ProductsInDishes.class, zipFile);
        AppDataBase db = AppDataBase.getInstance();

        productsInDishes.forEach(productInDish -> {
            productInDish.productId = productIds.get(productInDish.productId);
            productInDish.dishId = dishesIds.get(productInDish.dishId);
        });
        db.productsInDishesDao().insertAll(productsInDishes.toArray(new ProductsInDishes[0]));
    }
}
