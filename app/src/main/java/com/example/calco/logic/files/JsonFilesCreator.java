package com.example.calco.logic.files;

import com.example.calco.logic.persistent.entities.*;

public class JsonFilesCreator {
    public static JsonFile[] createJsonFiles() {
        JsonFile[] jsonFiles = new JsonFile[] {
                new JsonFile(JsonFileName.DishImages, JsonSerializer.serialize(DishImages.class)),
                new JsonFile(JsonFileName.Images, JsonSerializer.serialize(Image.class)),
                new JsonFile(JsonFileName.PDish, JsonSerializer.serialize(PDish.class)),
                new JsonFile(JsonFileName.PHistoryOfDishes, JsonSerializer.serialize(PHistoryOfDishes.class)),
                new JsonFile(JsonFileName.PHistoryOfProducts, JsonSerializer.serialize(PHistoryOfProducts.class)),
                new JsonFile(JsonFileName.PLimits, JsonSerializer.serialize(PLimit.class)),
                new JsonFile(JsonFileName.PProduct, JsonSerializer.serialize(PProduct.class)),
                new JsonFile(JsonFileName.ProductImages, JsonSerializer.serialize(ProductImages.class)),
                new JsonFile(JsonFileName.ProductsInDishes, JsonSerializer.serialize(ProductsInDishes.class))
        };

        return jsonFiles;
    }
}
