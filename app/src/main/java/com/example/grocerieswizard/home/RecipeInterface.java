package com.example.grocerieswizard.home;

import com.example.grocerieswizard.models.RecipeModel;

public interface RecipeInterface {

    void onItemClick(int position);

    void onItemDelete(int position);

    void onItemEdit(int position);
    Boolean isRecipeSelected(int id);

    int updateRecipe(RecipeModel oldRecipe);

    void insertRecipe(RecipeModel recipe);

    void deleteRecipe(int id);

    void deleteSelectedRecipe(int recipeId);

    void insertSelectedRecipe(int recipeId);

    boolean isRecipeFavorite(int id);

    void insertRecipeFav(int recipeId);

    void deleteRecipeFav(int recipeId);

    boolean onLongClick(int adapterPosition);

    void onItemShare(int adapterPosition);
}

