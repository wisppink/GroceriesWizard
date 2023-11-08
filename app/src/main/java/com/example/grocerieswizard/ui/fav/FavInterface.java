package com.example.grocerieswizard.ui.fav;

import com.example.grocerieswizard.ui.model.RecipeUi;

public interface FavInterface {
    void toggleFavoriteRecipe(RecipeUi recipeUi);

    void toggleCartRecipe(RecipeUi recipeUi);
}