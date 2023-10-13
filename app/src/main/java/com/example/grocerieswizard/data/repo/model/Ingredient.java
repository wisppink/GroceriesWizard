package com.example.grocerieswizard.data.repo.model;

public class Ingredient {
    private String name;
    private double quantity;
    private String unit;
    private int id;
    int recipeID;

    public Ingredient(String ingredientName, double ingredientQuantity, String ingredientUnit, long recipeId) {

        name = ingredientName;
        quantity = ingredientQuantity;
        unit = ingredientUnit;
        recipeID = (int) recipeId;
    }

    public Ingredient(String ingredientName, double ingredientQuantity, String ingredientUnit) {
        name = ingredientName;
        quantity = ingredientQuantity;
        unit = ingredientUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }
}
