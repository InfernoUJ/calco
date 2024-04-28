package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.Dish;
import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.Product;

public class ImageLogic {
    public static void setImage(Food food, String path) {
        long foodId = food.getId();
        // todo relegate the logic yo separate interfaces
        //  and add factory provider based on food type
        if(food instanceof Product){
            ProductLogic.setImage(foodId, path);
        }
        else if (food instanceof Dish){
            DishLogic.setImage(foodId, path);
        }
        else {
            throw new IllegalArgumentException("Unknown food type");
        }
    }
}
