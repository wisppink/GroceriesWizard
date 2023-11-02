package com.example.grocerieswizard.ui.home;

import com.example.grocerieswizard.ui.model.RecipeUi;

public interface RecipeInterface {

    void onItemClick(RecipeUi recipeUi);

    void onItemDelete(RecipeUi recipeUi);

    void onItemEdit(RecipeUi recipeUi);
    Boolean isRecipeSelected(int id);

    int updateRecipe(RecipeUi oldRecipeUi);

    void insertRecipe(RecipeUi recipeUi);

    void deleteSelectedRecipe(int recipeId);

    void insertSelectedRecipe(int recipeId);

    boolean isRecipeFavorite(int id);

    void insertRecipeFav(int recipeId);

    void deleteRecipeFav(int recipeId);

    void onItemShare(RecipeUi recipe);
}
