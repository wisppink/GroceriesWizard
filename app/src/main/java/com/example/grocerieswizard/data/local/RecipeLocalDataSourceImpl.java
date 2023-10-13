package com.example.grocerieswizard.data.local;

import android.util.Log;

import com.example.grocerieswizard.data.repo.model.Ingredient;
import com.example.grocerieswizard.data.repo.model.Recipe;

import java.util.List;

public class RecipeLocalDataSourceImpl implements RecipeLocalDataSource {
    private final RecipeDatabaseHelper dbHelper;
    private static final String TAG = "RecipeLocalDataSourceIm";

    public RecipeLocalDataSourceImpl(RecipeDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return dbHelper.getAllRecipesFromDB();
    }

    @Override
    public void deleteRecipeFav(int recipeId) {
        dbHelper.deleteRecipeFav(recipeId);
    }

    @Override
    public void insertRecipeFav(int recipeId) {
        dbHelper.insertRecipeFav(recipeId);
    }

    @Override
    public boolean isRecipeFavorite(int recipeId) {
        return dbHelper.isRecipeFavorite(recipeId);
    }

    @Override
    public void insertSelectedRecipe(int recipeId) {
        dbHelper.insertSelectedRecipe(recipeId);
    }

    @Override
    public void deleteSelectedRecipe(int recipeId) {
        dbHelper.deleteSelectedRecipe(recipeId);
    }

    @Override
    public void deleteRecipe(int recipeId) {
        Log.d(TAG, "deleteRecipe: before: " + dbHelper.getAllRecipesFromDB().size());
        dbHelper.deleteRecipe(recipeId);
        Log.d(TAG, "deleteRecipe: before: " + dbHelper.getAllRecipesFromDB().size());

    }

    @Override
    public void insertRecipe(Recipe recipe) {
        dbHelper.insertRecipe(recipe);
    }

    @Override
    public Boolean isRecipeSelected(int recipeId) {
        return dbHelper.isRecipeSelected(recipeId);
    }

    @Override
    public int updateRecipe(int oldRecipeId, Recipe recipe) {
        return dbHelper.updateRecipe(oldRecipeId, recipe);
    }

    @Override
    public List<Recipe> getFavoriteRecipes() {
        return dbHelper.getRecipesFav();
    }

    @Override
    public void deleteIngredient(int ingredientId) {
        dbHelper.deleteIngredient(ingredientId);
    }

    @Override
    public void insertIngredient(Ingredient ingredient) {
        dbHelper.insertIngredient(ingredient);
    }

    @Override
    public List<Recipe> getSelectedRecipes() {
        return dbHelper.getSelectedRecipes();
    }
}