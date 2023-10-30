package com.example.grocerieswizard.data.repo;

import androidx.annotation.NonNull;

import com.example.grocerieswizard.data.local.RecipeLocalDataSource;
import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.FavItem;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.remote.RecipeRemoteDataSource;
import com.example.grocerieswizard.data.remote.model.MealResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepositoryImpl implements RecipeRepository {

    private final RecipeLocalDataSource localDataSource;
    private final RecipeRemoteDataSource remoteDataSource;
    private final RepoMapper mapper;

    public RecipeRepositoryImpl(RecipeLocalDataSource localDataSource, RecipeRemoteDataSource remoteDataSource, RepoMapper mapper) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.mapper = mapper;
    }

    @Override
    public List<RecipeItem> getAllRecipes() {
        return localDataSource.getAllRecipes();
    }

    @Override
    public void deleteRecipe(int recipeId) {
        localDataSource.deleteRecipe(recipeId);

    }

    @Override
    public void insertRecipe(RecipeItem recipe) {
        localDataSource.insertRecipe(recipe);
    }

    @Override
    public int updateRecipe(int oldRecipeId, RecipeItem recipe) {
        return localDataSource.updateRecipe(oldRecipeId, recipe);
    }

    @Override
    public RecipeItem getRecipeByRecipeId(int recipeId) {
        return localDataSource.getRecipeByRecipeId(recipeId);
    }


    @Override
    public List<FavItem> getFavoriteRecipes() {
        return localDataSource.getFavoriteRecipes();
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
    public void deleteIngredient(IngredientItem ingredientItem) {
        localDataSource.deleteIngredient(ingredientItem);
    }

    @Override
    public void insertIngredient(IngredientItem ingredientItem) {
        localDataSource.insertIngredient(ingredientItem);
    }


    @Override
    public Boolean isRecipeInCart(int recipeId) {
        return localDataSource.isRecipeInCart(recipeId);
    }

    @Override
    public void insertCartItem(CartItem cartItem) {
        localDataSource.insertCartItem(cartItem);
    }

    @Override
    public void deleteCartItem(int recipeId) {
        localDataSource.deleteCartItem(recipeId);
    }

    @Override
    public List<CartItem> getCartItems() {
        return localDataSource.getCartItems();
    }


    @Override
    public void searchMeals(String query, RepositoryCallback<List<RecipeItem>> callback) {
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