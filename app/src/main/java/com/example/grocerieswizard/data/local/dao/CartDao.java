package com.example.grocerieswizard.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;

import java.util.List;

@Dao
public interface CartDao {
    @Upsert
    void insert(CartItem cartItem);

    @Query("SELECT * FROM cart")
    List<CartItem> getAllCartItems();

    @Query("DELETE FROM cart")
    void deleteAllCartItems();

    @Query("DELETE FROM cart WHERE recipeId = :recipeId")
    void deleteCartItem(int recipeId);

    @Query("SELECT EXISTS (SELECT 1 FROM cart WHERE recipeId = :recipeId)")
    Boolean isRecipeInCart(int recipeId);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    RecipeItem getRecipeById(long recipeId);
}

