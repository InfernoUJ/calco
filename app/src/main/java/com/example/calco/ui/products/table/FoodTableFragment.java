package com.example.calco.ui.products.table;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calco.R;
import com.example.calco.databinding.FragmentFoodTableBinding;

import java.util.List;
import java.util.function.Function;

public class FoodTableFragment extends Fragment {
    private FragmentFoodTableBinding binding;
    private ImageView image;

    private final View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("Image clicked");
            mGetPictureProvider.launch(null);
        }
    };

    private final ActivityResultLauncher<Void> mGetPictureProvider =
            registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (getImage() != null) {
                        getImage().setImageBitmap(bitmap);
                    }
                });

    private ImageView getImage() {
        return image;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void addProduct(ProductImpactRecordData productImpactRecordData) {
//        View productRow = getLayoutInflater().inflate(R.layout.product_table_record, binding.productTableLinearLayout, false);
        View productRow = getLayoutInflater().inflate(R.layout.product_table_record,null);
        ImageView productImage = productRow.findViewById(R.id.product_image);
        image = productImage;
        productImage.setOnClickListener(imageClickListener);
        TextView productName = productRow.findViewById(R.id.product_name);
        TextView productPercentImpact = productRow.findViewById(R.id.product_percent);
        TextView productAbsoluteImpact = productRow.findViewById(R.id.product_absolute);

        productImage.setImageResource(productImpactRecordData.getImageId());
        productName.setText(productImpactRecordData.getName());
        productPercentImpact.setText(getPercentageOfRecordAsString(productImpactRecordData));
        productAbsoluteImpact.setText(productImpactRecordData.getAbsoluteValue().toString());

        binding.foodTableLinearLayout.addView(productRow);

        System.out.println("Id: " + binding.foodTableLinearLayout.getId() + " Child count: " + binding.foodTableLinearLayout.getChildCount());
    }

    public void addProducts(List<ProductImpactRecordData> productImpactRecordDataList) {
        for (ProductImpactRecordData productImpactRecordData : productImpactRecordDataList) {
            addProduct(productImpactRecordData);
        }
    }

    public void replaceProducts(List<ProductImpactRecordData> newProductImpactRecordDataList) {
        removeAllProductRecords();
        addProducts(newProductImpactRecordDataList);
    }

    private String getPercentageOfRecordAsString(ProductImpactRecordData productImpactRecordData) {
        return productImpactRecordData.getPercentage() + "%";
    }

    private void removeAllProductRecords() {
        binding.foodTableLinearLayout.removeAllViews();
    }
}
