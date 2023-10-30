package com.example.grocerieswizard.data.local;

import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.FavItem;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;

import java.util.List;

public interface RecipeLocalDataSource {
    List<RecipeItem> getAllRecipes();

    void deleteRecipe(int recipeId);

    void insertRecipe(RecipeItem recipe);

    int updateRecipe(int oldRecipeId, RecipeItem recipe);

    RecipeItem getRecipeByRecipeId(int recipeId);


    void insertCartItem(CartItem cartItem);

    List<CartItem> getCartItems();

    void deleteCartItem(int recipeId);

    Boolean isRecipeInCart(int recipeId);


    List<FavItem> getFavoriteRecipes();

    void deleteRecipeFav(int recipeId);

    void insertRecipeFav(int recipeId);

    boolean isRecipeFavorite(int recipeId);


    void deleteIngredient(IngredientItem ingredient);

    void insertIngredient(IngredientItem ingredient);

}