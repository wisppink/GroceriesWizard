package com.example.grocerieswizard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubRecipeRecyclerViewAdapter extends RecyclerView.Adapter<SubRecipeRecyclerViewAdapter.ViewHolder> {

    private Context context;
    Map<String, Map<String, Double>> ingredientInfo;
    private ArrayList<String> recipeNames;
    private ArrayList<String> ingredientUnit;
    private ArrayList<Double> ingredientQuantity;
    private String TAG = "subRecipeAdapter";

    public SubRecipeRecyclerViewAdapter(Context context) {
        this.context = context;
        recipeNames = new ArrayList<>();
        ingredientUnit = new ArrayList<>();
        ingredientQuantity = new ArrayList<>();

    }


    public void setIngredientInfo(Map<String, Map<String, Double>> ingredientInfo) {
        this.ingredientInfo = ingredientInfo;
        reArrange();
    }

    private void reArrange() {
        // Clear existing data
        recipeNames.clear();
        ingredientUnit.clear();
        ingredientQuantity.clear();

        // Check if ingredientInfo is not null and not empty
        if (ingredientInfo != null && !ingredientInfo.isEmpty()) {
            for (Map.Entry<String, Map<String, Double>> entry : ingredientInfo.entrySet()) {
                String recipeName = entry.getKey();
                Map<String, Double> ingredientData = entry.getValue();

                // Iterate through the inner map to extract unit and quantity
                for (Map.Entry<String, Double> innerEntry : ingredientData.entrySet()) {
                    String unit = innerEntry.getKey();
                    Double quantity = innerEntry.getValue();

                    // Add the extracted data to the respective lists
                    recipeNames.add(recipeName);
                    ingredientUnit.add(unit);
                    ingredientQuantity.add(quantity);
                }
            }
        }

        notifyDataSetChanged(); // Notify the adapter that the data has changed
        Log.d(TAG, "reArrange ");
        Log.d(TAG, "recipe names: " + recipeNames.toString() + "unit: " + ingredientUnit.toString() + "quantities: " + ingredientQuantity.toString());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recipe, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipeNames.get(position), ingredientUnit.get(position), ingredientQuantity.get(position));
        Log.d(TAG, "onBindViewHolder" + " " + recipeNames.get(position).toString() + " " + ingredientUnit.get(position).toString() + " " + ingredientQuantity.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return recipeNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        TextView ingredientQuantity;
        TextView ingredientUnit;
        CheckBox ingredientCB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.sub_recipe_name);
            ingredientQuantity = itemView.findViewById(R.id.sub_ingredient_quantity);
            ingredientUnit = itemView.findViewById(R.id.sub_ingredient_unit);
            ingredientCB = itemView.findViewById(R.id.sub_ingredient_checkbox);

        }

        public void bind(String recipeName, String unit, Double quantity) {
            recipeTitle.setText(recipeName);
            ingredientUnit.setText(unit);
            ingredientQuantity.setText(String.format(String.valueOf(quantity)));
        }
    }


}
