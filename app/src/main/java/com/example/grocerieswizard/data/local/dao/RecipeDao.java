package com.example.grocerieswizard.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(RecipeItem recipeItem);

    @Query("UPDATE recipe SET name = :newName, instructors = :newInstructors, ingredientList = :newIngredientList WHERE id = :oldRecipeId")
    int updateRecipe(long oldRecipeId, String newName, String newInstructors, List<IngredientItem> newIngredientList);


    @Query("DELETE FROM recipe WHERE id = :recipeId")
    void delete(int recipeId);

    @Query("SELECT * FROM recipe")
    List<RecipeItem> getAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    RecipeItem getRecipeById(long recipeId);
}
