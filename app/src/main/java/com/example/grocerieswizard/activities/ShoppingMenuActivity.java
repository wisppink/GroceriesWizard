package com.example.grocerieswizard.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.ShopAdapter;
import com.example.grocerieswizard.databinding.ActivityShoppingMenuBinding;
import com.example.grocerieswizard.interfaces.ShopInterface;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShoppingMenuActivity extends AppCompatActivity implements ShopInterface {

    private static final String TAG = "ShoppingMenuActivity";
    ActivityShoppingMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ArrayList<RecipeModel> recipes;
        ShopAdapter adapter;
        try (RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this)) {

            adapter = new ShopAdapter(this);
            binding.shoppingCart.setAdapter(adapter);
            binding.shoppingCart.setLayoutManager(new LinearLayoutManager(this));

            adapter.setShopInterface(this);
            recipes = dbHelper.getSelectedRecipes();
        }
        adapter.setSelectedRecipeList(recipes);
    }

    @Override
    public ArrayList<ShoppingItem> generateShoppingItems(ArrayList<RecipeModel> recipes) {
        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
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

    @Override
    public String generateTotal(Map<SubShoppingItem, Boolean> subShoppingItems) {

        Map<String, Double> unitToQuantity = new HashMap<>();

        subShoppingItems.forEach((item, isChecked) -> {
            if (!isChecked) {
                if (!unitToQuantity.containsKey(item.getIngredientUnit())) {
                    unitToQuantity.put(item.getIngredientUnit(), item.getIngredientQuantity());
                    Log.d(TAG, "it doesn't contain and it is false added: " + item.getRecipeName() + " " + item.getIngredientUnit() + " " + item.getIngredientQuantity());
                } else {
                    Log.d(TAG, "it contains and it is false added: " + item.getRecipeName() + " " + item.getIngredientUnit());
                    Double oldValue = unitToQuantity.get(item.getIngredientUnit());
                    Log.d(TAG, "generateTotal: old value " + oldValue);
                    Double quantity = item.getIngredientQuantity();
                    Log.d(TAG, "generateTotal: quantity " + quantity);
                    assert oldValue != null;
                    Double newValue = oldValue + quantity;
                    Log.d(TAG, "generateTotal: new value " + newValue);
                    unitToQuantity.put(item.getIngredientUnit(), newValue);
                }
            }
        });

        StringBuilder resultBuilder = new StringBuilder();

        unitToQuantity.forEach((unit, quantity) -> resultBuilder.append(quantity).append(" ").append(unit).append(", "));

        // Remove the trailing ", " from the result string.
        String result = resultBuilder.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }
        Log.d(TAG, "generateTotal: result: " + result);
        return result;
    }


}