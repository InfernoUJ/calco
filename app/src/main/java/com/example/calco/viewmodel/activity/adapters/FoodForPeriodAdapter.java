package com.example.calco.viewmodel.activity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.ui.products.table.FoodImpactRecordData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class FoodForPeriodAdapter extends RecyclerView.Adapter<FoodForPeriodAdapter.FoodForPeriodHolder> {

    private BiConsumer<View, FoodImpactRecordData> dialogHandlerForImageSelection;
    static class FoodForPeriodHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView percent;
        TextView absolute;
        View view;
        public FoodForPeriodHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            percent = itemView.findViewById(R.id.product_percent);
            absolute = itemView.findViewById(R.id.product_absolute);
        }
    }

    private List<FoodImpactRecordData> foodList = new ArrayList<>();

    @NonNull
    @Override
    public FoodForPeriodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_table_record, parent, false);
        return new FoodForPeriodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodForPeriodHolder holder, int position) {
        FoodImpactRecordData food = foodList.get(position);
        if (food.getImage() != null) {
            holder.image.setImageBitmap(food.getImage());
        } else {
            holder.image.setImageResource(food.getDefaultImageId());
        }
        holder.image.setOnClickListener(v -> dialogHandlerForImageSelection.accept(v, food));
        holder.name.setText(food.getName());
        holder.percent.setText(getPercentageOfRecordAsString(food));
        holder.absolute.setText(getAbsoluteOfRecordAsString(food));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void replaceFoodList(List<FoodImpactRecordData> foodList) {
        this.foodList.clear();
        this.foodList.addAll(foodList);
        notifyDataSetChanged();
    }

    public void setDialogHandlerForImageSelection(BiConsumer<View, FoodImpactRecordData> dialogHandlerForImageSelection) {
        this.dialogHandlerForImageSelection = dialogHandlerForImageSelection;
    }

    private String getPercentageOfRecordAsString(FoodImpactRecordData foodImpactRecordData) {
        return foodImpactRecordData.getPercentage() + "%";
    }

    private String getAbsoluteOfRecordAsString(FoodImpactRecordData foodImpactRecordData) {
        return foodImpactRecordData.getAbsoluteValue() + "g";
    }
}
