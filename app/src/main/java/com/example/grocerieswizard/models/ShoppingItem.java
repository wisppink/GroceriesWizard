package com.example.grocerieswizard.models;

import java.util.HashMap;
import java.util.Map;

public class ShoppingItem {
    private final String ingredientName;

    private final Map<SubShoppingItem, Boolean> subShoppingItems = new HashMap<>();

    public ShoppingItem(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Map<SubShoppingItem, Boolean> getSubShoppingItems() {
        return subShoppingItems;
    }


    public String getIngredientName() {
        return ingredientName;
    }

    public void addSubItem(SubShoppingItem subItem, boolean isChecked) {
        subShoppingItems.put(subItem, isChecked);

    }

    public void updateSubItemStatus(SubShoppingItem subItem, boolean isChecked) {
        subShoppingItems.put(subItem, isChecked);
    }


}
