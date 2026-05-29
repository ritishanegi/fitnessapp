package com.example.fitnessapp;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    ArrayList<FoodItem> list;
    Runnable onChanged;

    public FoodAdapter(ArrayList<FoodItem> list, Runnable onChanged) {
        this.list = list;
        this.onChanged = onChanged;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        FoodItem f = list.get(position);
        h.name.setText(f.name);
        h.calories.setText(f.calories + " kcal");
        h.macros.setText("P: " + f.protein + "g  C: " + f.carbs + "g  F: " + f.fats + "g");
        h.delete.setOnClickListener(v -> {
            list.remove(h.getAdapterPosition());
            notifyItemRemoved(h.getAdapterPosition());
            onChanged.run();
        });
    }

    @Override public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, calories, macros, delete;
        ViewHolder(View v) {
            super(v);
            name     = v.findViewById(R.id.tvFoodName);
            calories = v.findViewById(R.id.tvFoodCalories);
            macros   = v.findViewById(R.id.tvFoodMacros);
            delete   = v.findViewById(R.id.btnDeleteFood);
        }
    }
}