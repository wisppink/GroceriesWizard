package com.example.grocerieswizard.Fav;

import com.example.grocerieswizard.home.RecipeModel;

public interface FavInterface {
    void onRemoveFromFavorites(RecipeModel recipeModel);

    boolean isRecipeSelected(int id);

    void insertSelectedRecipe(int id);

    void removeSelectedRecipe(int id);
}
