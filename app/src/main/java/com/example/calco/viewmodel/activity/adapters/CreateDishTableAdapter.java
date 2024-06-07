package com.example.calco.viewmodel.activity.adapters;

import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.function.BiConsumer;

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
    private BiConsumer<FoodWithCCFPData, Integer> massSaver;

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
        holder.mass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int mass = Integer.parseInt(s.toString());
                    massSaver.accept(food, mass);
                } catch (NumberFormatException e) {
                }
            }
        });
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

    public void setMassSaver(BiConsumer<FoodWithCCFPData, Integer> massSaver) {
        this.massSaver = massSaver;
    }
}
