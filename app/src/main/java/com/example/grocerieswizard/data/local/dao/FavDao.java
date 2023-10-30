package com.example.grocerieswizard.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.grocerieswizard.data.local.model.FavItem;

import java.util.List;

@Dao
public interface FavDao {
    @Upsert
    void insert(FavItem favItem);


    @Query("SELECT * FROM favorite")
    List<FavItem> getAllFavItems();

    @Query("DELETE FROM favorite")
    void deleteAllFavItems();

    @Query("DELETE FROM favorite WHERE recipeId = :recipeId")
    void deleteFavItem(int recipeId);

    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE recipeId = :recipeId)")
    Boolean isRecipeInFav(int recipeId);
}
