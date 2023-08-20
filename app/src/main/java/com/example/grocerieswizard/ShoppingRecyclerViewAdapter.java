package com.example.grocerieswizard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingRecyclerViewAdapter.ShoppingViewHolder> {
    private Context context;
    private List<Map.Entry<String, List<String>>> ingredientEntries; // List of map entries


    public ShoppingRecyclerViewAdapter(Context context, Map<String, List<String>> recipeIngredientsMap) {
        this.context = context;
        this.ingredientEntries = new ArrayList<>(recipeIngredientsMap.entrySet());
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item_row, parent, false);
        return new ShoppingViewHolder(itemView);
    }

    // Bind the data to the ViewHolder and handle sub-recipe checkbox logic
    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        Map.Entry<String, List<String>> entry = ingredientEntries.get(position);
        String ingredientName = entry.getKey();
        List<String> values = entry.getValue();
        Log.d("ShoppingMenu", "Binding data: " + entry.getKey() + " - " + entry.getValue());

        holder.bind(ingredientName, values);
        boolean allSubItemsChecked = true;
        for (int i = 0; i < values.size(); i++) {
            if (!holder.getSubRecipeAdapter().isItemChecked(i)) {
                allSubItemsChecked = false;
                break;
            }
        }
        holder.checkBox.setChecked(allSubItemsChecked);
    }

    @Override
    public int getItemCount() {
        Log.d("ShoppingMenu", "getItemCount: " + ingredientEntries.size());
        return ingredientEntries.size();
    }


    public class ShoppingViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredient_name;
        private RecyclerView subRecipe;
        private TextView total;
        private CheckBox checkBox;
        private int totalWeight = 0;
        private int totalQuantity = 0;
        private SubRecipeRecyclerViewAdapter subRecipeAdapter;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.shopping_cart_ingredient_name);
            subRecipe = itemView.findViewById(R.id.sub_recipe_recycler);
            total = itemView.findViewById(R.id.total);
            checkBox = itemView.findViewById(R.id.is_finished);
        }

        // Bind the data to the ViewHolder and handle sub-recipe checkbox logic
        public void bind(String ingredientName, List<String> values) {
            ingredient_name.setText(ingredientName);
            subRecipeAdapter = new SubRecipeRecyclerViewAdapter(itemView.getContext(), values);
            subRecipe.setAdapter(subRecipeAdapter);
            subRecipe.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

            subRecipeAdapter.setOnSubItemCheckListener(isAllChecked -> {
                checkBox.setChecked(isAllChecked);
                updateTotal();
            });

            updateTotal();
        }

        // Update total weight and quantity based on checked sub-recipe items
        private void updateTotal() {
            Map<String, Double> unitQuantities = new HashMap<>(); // Use Double instead of Integer

            for (int i = 0; i < subRecipeAdapter.getItemCount(); i++) {
                if (!subRecipeAdapter.isItemChecked(i)) {
                    String value = subRecipeAdapter.getValueAtPosition(i);
                    String[] parts = value.split(" ");
                    double quantity = Double.parseDouble(parts[1]); // Parse as double
                    String unit = parts[2];

                    if (unitQuantities.containsKey(unit)) {
                        double currentQuantity = unitQuantities.get(unit);
                        unitQuantities.put(unit, currentQuantity + quantity);
                    } else {
                        unitQuantities.put(unit, quantity);
                    }
                }
            }

            String totalString = "Total: ";
            List<String> unitStrings = new ArrayList<>();

            for (Map.Entry<String, Double> entry : unitQuantities.entrySet()) {
                String unit = entry.getKey();
                double quantity = entry.getValue();

                if (quantity > 0.0) {
                    unitStrings.add(unit + " " + quantity);
                }
            }


            // If you want to subtract checked items' quantities from the total
            for (int i = 0; i < subRecipeAdapter.getItemCount(); i++) {
                if (subRecipeAdapter.isItemChecked(i)) {
                    String value = subRecipeAdapter.getValueAtPosition(i);
                    String[] parts = value.split(" ");
                    double quantity = Double.parseDouble(parts[1]); // Parse as double
                    String unit = parts[2];

                    if (unitQuantities.containsKey(unit)) {
                        double currentQuantity = unitQuantities.get(unit);
                        unitQuantities.put(unit, currentQuantity - quantity);

                        if (currentQuantity - quantity <= 0.0) {
                            unitQuantities.remove(unit);
                        }
                    }
                }
            }

            // Update the totalString again if needed
            unitStrings.clear();

            for (Map.Entry<String, Double> entry : unitQuantities.entrySet()) {
                String unit = entry.getKey();
                double quantity = entry.getValue();

                if (quantity > 0.0) {
                    unitStrings.add(unit + " " + quantity);
                }
            }

            totalString = "Total: " + String.join(", ", unitStrings);
            total.setText(totalString);
        }



        public SubRecipeRecyclerViewAdapter getSubRecipeAdapter() {
            return subRecipeAdapter;
        }
    }

}
