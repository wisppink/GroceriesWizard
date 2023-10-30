package com.example.grocerieswizard.ui;

import com.example.grocerieswizard.data.local.model.IngredientDesc;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UiMapper {
    public RecipeUi toRecipeUi(RecipeItem recipe) {
        RecipeUi recipeUi = new RecipeUi(recipe.getName(), toIngredientUi(recipe.getIngredientList()), recipe.getInstructors());
        //, recipe.getImageBitmap()
        //Log.d(TAG, "toRecipeUi: recipe image bitmap: " + recipe.getImageBitmap());
        recipeUi.setId((int) recipe.getId());
        return recipeUi;
    }


    public RecipeItem toRecipe(RecipeUi recipeUi) {
        return new RecipeItem(recipeUi.getRecipeName(), recipeUi.getInstructions(), toIngredients(recipeUi.getIngredients()));
        //,recipeUi.getImageBitmap())
    }

    private List<IngredientItem> toIngredients(List<IngredientUi> ingredients) {
        return ingredients.stream()
                .map(this::toIngredient)
                .collect(Collectors.toList());
    }

    public List<IngredientUi> toIngredientUi(List<IngredientItem> ingredients) {
        List<IngredientUi> ingredientList = new ArrayList<>();
        for (IngredientItem ingredient : ingredients) {
            IngredientUi ingredientUi = new IngredientUi(ingredient.getIngredientDesc().getName(), ingredient.getQuantity(), ingredient.getUnit());
            ingredientList.add(ingredientUi);
        }
        return ingredientList;
    }

    public IngredientItem toIngredient(IngredientUi ingredientUi) {
        IngredientDesc ingredientDesc = new IngredientDesc(ingredientUi.getName());
        return new IngredientItem(ingredientDesc, ingredientUi.getQuantity(), ingredientUi.getUnit());
    }
}