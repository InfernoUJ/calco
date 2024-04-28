package com.example.calco.viewmodel.activity.state;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.example.calco.logic.business.entities.Food;

public interface ImageTwoTypes {
    void setImage(Resources resources, String packageName);
    String getImageName();

}
