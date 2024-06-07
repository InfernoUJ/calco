package com.example.calco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.network.entities.WebProduct;
import com.example.calco.ui.dialogs.MassInputDialog;
import com.example.calco.viewmodel.activity.AddFoodVM;
import com.example.calco.viewmodel.activity.SearchVM;

public class AddFoodActivity extends AppCompatActivity implements MassInputDialog.MassInputDialogListener,
        SearchView.OnQueryTextListener {

    private AddFoodVM model;
    private SearchVM searchModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_food_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        model = new ViewModelProvider(this).get(AddFoodVM.class);
        searchModel = new ViewModelProvider(this).get(SearchVM.class);

        setHandlers();
        setUp();
    }

    // why onResume wasn't being called? - idk
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println( new Boolean(AppDataBase.getInstance() == null).toString());
        model.updateLastUsedFood(getResources(), getPackageName());
    }

    private void setHandlers() {
        setLastUsedFoodHandler();
        setCreateProductButtonHandler();
        setCreateDishButtonHandler();
    }

    private void setUp() {
        setUpSearching();
    }

    private void setCreateProductButtonHandler() {
        View createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener(view -> {
            Intent createProductActivityIntent = new Intent(this, CreateProductActivity.class);
            startActivity(createProductActivityIntent);
        });
    }

    private void setCreateDishButtonHandler() {
        View createDishBtn = findViewById(R.id.createDishBtn);
        createDishBtn.setOnClickListener(view -> {
            Intent createDishActivityIntent = new Intent(this, CreateDishActivity.class);
            startActivity(createDishActivityIntent);
        });
    }

    private void setLastUsedFoodHandler() {
        RecyclerView recyclerView = findViewById(R.id.lastFoodTableScrollView);
        recyclerView.setAdapter(model.getAdapter(this::setDialogHandlerForFoodRow));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpSearching() {
        setUpSearchingBar();
        setUpSearchingResults();
    }

    private void setUpSearchingBar() {
        SearchView searchView = findViewById(R.id.foodSearchView);
        searchView.setOnQueryTextListener(this);
    }

    private void setUpSearchingResults() {
        RecyclerView recyclerView = findViewById(R.id.searchingFoodResults);
        recyclerView.setAdapter(searchModel.getAdapter(this::setDialogHandlerForSearchingRow));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDialogHandlerForFoodRow(View view, int foodIndex) {
        view.setOnClickListener(v -> {
            DialogFragment dialogFragment = new MassInputDialog(foodIndex);
            dialogFragment.show(getSupportFragmentManager(), "massInputDialog");
        });
    }

    private void setDialogHandlerForSearchingRow(View view, WebProduct product) {
        view.setOnClickListener(v -> {
            DialogFragment dialogFragment = new MassInputDialog(product);
            dialogFragment.show(getSupportFragmentManager(), "massInputDialog");
        });
    }

    @Override
    public void onDialogPositiveClick(MassInputDialog dialog) {
        // User touched the dialog's positive button
        System.out.println( "Mass: " + dialog.getMass() + " time form bundle: " + getIntent().getExtras().getString("date"));

        model.addFoodToHistory(dialog.getIndex(), dialog.getMass(), dialog.getProduct(), getIntent().getExtras().getString("date"));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        // do nothing
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println( "onQueryTextSubmit: " + query);
        searchModel.searchFood(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println( "onQueryTextChange: " + newText);
        searchModel.searchFood(newText);
        return false;
    }
}