package com.example.fitnessapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {

    // Totals
    int totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFats = 0;

    // UI
    TextView tvCalories, tvCaloriesLeft, tvProtein, tvCarbs, tvFats;
    ProgressBar progressCalories;

    // Food lists per meal
    ArrayList<FoodItem> breakfastList = new ArrayList<>();
    ArrayList<FoodItem> lunchList     = new ArrayList<>();
    ArrayList<FoodItem> dinnerList    = new ArrayList<>();
    ArrayList<FoodItem> snacksList    = new ArrayList<>();

    FoodAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, snacksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Bind summary views
        tvCalories     = findViewById(R.id.tvCaloriesConsumed);
        tvCaloriesLeft = findViewById(R.id.tvCaloriesLeft);
        tvProtein      = findViewById(R.id.tvProtein);
        tvCarbs        = findViewById(R.id.tvCarbs);
        tvFats         = findViewById(R.id.tvFats);
        progressCalories = findViewById(R.id.progressCalories);

        // Setup RecyclerViews
        setupRecyclerView(R.id.rvBreakfast, breakfastList,
                breakfastAdapter = new FoodAdapter(breakfastList, this::updateTotals));
        setupRecyclerView(R.id.rvLunch, lunchList,
                lunchAdapter = new FoodAdapter(lunchList, this::updateTotals));
        setupRecyclerView(R.id.rvDinner, dinnerList,
                dinnerAdapter = new FoodAdapter(dinnerList, this::updateTotals));
        setupRecyclerView(R.id.rvSnacks, snacksList,
                snacksAdapter = new FoodAdapter(snacksList, this::updateTotals));

        // Add buttons
        findViewById(R.id.btnAddBreakfast).setOnClickListener(v ->
                showAddFoodDialog(breakfastList, breakfastAdapter));
        findViewById(R.id.btnAddLunch).setOnClickListener(v ->
                showAddFoodDialog(lunchList, lunchAdapter));
        findViewById(R.id.btnAddDinner).setOnClickListener(v ->
                showAddFoodDialog(dinnerList, dinnerAdapter));
        findViewById(R.id.btnAddSnacks).setOnClickListener(v ->
                showAddFoodDialog(snacksList, snacksAdapter));
    }

    void setupRecyclerView(int id, ArrayList<FoodItem> list, FoodAdapter adapter) {
        RecyclerView rv = findViewById(id);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    void showAddFoodDialog(ArrayList<FoodItem> list, FoodAdapter adapter) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_food);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText etName     = dialog.findViewById(R.id.etFoodName);
        EditText etCalories = dialog.findViewById(R.id.etCalories);
        EditText etProtein  = dialog.findViewById(R.id.etProtein);
        EditText etCarbs    = dialog.findViewById(R.id.etCarbs);
        EditText etFats     = dialog.findViewById(R.id.etFats);

        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnSaveFood).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Enter food name");
                return;
            }
            int cal  = etCalories.getText().toString().isEmpty() ? 0 : Integer.parseInt(etCalories.getText().toString());
            int prot = etProtein.getText().toString().isEmpty()  ? 0 : Integer.parseInt(etProtein.getText().toString());
            int carb = etCarbs.getText().toString().isEmpty()    ? 0 : Integer.parseInt(etCarbs.getText().toString());
            int fat  = etFats.getText().toString().isEmpty()     ? 0 : Integer.parseInt(etFats.getText().toString());

            list.add(new FoodItem(name, cal, prot, carb, fat));
            adapter.notifyItemInserted(list.size() - 1);
            updateTotals();
            dialog.dismiss();
        });

        dialog.show();
    }

    void updateTotals() {
        totalCalories = 0; totalProtein = 0; totalCarbs = 0; totalFats = 0;
        for (ArrayList<FoodItem> list : new ArrayList[]{breakfastList, lunchList, dinnerList, snacksList}) {
            for (FoodItem f : list) {
                totalCalories += f.calories;
                totalProtein  += f.protein;
                totalCarbs    += f.carbs;
                totalFats     += f.fats;
            }
        }
        tvCalories.setText(String.valueOf(totalCalories));
        tvCaloriesLeft.setText((2000 - totalCalories) + " left");
        tvProtein.setText(totalProtein + "g");
        tvCarbs.setText(totalCarbs + "g");
        tvFats.setText(totalFats + "g");
        progressCalories.setProgress(Math.min(totalCalories, 2000));
    }
}