package com.example.grocerieswizard.data.local;

import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.FavItem;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public interface RecipeLocalDataSource {
    List<RecipeItem> getAllRecipes();

    void deleteRecipe(RecipeUi recipe);

    void insertRecipe(RecipeItem recipe);

    int updateRecipe(int oldRecipeId, RecipeItem recipe);

    RecipeItem getRecipeByRecipeId(int recipeId);


    void insertCartItem(CartItem cartItem);

    List<CartItem> getCartItems();

    void deleteCartItem(RecipeUi recipeUi);

    Boolean isRecipeInCart(int recipeId);


    List<FavItem> getFavoriteRecipes();

    void deleteRecipeFav(int recipeId);

    void insertRecipeFav(RecipeUi recipeUi);

    void deleteIngredient(IngredientItem ingredient);

    void insertIngredient(IngredientItem ingredient);

}