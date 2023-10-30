package com.example.grocerieswizard.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerieswizard.data.local.model.IngredientItem;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insert(IngredientItem ingredientItem);

    @Update
    void update(IngredientItem ingredientItem);

    @Delete
    void delete(IngredientItem ingredientItem);

    @Query("SELECT * FROM ingredient")
    List<IngredientItem> getAllIngredients();

    @Query("SELECT * FROM ingredient WHERE ingredientDesc = :ingredientDescId")
    List<IngredientItem> getIngredientsByDescId(long ingredientDescId);
}