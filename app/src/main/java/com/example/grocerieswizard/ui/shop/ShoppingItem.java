package com.example.grocerieswizard.ui.shop;

import com.example.grocerieswizard.ui.shop.subshop.SubShoppingItem;

import java.util.ArrayList;

public class ShoppingItem {
    private final String ingredientName;

    public boolean isChecked() {
        return isChecked;
    }

    boolean isChecked;
    private final ArrayList<SubShoppingItem> subShoppingItems = new ArrayList<>();

    public ShoppingItem(String ingredientName) {
        this.ingredientName = ingredientName;
        isChecked = false;
    }

    public ArrayList<SubShoppingItem> getSubShoppingItems() {
        return subShoppingItems;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void addSubItem(SubShoppingItem subItem) {
        subShoppingItems.add(subItem);
    }

    public void setChecked(boolean b) {
        isChecked = b;
    }
}