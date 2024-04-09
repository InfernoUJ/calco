package com.example.calco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.ui.pickers.data.DatePickerFragment;
import com.example.calco.ui.products.table.ProductImpactRecordData;
import com.example.calco.ui.products.table.ProductTableFragment;
import com.example.calco.viewmodel.activity.AddFoodVM;
import com.example.calco.viewmodel.activity.MainVM;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calco.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener {
    public static final String dateFormat = "yyyy-MM-dd";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private MainVM model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDataBase.createInstance(getApplicationContext());

        model = new ViewModelProvider(this).get(MainVM.class);

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

        setHandlers();

        setDateToField(LocalDate.now());

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

    @Override
    protected void onStart() {
        super.onStart();
        updateFoodTable();
    }

    // TODO maybe create base class, so this method will call all methods annotated with my annotation
    private void setHandlers() {
        setDateChoosingHandlers();
        setAddingProductHandlers();
        setFoodTableHandler();
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
            Bundle bundle = new Bundle();
            bundle.putString("date", getDate());
            addProductactivityIntent.putExtras(bundle);
            startActivity(addProductactivityIntent);
        });
    }

    private void setFoodTableHandler() {
        ProductTableFragment productTable = (ProductTableFragment) getSupportFragmentManager().findFragmentById(R.id.product_table_fragment);
        model.getFoodRecords().observe(this, productImpactRecordDataList -> {
            productTable.replaceProducts(productImpactRecordDataList);
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int year, int month, int day) {
        LocalDate chosenDate = LocalDate.of(year, month, day);
        setDateToField(chosenDate);
        updateFoodTable();
    }

    private void updateFoodTable() {
        model.updateFoodTable(getLocalDate(), getResources(), getPackageName());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private void setDateToField(LocalDate date) {
        ((TextView)binding.appBarMain.getRoot().findViewById(R.id.dateTextView)).setText(date.format(DateTimeFormatter.ofPattern(dateFormat)));
    }

    private String getDate() {
        return ((TextView)binding.appBarMain.getRoot().findViewById(R.id.dateTextView)).getText().toString();
    }

    private LocalDate getLocalDate() {
        return LocalDate.parse(getDate(), DateTimeFormatter.ofPattern(MainActivity.dateFormat));
    }
}