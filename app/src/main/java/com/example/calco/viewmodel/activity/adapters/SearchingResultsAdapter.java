package com.example.calco.viewmodel.activity.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.R;
import com.example.calco.network.entities.WebProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SearchingResultsAdapter extends RecyclerView.Adapter<SearchingResultsAdapter.SearchingResultRecord> {
    private final List<WebProduct> products;
    private BiConsumer<View, WebProduct> dialogHandlerForSearchingRow;
    public class SearchingResultRecord extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView calories;
        private final TextView carbs;
        private final TextView fats;
        private final TextView proteins;
        private final View view;
        private WebProduct productInfo;
        public SearchingResultRecord(View itemView) {
            super(itemView);
            this.view = itemView;
            name = (TextView)itemView.findViewById(R.id.searching_result_record_name);
            calories = (TextView)itemView.findViewById(R.id.searching_result_record_calories);
            carbs = (TextView)itemView.findViewById(R.id.searching_result_record_carbs);
            fats = (TextView)itemView.findViewById(R.id.searching_result_record_fats);
            proteins = (TextView)itemView.findViewById(R.id.searching_result_record_proteins);
        }

        public void setData(WebProduct productInfo) {
            this.productInfo = productInfo;
            dialogHandlerForSearchingRow.accept(view, productInfo);
            name.setText(productInfo.getName());
            calories.setText(String.valueOf(productInfo.getCalories()));
            carbs.setText(String.valueOf(productInfo.getCarbs()));
            fats.setText(String.valueOf(productInfo.getFats()));
            proteins.setText(String.valueOf(productInfo.getProteins()));
        }
    }

    public SearchingResultsAdapter() {
        this.products = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchingResultRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View record = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searching_result_record, parent, false);
        return new SearchingResultRecord(record);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchingResultRecord holder, int position) {
        holder.setData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void replaceProducts(List<WebProduct> newProducts) {
        System.out.println( "Replacing products " + newProducts);
        products.clear();
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    public void setDialogHandlerForSearchingRow(BiConsumer<View, WebProduct> handler) {
        dialogHandlerForSearchingRow = handler;
    }
}
