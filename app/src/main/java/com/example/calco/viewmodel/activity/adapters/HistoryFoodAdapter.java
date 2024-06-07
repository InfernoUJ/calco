package com.example.calco.viewmodel.activity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class HistoryFoodAdapter extends RecyclerView.Adapter<HistoryFoodAdapter.HistoryFoodViewHolder> {

    private BiConsumer<View, Integer> dialogHandlerForMassInput;
    static class HistoryFoodViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView calories;
        TextView carbs;
        TextView fats;
        TextView proteins;
        View view;
        public HistoryFoodViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.last_food_image);
            name = itemView.findViewById(R.id.last_food_name);
            calories = itemView.findViewById(R.id.last_food_calories);
            carbs = itemView.findViewById(R.id.last_food_carbs);
            fats = itemView.findViewById(R.id.last_food_fats);
            proteins = itemView.findViewById(R.id.last_food_proteins);
        }
    }

    private List<FoodWithCCFPData> foodList = new ArrayList<>();

    @NonNull
    @Override
    public HistoryFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_food_table_record, parent, false);
        return new HistoryFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryFoodViewHolder holder, int position) {
        FoodWithCCFPData food = foodList.get(position);
        if (food.getImage() != null) {
            holder.image.setImageBitmap(food.getImage());
        } else {
            holder.image.setImageResource(food.getDefaultImageId());
        }
        holder.name.setText(food.getName());
        holder.calories.setText(food.getCalories());
        holder.carbs.setText(food.getCarbs());
        holder.fats.setText(food.getFats());
        holder.proteins.setText(food.getProteins());
        holder.view.setOnClickListener(v -> dialogHandlerForMassInput.accept(v, position));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void replaceFoodList(List<FoodWithCCFPData> foodList) {
        System.out.println( "Replacing recent food " + foodList);
        this.foodList.clear();
        this.foodList.addAll(foodList);
        notifyDataSetChanged();
    }

    public void setDialogHandlerForMassInput(BiConsumer<View, Integer> dialogHandlerForMassInput) {
        this.dialogHandlerForMassInput = dialogHandlerForMassInput;
    }
}
