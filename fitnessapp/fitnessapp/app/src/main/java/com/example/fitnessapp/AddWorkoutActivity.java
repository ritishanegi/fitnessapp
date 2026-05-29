package com.example.fitnessapp;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddWorkoutActivity extends AppCompatActivity {

    Spinner spinnerWorkout;
    EditText etDuration;
    Button btnSaveWorkout;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        spinnerWorkout = findViewById(R.id.spinnerWorkout);
        etDuration = findViewById(R.id.etDuration);
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout);

        db = new DatabaseHelper(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String[] workouts = {"Walking", "Running", "Cycling"};
        spinnerWorkout.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, workouts));

        btnSaveWorkout.setOnClickListener(v -> {
            try {
                int duration = Integer.parseInt(etDuration.getText().toString());
                int calories = duration * 5;

                ContentValues values = new ContentValues();
                values.put("workout_type", spinnerWorkout.getSelectedItem().toString());
                values.put("duration", duration);
                values.put("calories_burned", calories);

                db.getWritableDatabase().insert("workouts", null, values);

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter valid duration", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
