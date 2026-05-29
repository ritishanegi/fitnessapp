package com.example.fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CardView cardBmi;
    Button btnLogWeight, btnFood, btnAddWorkout;
    TextView tvCurrentWeight, tvBmiValue, tvBmiStatus;
    LinearLayout logContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        cardBmi = findViewById(R.id.cardBmi);
        btnLogWeight = findViewById(R.id.btnLogWeight);
        btnFood = findViewById(R.id.btnFood);
        btnAddWorkout = findViewById(R.id.btnAddWorkout);
        tvCurrentWeight = findViewById(R.id.tvCurrentWeight);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvBmiStatus = findViewById(R.id.tvBmiStatus);
        logContainer = findViewById(R.id.logContainer);

        if (btnFood != null) {
            btnFood.setOnClickListener(v ->
                    startActivity(new Intent(this, FoodActivity.class)));
        }

        if (btnAddWorkout != null) {
            btnAddWorkout.setOnClickListener(v ->
                    startActivity(new Intent(this, AddWorkoutActivity.class)));
        }

        if (cardBmi != null) {
            cardBmi.setOnClickListener(v ->
                    startActivity(new Intent(this, BMICalculatorActivity.class)));
        }

        if (btnLogWeight != null) {
            btnLogWeight.setOnClickListener(v -> {
                startActivity(new Intent(this, BMICalculatorActivity.class));
            });
        }
        
        loadWeightLogs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWeightLogs();
    }

    private void loadWeightLogs() {
        if (logContainer == null) return;
        
        Cursor cursor = db.getReadableDatabase().query("weight_logs", null, null, null, null, null, "id DESC", "4");
        
        logContainer.removeAllViews();
        
        if (cursor != null && cursor.moveToFirst()) {
            float latestWeight = cursor.getFloat(cursor.getColumnIndexOrThrow("weight"));
            tvCurrentWeight.setText(String.format(Locale.getDefault(), "%.1f kg", latestWeight));
            
            updateBmiSummary(latestWeight);

            do {
                float weight = cursor.getFloat(cursor.getColumnIndexOrThrow("weight"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                
                View itemView = getLayoutInflater().inflate(R.layout.item_weight_log, logContainer, false);
                TextView tvLogDate = itemView.findViewById(R.id.tvLogDate);
                TextView tvLogWeight = itemView.findViewById(R.id.tvLogWeight);
                
                tvLogDate.setText(date);
                tvLogWeight.setText(String.format(Locale.getDefault(), "%.1f kg", weight));
                
                logContainer.addView(itemView);
                
                View divider = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                params.setMargins(40, 0, 40, 0);
                divider.setLayoutParams(params);
                divider.setBackgroundColor(0xFFF0F4F2);
                logContainer.addView(divider);
                
            } while (cursor.moveToNext());
        } else {
            tvCurrentWeight.setText("--");
            tvBmiValue.setText("--");
            tvBmiStatus.setText("No data");
        }
        if (cursor != null) cursor.close();
    }

    private void updateBmiSummary(float weight) {
        float height = 1.75f; // Standard height or should be fetched from user profile
        float bmi = weight / (height * height);
        tvBmiValue.setText(String.format(Locale.getDefault(), "%.1f", bmi));
        
        if (bmi < 18.5) {
            tvBmiStatus.setText("Underweight");
            tvBmiStatus.setTextColor(0xFFFFA000);
        } else if (bmi < 25) {
            tvBmiStatus.setText("Normal");
            tvBmiStatus.setTextColor(0xFF3A9E60);
        } else {
            tvBmiStatus.setText("Overweight");
            tvBmiStatus.setTextColor(0xFFE53935);
        }
    }
}
