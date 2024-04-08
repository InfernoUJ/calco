package com.example.calco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.ui.pickers.data.DatePickerFragment;
import com.example.calco.ui.products.table.ProductImpactRecordData;
import com.example.calco.ui.products.table.ProductTableFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calco.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDataBase.createInstance(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        // this is mail icon action
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        ProductTableFragment productTable = (ProductTableFragment) getSupportFragmentManager().findFragmentById(R.id.product_table_fragment);
        if (productTable != null) {
            for (int i = 0; i < 10; i++) {
                productTable.addProduct(new ProductImpactRecordData("Product " + i, i * 10, i * 100, R.drawable.question_mark));
            }
        }

        setHandlers();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // TODO maybe create base class, so this method will call all methods annotated with my annotation
    private void setHandlers() {
        setDateChoosingHandlers();
        setAddingProductHandlers();
    }

    private void setDateChoosingHandlers() {
        View chooseDateButton = binding.appBarMain.getRoot().findViewById(R.id.chooseDateBtn);
        chooseDateButton.setOnClickListener(view -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }

    private void setAddingProductHandlers() {
        View chooseDateButton = binding.appBarMain.getRoot().findViewById(R.id.addProductBtn);
        chooseDateButton.setOnClickListener(view -> {
            Intent addProductactivityIntent = new Intent(this, AddFoodActivity.class);
            startActivity(addProductactivityIntent);
        });
    }
}