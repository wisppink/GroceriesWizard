package com.example.grocerieswizard.models;

public class SubShoppingItem {
    private final String recipeName;
    private final String ingredientUnit;
    private final Double ingredientQuantity;

    public SubShoppingItem(String recipeName, String ingredientUnit, Double ingredientQuantity) {
        this.recipeName = recipeName;
        this.ingredientUnit = ingredientUnit;
        this.ingredientQuantity = ingredientQuantity;
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
}


