package com.example.grocerieswizard.interfaces;

import com.example.grocerieswizard.models.RecipeModel;

public interface FavInterface {
    void onRemoveFromFavorites(RecipeModel recipeModel);

    boolean isRecipeSelected(int id);

    void insertSelectedRecipe(int id);

    void removeSelectedRecipe(int id);
}
