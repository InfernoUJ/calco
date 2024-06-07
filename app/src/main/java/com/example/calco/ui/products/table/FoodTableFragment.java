package com.example.calco.ui.products.table;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.databinding.FragmentFoodTableBinding;
import com.example.calco.ui.dialogs.WayToChooseImageDialog;
import com.example.calco.viewmodel.activity.FoodTableVM;

import java.time.LocalDate;

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

    public void replaceProducts(LocalDate startDate, LocalDate endDate, Resources resources, String packageName) {
        foodModel.updateFoodTable(startDate, endDate, resources, packageName);
    }
    
    private void setDialogHandlerForImageSelection(View view, FoodImpactRecordData food) {
        view.setOnClickListener(v -> {
            DialogFragment dialog = new WayToChooseImageDialog(food);
            dialog.show(getParentFragmentManager(), "wayToChooseImageDialog");
        });
    }
}
