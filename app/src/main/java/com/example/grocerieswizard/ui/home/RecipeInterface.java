package com.example.grocerieswizard.ui.home;

import com.example.grocerieswizard.ui.model.RecipeUi;

public interface RecipeInterface {

    void onItemClick(int position);

    void onItemDelete(int position);

    void onItemEdit(int position);
    Boolean isRecipeSelected(int id);

    int updateRecipe(RecipeUi oldRecipeUi);

    void insertRecipe(RecipeUi recipeUi);

    void deleteRecipe(int id);

    void deleteSelectedRecipe(int recipeId);

    void insertSelectedRecipe(int recipeId);

    boolean isRecipeFavorite(int id);

    void insertRecipeFav(int recipeId);

    void deleteRecipeFav(int recipeId);

    void onItemShare(int adapterPosition);
}
