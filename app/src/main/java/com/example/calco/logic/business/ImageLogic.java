package com.example.calco.logic.business;

import com.example.calco.logic.business.entities.Food;
import com.example.calco.logic.business.entities.Product;

public class ImageLogic {
    public static void setImage(Food food, String path) {
        if(food instanceof Product){
            Product product = (Product) food;
        }
    }
}
