package com.example.grocerieswizard.data.repo.model;

import android.graphics.Bitmap;

import java.util.List;

public class Recipe {
    private String imageUrl;
    private long id;
    private String recipeName;
    private Bitmap imageBitmap;
    private String instructions;
    private List<Ingredient> ingredients;

    public Recipe(long id, String recipeName, Bitmap imageBitmap, String instructions, List<Ingredient> ingredients) {
        this.id = id;
        this.recipeName = recipeName;
        this.imageBitmap = imageBitmap;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public Recipe(String recipeName, List<Ingredient> ingredients, String instructions, Bitmap imageBitmap) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageBitmap = imageBitmap;
    }

    public Recipe(String recipeName, List<Ingredient> ingredients, String instructions, String imageUrl) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}