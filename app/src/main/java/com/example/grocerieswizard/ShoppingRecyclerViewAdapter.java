package com.example.grocerieswizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ShoppingRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingRecyclerViewAdapter.ShoppingViewHolder> {
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private ArrayList<RecipeModel> selectedRecipeList = new ArrayList<>();
    private Map<String, Map<String, Map<String, Double>>> ingredientInfo = new HashMap<>();
    private ArrayList<String> ingredientNameKey = new ArrayList<>();

    String TAG = "Shopping Adapter";

    public ShoppingRecyclerViewAdapter(Context context) {
        this.context = context;
        recipeDatabaseHelper = new RecipeDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item_row, parent, false);
        return new ShoppingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        Entry<String, Map<String, Map<String, Double>>> entry = getEntryAtPosition(position);
        String ingredientName = entry.getKey();
        Map<String, Map<String, Double>> ingredientMap = entry.getValue();
        holder.bind(ingredientName, ingredientMap, position);
        boolean allSubItemsChecked = true;
        for (int i = 0; i < ingredientMap.size(); i++) {
            if (!holder.getSubRecipeAdapter().isItemChecked(i)) {
                allSubItemsChecked = false;
                break;
            }
        }
        holder.checkBox.setChecked(allSubItemsChecked);
    }

    @Override
    public int getItemCount() {
        return ingredientInfo.size();
    }

    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setSelectedRecipeList(ArrayList<RecipeModel> selectedRecipeList) {
        this.selectedRecipeList = selectedRecipeList;
        ingredientInfo = rearrange(selectedRecipeList);
        notifyDataSetChanged();
    }

    private Entry<String, Map<String, Map<String, Double>>> getEntryAtPosition(int position) {
        List<Entry<String, Map<String, Map<String, Double>>>> entries = new ArrayList<>(ingredientInfo.entrySet());
        return entries.get(position);
    }

    public Map<String, Map<String, Map<String, Double>>> rearrange(ArrayList<RecipeModel> recipes) {
        // Clear the existing data before rearranging.
        ingredientInfo.clear();
        ingredientNameKey.clear();

        // Iterate through each recipe in the input ArrayList
        for (RecipeModel recipe : recipes) {
            String recipeName = recipe.getRecipeName();

            // Iterate through each ingredient in the current recipe
            for (IngredientModel ingredient : recipe.getIngredients()) {
                String ingredientName = ingredient.getName();
                double quantity = ingredient.getQuantity();
                String unit = ingredient.getUnit();

                // If the ingredientName is not present in the ingredientInfo map, add it
                if (!ingredientInfo.containsKey(ingredientName)) {
                    ingredientInfo.put(ingredientName, new HashMap<>());
                    ingredientNameKey.add(ingredientName);
                }

                // If the recipeName is not present for this ingredient, add it
                if (!ingredientInfo.get(ingredientName).containsKey(recipeName)) {
                    ingredientInfo.get(ingredientName).put(recipeName, new HashMap<>());
                }

                // Add the unit and quantity information to the ingredientInfo map
                ingredientInfo.get(ingredientName).get(recipeName).put(unit, quantity);
            }
        }

        return ingredientInfo;
    }

    public class ShoppingViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView ingredient_name;
        private RecyclerView subRecipe;
        private TextView total;
        private SubRecipeRecyclerViewAdapter subRecipeAdapter;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.shopping_cart_ingredient_name);
            subRecipe = itemView.findViewById(R.id.sub_recipe_recycler);
            total = itemView.findViewById(R.id.total);
            checkBox = itemView.findViewById(R.id.is_finished);
            checkBox.setClickable(false);
        }

        public void bind(String ingredientName, Map<String, Map<String, Double>> ingredientMap, int position) {
            ingredient_name.setText(ingredientName);
            subRecipeAdapter = new SubRecipeRecyclerViewAdapter(context);
            subRecipe.setAdapter(subRecipeAdapter);
            subRecipe.setLayoutManager(new LinearLayoutManager(context));
            subRecipeAdapter.setIngredientInfo(ingredientMap);

            subRecipeAdapter.setOnSubItemCheckListener(isAllChecked -> {
                checkBox.setChecked(isAllChecked);
                updateTotal();
            });

            updateTotal();
        }

        public SubRecipeRecyclerViewAdapter getSubRecipeAdapter() {
            return subRecipeAdapter;
        }
    }

    private void updateTotal() {

    }


}

