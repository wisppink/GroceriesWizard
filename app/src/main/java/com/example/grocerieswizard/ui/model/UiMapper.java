package com.example.grocerieswizard.ui.model;

import android.util.Log;

import com.example.grocerieswizard.data.repo.model.Recipe;

public class UiMapper {
    private static final String TAG = "UiMapper";
    public RecipeUi toRecipeUi(Recipe recipe) {
        RecipeUi recipeUi = new RecipeUi(recipe.getRecipeName(), recipe.getIngredients(), recipe.getInstructions(), recipe.getImageBitmap());
        Log.d(TAG, "toRecipeUi: recipe image bitmap: " + recipe.getImageBitmap());
        recipeUi.setId((int) recipe.getId());
        return  recipeUi;
    }

    public Recipe toRecipe(RecipeUi recipeUi) {
        return new Recipe(recipeUi.getRecipeName(), recipeUi.getIngredients(), recipeUi.getInstructions(), recipeUi.getImageBitmap());
    }
}