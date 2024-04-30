package com.example.calco.ui.products.table;

import android.content.Intent;
import android.content.res.Resources;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.TakingPictureActivity;
import com.example.calco.databinding.FragmentFoodTableBinding;
import com.example.calco.ui.dialogs.WayToChooseImageDialog;
import com.example.calco.viewmodel.activity.FoodTableVM;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;

public class FoodTableFragment extends Fragment {
    private FragmentFoodTableBinding binding;
    private FoodTableVM foodModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        foodModel = new ViewModelProvider(requireActivity()).get(FoodTableVM.class);
        recyclerView = root.findViewById(R.id.product_table_scroll_view);
        setFoodForPeriodAdapter();
        return root;
    }

    private void setFoodForPeriodAdapter() {
        recyclerView.setAdapter(foodModel.getAdapter(this::setDialogHandlerForImageSelection));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

//    public void addProduct(FoodImpactRecordData foodImpactRecordData) {
////        View productRow = getLayoutInflater().inflate(R.layout.product_table_record, binding.productTableLinearLayout, false);
//        View productRow = getLayoutInflater().inflate(R.layout.product_table_record,null);
//
//        ImageView productImage = productRow.findViewById(R.id.product_image);
//
//        TextView productName = productRow.findViewById(R.id.product_name);
//        TextView productPercentImpact = productRow.findViewById(R.id.product_percent);
//        TextView productAbsoluteImpact = productRow.findViewById(R.id.product_absolute);
//
//        if (foodImpactRecordData.getImage() != null) {
//            productImage.setImageBitmap(foodImpactRecordData.getImage());
//        } else {
//            productImage.setImageResource(foodImpactRecordData.getDefaultImageId());
//        }
//        productName.setText(foodImpactRecordData.getName());
//        productPercentImpact.setText(getPercentageOfRecordAsString(foodImpactRecordData));
//        productAbsoluteImpact.setText(foodImpactRecordData.getAbsoluteValue().toString());
//
//        binding.foodTableLinearLayout.addView(productRow);
//
//        System.out.println("Id: " + binding.foodTableLinearLayout.getId() + " Child count: " + binding.foodTableLinearLayout.getChildCount());
//    }
//
//    public void addProducts(List<FoodImpactRecordData> foodImpactRecordDataList) {
//        for (FoodImpactRecordData foodImpactRecordData : foodImpactRecordDataList) {
//            addProduct(foodImpactRecordData);
//        }
//    }

    public void replaceProducts(LocalDate date, Resources resources, String packageName) {
        foodModel.updateFoodTable(date, resources, packageName);
    }

//    private void removeAllProductRecords() {
//        binding.foodTableLinearLayout.removeAllViews();
//    }

    private void setDialogHandlerForImageSelection(View view, FoodImpactRecordData food) {
        view.setOnClickListener(v -> {
            DialogFragment dialog = new WayToChooseImageDialog(food);
            dialog.show(getParentFragmentManager(), "wayToChooseImageDialog");
        });
    }
}
