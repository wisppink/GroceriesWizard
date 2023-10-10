package com.example.grocerieswizard.data.repo;

import com.example.grocerieswizard.addRecipe.IngredientModel;
import com.example.grocerieswizard.data.repo.model.Recipe;

import java.util.List;

public interface RecipeRepository {
    List<Recipe> getAllRecipes();

    void deleteRecipeFromFavorites(int recipeId);

    void insertRecipeFav(int recipeId);

    boolean isRecipeFavorite(int recipeId);

    void insertSelectedRecipe(int recipeId);

    void deleteSelectedRecipe(int recipeId);

    void deleteRecipe(int recipeId);

    void insertRecipe(Recipe recipe);

    Boolean isRecipeSelected(int recipeId);

    int updateRecipe(int oldRecipeId, Recipe recipe);

    List<Recipe> getFavoriteRecipes();

    void deleteIngredient(int ingredientId);

    void insertIngredient(IngredientModel ingredientModel, long recipeId);

    List<Recipe> getSelectedRecipes();

    void searchMeals(String query, RepositoryCallback<List<Recipe>> callback);
}