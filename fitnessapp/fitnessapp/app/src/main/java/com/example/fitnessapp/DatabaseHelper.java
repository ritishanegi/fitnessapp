package com.example.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "FitnessDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE meals(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "meal_name TEXT," +
                "calories INTEGER," +
                "date TEXT)");

        db.execSQL("CREATE TABLE workouts(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "workout_type TEXT," +
                "duration INTEGER," +
                "calories_burned INTEGER)");

        db.execSQL("CREATE TABLE weight_logs(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "weight REAL," +
                "date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS weight_logs(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "weight REAL," +
                    "date TEXT)");
        }
    }
}