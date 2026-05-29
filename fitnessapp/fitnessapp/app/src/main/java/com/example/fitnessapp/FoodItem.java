package com.example.fitnessapp;

public class FoodItem {
    public String name;
    public int calories, protein, carbs, fats;

    public FoodItem(String name, int calories, int protein, int carbs, int fats) {
        this.name     = name;
        this.calories = calories;
        this.protein  = protein;
        this.carbs    = carbs;
        this.fats     = fats;
    }
}