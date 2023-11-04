package com.example.grocerieswizard.ui.fav;

import com.example.grocerieswizard.ui.model.RecipeUi;

public interface FavInterface {
    void onRemoveFromFavorites(RecipeUi recipeUi);

    boolean isRecipeSelected(int id);

    void insertSelectedRecipe(RecipeUi recipeUi);

    void removeSelectedRecipe(RecipeUi recipeUi);

    void updateIt(int adapterPosition);
}