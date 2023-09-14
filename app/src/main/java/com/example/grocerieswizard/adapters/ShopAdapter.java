package com.example.grocerieswizard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;
import java.util.Objects;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShoppingViewHolder> {
    private final Context context;
    private ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();

    public ShopAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item_row, parent, false);
        return new ShoppingViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItems.get(position);
        holder.bind(shoppingItem);
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void setSelectedRecipeList(ArrayList<RecipeModel> selectedRecipeList) {
        shoppingItems = generateShoppingItems(selectedRecipeList);
    }

    public ArrayList<ShoppingItem> generateShoppingItems(ArrayList<RecipeModel> recipes) {
        for (RecipeModel recipe : recipes) {
            for (IngredientModel ingredient : recipe.getIngredients()) {
                String ingredientName = ingredient.getName();
                String recipeName = recipe.getRecipeName();
                String unit = ingredient.getUnit();
                double quantity = ingredient.getQuantity();

                // Check if the shopping item is already in the list
                boolean itemExists = false;
                for (ShoppingItem item : shoppingItems) {
                    if (Objects.equals(item.getIngredientName(), ingredientName)) {
                        // If it's already in the list, create a sub-shopping item
                        // and add it to the existing shopping item
                        SubShoppingItem subItem = new SubShoppingItem(recipeName, unit, quantity);
                        item.addSubItem(subItem, false);
                        itemExists = true;
                        break;
                    }
                }

                if (!itemExists) {
                    // If it's not in the list, create a new shopping item
                    ShoppingItem newItem = new ShoppingItem(ingredientName);
                    SubShoppingItem subItem = new SubShoppingItem(recipeName, unit, quantity);
                    newItem.addSubItem(subItem, false);
                    shoppingItems.add(newItem);
                }
            }
        }

        return shoppingItems;
    }


    public class ShoppingViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final TextView ingredient_name;
        private final RecyclerView subRecipe;
        private final TextView totalTV;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.shopping_cart_ingredient_name);
            subRecipe = itemView.findViewById(R.id.sub_recipe_recycler);
            totalTV = itemView.findViewById(R.id.total);
            checkBox = itemView.findViewById(R.id.is_finished);
        }

        public void bind(ShoppingItem shoppingItem) {
            ingredient_name.setText(shoppingItem.getIngredientName());

            SubShopAdapter subRecipeAdapter = new SubShopAdapter();
            subRecipe.setAdapter(subRecipeAdapter);
            subRecipeAdapter.setShoppingItems(shoppingItem);
            subRecipe.setLayoutManager(new LinearLayoutManager(context));
            subRecipeAdapter.setOnSubItemCheckListener(checkBox::setChecked);
            totalTV.setText(R.string.not_available);
        }

    }

}
