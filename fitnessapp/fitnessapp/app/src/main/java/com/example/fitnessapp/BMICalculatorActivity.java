package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BMICalculatorActivity extends AppCompatActivity {

    EditText etHeight, etWeight;
    TextView tvResult;
    DatabaseHelper db;

    @SuppressLint({"MissingInflatedId", "DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        db = new DatabaseHelper(this);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        Button btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(v -> {
            try {
                float weight = Float.parseFloat(etWeight.getText().toString());
                float height = Float.parseFloat(etHeight.getText().toString()) / 100;
                if (height > 0) {
                    float bmi = weight / (height * height);
                    String category = getBmiCategory(bmi);
                    tvResult.setText(String.format("BMI: %.1f — %s", bmi, category));
                }
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid input");
            }
        });

        Button btnLogWeight = findViewById(R.id.btnLogWeight);
        btnLogWeight.setOnClickListener(v -> {
            String weightStr = etWeight.getText().toString();
            if (weightStr.isEmpty()) {
                Toast.makeText(this, "Enter weight first", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float weight = Float.parseFloat(weightStr);
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                ContentValues values = new ContentValues();
                values.put("weight", weight);
                values.put("date", currentDate);

                long id = db.getWritableDatabase().insert("weight_logs", null, values);
                if (id != -1) {
                    Toast.makeText(this, "Weight logged for today!", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to main after logging
                } else {
                    Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid weight value", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getBmiCategory(float bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }
}
