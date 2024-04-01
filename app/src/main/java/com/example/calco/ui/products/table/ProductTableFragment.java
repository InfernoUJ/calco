package com.example.calco.ui.products.table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calco.R;
import com.example.calco.databinding.FragmentProductTableBinding;

import java.util.List;

public class ProductTableFragment extends Fragment {
    private FragmentProductTableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void addProduct(ProductImpactRecordData productImpactRecordData) {
        // todo - what does attach to root do?
        View productRow = LayoutInflater.from(getContext()).inflate(R.layout.product_table_record, binding.productTableLinearLayout, true);
        ImageView productImage = productRow.findViewById(R.id.product_image);
        TextView productName = productRow.findViewById(R.id.product_name);
        TextView productPercentImpact = productRow.findViewById(R.id.product_percent);
        TextView productAbsoluteImpact = productRow.findViewById(R.id.product_absolute);

        productImage.setImageResource(productImpactRecordData.getImageId());
        productName.setText(productImpactRecordData.getName());
        productPercentImpact.setText(productImpactRecordData.getPercentage().toString());
        productAbsoluteImpact.setText(productImpactRecordData.getAbsoluteValue().toString());

    }

    public void addProducts(List<ProductImpactRecordData> productImpactRecordDataList) {
        // todo
    }

    public void replaceProducts(List<ProductImpactRecordData> newProductImpactRecordDataList) {
        // todo
    }


}
