package com.example.fitnessapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddMealActivity extends AppCompatActivity {

    EditText etMeal, etCalories;
    Button btnSaveMeal;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        etMeal = findViewById(R.id.etMeal);
        etCalories = findViewById(R.id.etCalories);
        btnSaveMeal = findViewById(R.id.btnSaveMeal);

        db = new DatabaseHelper(this);

        // Back button
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnSaveMeal.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("meal_name", etMeal.getText().toString());
            values.put("calories", Integer.parseInt(etCalories.getText().toString()));
            values.put("date", "today");

            SQLiteDatabase database = db.getWritableDatabase();
            database.insert("meals", null, values);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
