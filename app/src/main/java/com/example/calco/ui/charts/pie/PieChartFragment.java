package com.example.calco.ui.charts.pie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.databinding.FragmentPieChartBinding;

public class PieChartFragment extends Fragment {
    private FragmentPieChartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PieChartViewModel galleryViewModel =
                new ViewModelProvider(this).get(PieChartViewModel.class);

        binding = FragmentPieChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
