package com.example.grocerieswizard.ui.model;

import com.example.grocerieswizard.data.repo.model.Recipe;

public class UiMapper {
    public RecipeUi toRecipeUi(Recipe recipe) {
        return new RecipeUi(recipe.getRecipeName(), recipe.getIngredients(), recipe.getInstructions(), recipe.getImageBitmap());
    }

    public Recipe toRecipe(RecipeUi recipeUi) {
        //TODO: recipe_id set is a problem
        Recipe recipe = new Recipe(null,null,null,null);
        recipe.setId(recipeUi.getId());
        recipe.setRecipeName(recipeUi.getRecipeName());
        recipe.setIngredients(recipeUi.getIngredients());
        recipe.setInstructions(recipeUi.getInstructions());
        recipe.setImageBitmap(recipeUi.getImageBitmap());
        return recipe;
    }
}