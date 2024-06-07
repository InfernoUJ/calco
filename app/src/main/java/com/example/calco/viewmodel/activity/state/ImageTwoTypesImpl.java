package com.example.calco.viewmodel.activity.state;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.example.calco.logic.business.ImageLogic;

import java.io.File;
import java.nio.file.Files;

public abstract class ImageTwoTypesImpl implements ImageTwoTypes{
    protected Integer defaultImageId;
    protected Bitmap image;

    public Integer getDefaultImageId() {
        return defaultImageId;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public void setImage(Resources resources, String packageName) {
        defaultImageId = -1;
        image = null;
        if (getImageName().equals(ImageLogic.DEFAULT_IMAGE)) {
            defaultImageId = resources.getIdentifier(getImageName() , "drawable", packageName);
        }
        else {
            image = loadImage(getImageName());
        }
    }

    @Nullable
    private static Bitmap loadImage(String imagePath) {
        File file = new File(imagePath);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(Files.newInputStream(file.toPath()));
        }
        finally {
            return bitmap;
        }
    }
}
