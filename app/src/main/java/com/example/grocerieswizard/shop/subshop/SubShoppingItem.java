package com.example.grocerieswizard.shop.subshop;

import com.example.grocerieswizard.shop.ShopAdapter;

public class SubShoppingItem {
    private final String recipeName;
    private final String ingredientUnit;
    private final Double ingredientQuantity;
    private Boolean isChecked;

    public SubShoppingItem(String recipeName, String ingredientUnit, Double ingredientQuantity) {
        this.recipeName = recipeName;
        this.ingredientUnit = ingredientUnit;
        this.ingredientQuantity = ingredientQuantity;
        isChecked = false;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getIngredientUnit() {
        return ingredientUnit;
    }

    public Double getIngredientQuantity() {
        return ingredientQuantity;
    }
    public Boolean getChecked() {
        return isChecked;
    }
    public void setChecked(Boolean checked){
        isChecked = checked;
    }
}


