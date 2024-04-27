package com.example.calco;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.ui.products.table.FoodImpactRecordData;
import com.example.calco.viewmodel.activity.ImageVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class TakingPictureActivity extends AppCompatActivity {
    private static final String IMAGE_DIR = "imageDir";
    private FoodImpactRecordData food;
    private ImageVM imageModel;
    private final ActivityResultLauncher<Void> mGetPictureProvider =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {
                        String path = saveToInternalStorage(bitmap, food.getName());
                        imageModel.setImage(food, path);
                        finish();
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        food = (FoodImpactRecordData) getIntent().getSerializableExtra("food");
        imageModel = new ViewModelProvider(this).get(ImageVM.class);
        mGetPictureProvider.launch(null);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        File myPath = new File(directory, generateFileName(name));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Image saving went wrong", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }

    private String generateFileName(String name) {
        return name+"_"+LocalDateTime.now()+".png";
    }
}