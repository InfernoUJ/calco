package com.example.calco.viewmodel.activity;

import androidx.lifecycle.ViewModel;

import com.example.calco.logic.business.ImageLogic;
import com.example.calco.ui.products.table.FoodImpactRecordData;

import java.time.LocalDateTime;

public class ImageVM extends ViewModel {

    public void setImage(FoodImpactRecordData food, String path) {
        System.out.println( "Image setting: "+food.getName()+" "+path);
        ImageLogic.setImage(food.getFood(), path);
    }

    public static String generateFileName(String name) {
        return name+"_"+ LocalDateTime.now()+".png";
    }
}
