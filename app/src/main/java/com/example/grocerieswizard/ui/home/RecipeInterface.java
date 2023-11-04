package com.example.grocerieswizard.ui.home;

import com.example.grocerieswizard.ui.model.RecipeUi;

public interface RecipeInterface {

    void onItemClick(RecipeUi recipeUi);

    void onItemDelete(RecipeUi recipeUi);

    void onItemEdit(RecipeUi recipeUi);
    int updateRecipe(RecipeUi oldRecipeUi);

    void onItemShare(RecipeUi recipe);

    void toggleFavoriteRecipe(RecipeUi recipeUi);

    void toggleCartRecipe(RecipeUi recipeUi);
}
