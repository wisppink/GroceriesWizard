package com.example.grocerieswizard.data.repo;

import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.FavItem;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public interface RecipeRepository {
    List<RecipeItem> getAllRecipes();

    void deleteRecipe(RecipeUi recipeUi);

    void insertRecipe(RecipeItem recipeItem);

    int updateRecipe(int oldRecipeId, RecipeItem recipe);

    RecipeItem getRecipeByRecipeId(int recipeId);


    void deleteRecipeFromFavorites(int recipeId);

    void insertRecipeFav(int recipeId);

    List<FavItem> getFavoriteRecipes();

    boolean isRecipeFavorite(int recipeId);


    List<CartItem> getCartItems();

    void insertCartItem(CartItem cartItem);

    void deleteCartItem(int recipeId);

    Boolean isRecipeInCart(int recipeId);


    void deleteIngredient(IngredientItem ingredientItem);

    void insertIngredient(IngredientItem ingredientItem);


    void searchMeals(String query, RepositoryCallback<List<RecipeItem>> callback);
}