package com.example.grocerieswizard.ui;

import android.util.Log;

import com.example.grocerieswizard.data.repo.model.Ingredient;
import com.example.grocerieswizard.data.repo.model.Recipe;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UiMapper {
    private static final String TAG = "UiMapper";

    public RecipeUi toRecipeUi(Recipe recipe) {
        RecipeUi recipeUi = new RecipeUi(recipe.getRecipeName(), toIngredientUi(recipe.getIngredients()), recipe.getInstructions(), recipe.getImageBitmap());
        Log.d(TAG, "toRecipeUi: recipe image bitmap: " + recipe.getImageBitmap());
        recipeUi.setId((int) recipe.getId());
        return recipeUi;
    }

    public Recipe toRecipe(RecipeUi recipeUi) {
        return new Recipe(recipeUi.getRecipeName(), toIngredients(recipeUi.getIngredients()), recipeUi.getInstructions(), recipeUi.getImageBitmap());
    }

    private List<Ingredient> toIngredients(List<IngredientUi> ingredients) {
        return ingredients.stream()
                .map(this::toIngredient)
                .collect(Collectors.toList());
    }

    public List<IngredientUi> toIngredientUi(List<Ingredient> ingredients) {
        List<IngredientUi> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            IngredientUi ingredientUi = new IngredientUi(ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit());
            ingredientList.add(ingredientUi);
        }
        return ingredientList;
    }

    public Ingredient toIngredient(IngredientUi ingredientUi) {
        return new Ingredient(ingredientUi.getName(), ingredientUi.getQuantity(), ingredientUi.getUnit(), ingredientUi.getRecipeId());
    }
}