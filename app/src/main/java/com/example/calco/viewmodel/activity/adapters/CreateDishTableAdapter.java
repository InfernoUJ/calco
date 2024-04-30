package com.example.calco.viewmodel.activity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.viewmodel.activity.state.FoodWithCCFPData;

import java.util.ArrayList;
import java.util.List;

public class CreateDishTableAdapter extends RecyclerView.Adapter<CreateDishTableAdapter.CreateDishTableHolder> {
    static class CreateDishTableHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView calories;
        TextView carbs;
        TextView fats;
        TextView proteins;
        EditText mass;
        View view;
        public CreateDishTableHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.component_image);
            name = itemView.findViewById(R.id.component_name);
            calories = itemView.findViewById(R.id.component_calories);
            carbs = itemView.findViewById(R.id.component_carbs);
            fats = itemView.findViewById(R.id.component_fats);
            proteins = itemView.findViewById(R.id.component_proteins);
            mass = itemView.findViewById(R.id.amount_in_g);
        }
    }

    private List<FoodWithCCFPData> foodList = new ArrayList<>();

    @NonNull
    @Override
    public CreateDishTableAdapter.CreateDishTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_of_dish, parent, false);
        return new CreateDishTableAdapter.CreateDishTableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateDishTableAdapter.CreateDishTableHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void replaceFoodList(List<FoodWithCCFPData> foodList) {
        System.out.println("Replacing recent food " + foodList);
        this.foodList.clear();
        this.foodList.addAll(foodList);
        notifyDataSetChanged();
    }
}
