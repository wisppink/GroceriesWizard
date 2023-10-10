package com.example.grocerieswizard.shop;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.grocerieswizard.addRecipe.IngredientModel;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.example.grocerieswizard.shop.subshop.SubShoppingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShopHelperImpl implements ShopHelper {

    @Override
    public ArrayList<ShoppingItem> generateShoppingItems(List<RecipeUi> recipeUis) {
        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
        for (RecipeUi recipeUi : recipeUis) {
            for (IngredientModel ingredient : recipeUi.getIngredients()) {
                String ingredientName = ingredient.getName();
                String recipeName = recipeUi.getRecipeName();
                String unit = ingredient.getUnit();
                double quantity = ingredient.getQuantity();

                // Check if the shopping item is already in the list
                boolean itemExists = false;
                for (ShoppingItem item : shoppingItems) {
                    if (Objects.equals(item.getIngredientName(), ingredientName)) {
                        // If it's already in the list, create a sub-shopping item
                        // and add it to the existing shopping item
                        SubShoppingItem subItem = new SubShoppingItem(recipeName, unit, quantity);
                        item.addSubItem(subItem);
                        itemExists = true;
                        break;
                    }
                }

                if (!itemExists) {
                    // If it's not in the list, create a new shopping item
                    ShoppingItem newItem = new ShoppingItem(ingredientName);
                    SubShoppingItem subItem = new SubShoppingItem(recipeName, unit, quantity);
                    newItem.addSubItem(subItem);
                    shoppingItems.add(newItem);
                }
            }
        }

        return shoppingItems;
    }

    @Override
    public String generateTotal(ArrayList<SubShoppingItem> subShoppingItems) {
        Map<String, Double> unitToQuantity = new HashMap<>();

        subShoppingItems.forEach((item) -> {
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