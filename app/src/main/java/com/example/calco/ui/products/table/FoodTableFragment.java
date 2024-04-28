package com.example.calco.ui.products.table;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calco.R;
import com.example.calco.TakingPictureActivity;
import com.example.calco.databinding.FragmentFoodTableBinding;

import java.util.List;

public class FoodTableFragment extends Fragment {
    private FragmentFoodTableBinding binding;
    private ImageView image;
    private Bitmap bitmap;

    private final ActivityResultLauncher<Void> mGetPictureProvider =
            registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    getImage().setImageBitmap(bitmap);
                    System.out.println("Bitmap set");
                });

    public FoodTableFragment() {
    }

    class ImageClickListener implements View.OnClickListener {
        private final FoodImpactRecordData food;

        ImageClickListener(FoodImpactRecordData food) {
            this.food = food;
        }
        @Override
        public void onClick(View view) {
            System.out.println("Image clicked");
            mGetPictureProvider.launch(null);
        }
    }

    private ImageView getImage() {
        return image;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void addProduct(FoodImpactRecordData foodImpactRecordData) {
//        View productRow = getLayoutInflater().inflate(R.layout.product_table_record, binding.productTableLinearLayout, false);
        View productRow = getLayoutInflater().inflate(R.layout.product_table_record,null);

        ImageView productImage = productRow.findViewById(R.id.product_image);
        productImage.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TakingPictureActivity.class);
            intent.putExtra("food", foodImpactRecordData);
            startActivity(intent);
        });

        TextView productName = productRow.findViewById(R.id.product_name);
        TextView productPercentImpact = productRow.findViewById(R.id.product_percent);
        TextView productAbsoluteImpact = productRow.findViewById(R.id.product_absolute);

        if (foodImpactRecordData.getImage() != null) {
            productImage.setImageBitmap(foodImpactRecordData.getImage());
        } else {
            productImage.setImageResource(foodImpactRecordData.getDefaultImageId());
        }
        productName.setText(foodImpactRecordData.getName());
        productPercentImpact.setText(getPercentageOfRecordAsString(foodImpactRecordData));
        productAbsoluteImpact.setText(foodImpactRecordData.getAbsoluteValue().toString());

        binding.foodTableLinearLayout.addView(productRow);

        System.out.println("Id: " + binding.foodTableLinearLayout.getId() + " Child count: " + binding.foodTableLinearLayout.getChildCount());
    }

    public void addProducts(List<FoodImpactRecordData> foodImpactRecordDataList) {
        for (FoodImpactRecordData foodImpactRecordData : foodImpactRecordDataList) {
            addProduct(foodImpactRecordData);
        }
    }

    public void replaceProducts(List<FoodImpactRecordData> newFoodImpactRecordDataList) {
        removeAllProductRecords();
        addProducts(newFoodImpactRecordDataList);
    }

    private String getPercentageOfRecordAsString(FoodImpactRecordData foodImpactRecordData) {
        return foodImpactRecordData.getPercentage() + "%";
    }

    private void removeAllProductRecords() {
        binding.foodTableLinearLayout.removeAllViews();
    }
}
