package com.example.calco.viewmodel.activity.state;

import android.content.res.Resources;

import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.Product;
import com.example.calco.ui.products.table.FoodImpactRecordData;

public class ImageTwoTypesFactory {
    public static <T extends ImageTwoTypes> T  createImageTwoTypes(Class<T> clazz, Resources resources, String packageName, Object... constructorArgs) {
        T toReturn = null;
        if (clazz.equals(FoodImpactRecordData.class)) {
            toReturn = (T) new FoodImpactRecordData((Food) constructorArgs[0], (String) constructorArgs[1], (Integer) constructorArgs[2], (Integer) constructorArgs[3]);
        }
        else if (DishWithCCFPData.class.isAssignableFrom(clazz)) {
            toReturn = (T) new DishWithCCFPData((Dish) constructorArgs[0], (String) constructorArgs[1], (String) constructorArgs[2], (String) constructorArgs[3], (String) constructorArgs[4], (String) constructorArgs[5]);
        }
        else if (ProductWithCCFPData.class.isAssignableFrom(clazz)) {
            toReturn = (T) new ProductWithCCFPData((Product) constructorArgs[0], (String) constructorArgs[1], (String) constructorArgs[2], (String) constructorArgs[3], (String) constructorArgs[4], (String) constructorArgs[5]);
        }
        toReturn.setImage(resources, packageName);
        return toReturn;
    }
}
