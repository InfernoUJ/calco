package com.example.calco;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.logic.business.ImageLogic;
import com.example.calco.ui.products.table.FoodImpactRecordData;
import com.example.calco.viewmodel.activity.ImageVM;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class SelectPictureActivity extends AppCompatActivity {
    private FoodImpactRecordData food;
    private ImageVM imageModel;
    private final ActivityResultLauncher<PickVisualMediaRequest> mSelectPicture =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor =
                                getContentResolver().openFileDescriptor(uri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                        String path = saveToInternalStorage(image, food.getName());
                        imageModel.setImage(food, path);
                        parcelFileDescriptor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Image loading went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                finish();
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        food = (FoodImpactRecordData) getIntent().getSerializableExtra("food");
        imageModel = new ViewModelProvider(this).get(ImageVM.class);
        mSelectPicture.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    // todo unite with the same method in TakingPictureActivity
    private String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(ImageLogic.IMAGE_DIR, Context.MODE_PRIVATE);
        File myPath = new File(directory, ImageVM.generateFileName(name));

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
}