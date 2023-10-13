package com.example.grocerieswizard.data.repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.grocerieswizard.data.local.RecipeLocalDataSource;
import com.example.grocerieswizard.data.remote.RecipeRemoteDataSource;
import com.example.grocerieswizard.data.remote.model.MealResponse;
import com.example.grocerieswizard.data.repo.model.Ingredient;
import com.example.grocerieswizard.data.repo.model.Recipe;
import com.example.grocerieswizard.data.repo.model.RepoMapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepositoryImpl implements RecipeRepository {

    private final RecipeLocalDataSource localDataSource;
    private final RecipeRemoteDataSource remoteDataSource;
    private final RepoMapper mapper;
    private static final String TAG = "RecipeRepositoryImpl";

    public RecipeRepositoryImpl(RecipeLocalDataSource localDataSource, RecipeRemoteDataSource remoteDataSource, RepoMapper mapper) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.mapper = mapper;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return localDataSource.getAllRecipes();
    }

    @Override
    public void deleteRecipeFromFavorites(int recipeId) {
        localDataSource.deleteRecipeFav(recipeId);
    }

    @Override
    public void insertRecipeFav(int recipeId) {
        localDataSource.insertRecipeFav(recipeId);
    }

    @Override
    public boolean isRecipeFavorite(int recipeId) {
        return localDataSource.isRecipeFavorite(recipeId);
    }

    @Override
    public void insertSelectedRecipe(int recipeId) {
        localDataSource.insertSelectedRecipe(recipeId);
    }

    @Override
    public void deleteSelectedRecipe(int recipeId) {
        localDataSource.deleteSelectedRecipe(recipeId);
    }

    @Override
    public void deleteRecipe(int recipeId) {
        Log.d(TAG, "deleteRecipe: before: " + localDataSource.getAllRecipes().size());
        localDataSource.deleteRecipe(recipeId);
        Log.d(TAG, "deleteRecipe: before: " + localDataSource.getAllRecipes().size());

    }

    @Override
    public void insertRecipe(Recipe recipe) {
        localDataSource.insertRecipe(recipe);
    }

    @Override
    public Boolean isRecipeSelected(int recipeId) {
        return localDataSource.isRecipeSelected(recipeId);
    }

    @Override
    public int updateRecipe(int oldRecipeId, Recipe recipe) {
        return localDataSource.updateRecipe(oldRecipeId, recipe);
    }

    @Override
    public List<Recipe> getFavoriteRecipes() {
        return localDataSource.getFavoriteRecipes();
    }

    @Override
    public void deleteIngredient(int ingredientId) {
        localDataSource.deleteIngredient(ingredientId);
    }

    @Override
    public void insertIngredient(Ingredient ingredient) {
        localDataSource.insertIngredient(ingredient);
    }

    @Override
    public List<Recipe> getSelectedRecipes() {
        return localDataSource.getSelectedRecipes();
    }

    @Override
    public void searchMeals(String query, RepositoryCallback<List<Recipe>> callback) {
        remoteDataSource.searchMeals(query).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(mapper.toRecipes(response.body()));
                } else {
                    callback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }
}