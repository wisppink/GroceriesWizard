package com.example.grocerieswizard.data.repo;

import android.util.Log;

import com.example.grocerieswizard.data.local.model.IngredientDesc;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.remote.model.Meal;
import com.example.grocerieswizard.data.remote.model.MealResponse;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RepoMapper {
    private static final String TAG = "RepoMapper";

    public List<RecipeItem> toRecipes(MealResponse mealResponse) {
        if (mealResponse.getMeals() == null) return new ArrayList<>();
        return Arrays.stream(mealResponse.getMeals())
                .map(this::toRecipe)
                .collect(Collectors.toList());
    }

    RecipeItem toRecipe(Meal meal) {
        return new RecipeItem(meal.getStrMeal(), meal.getStrInstructions(), meal.getIngredients());
        //, meal.getStrMealThumb())
    }

    public RecipeItem uiToRecipe(RecipeUi recipeUi) {
        Log.d(TAG, "uiToRecipe: ui: " + recipeUi.isFav());
        RecipeItem recipeItem = new RecipeItem(recipeUi.getRecipeName(), recipeUi.getInstructions(), toIngredient(recipeUi.getIngredients()));
        recipeItem.setFav(recipeUi.isFav());
        recipeItem.setCart(recipeUi.isCart());
        return recipeItem;
    }

    private List<IngredientItem> toIngredient(List<IngredientUi> uiIngredients) {
        List<IngredientItem> ingredients = new ArrayList<>();
        for (IngredientUi ingredient : uiIngredients) {
            IngredientItem ingredientItem = new IngredientItem(new IngredientDesc(ingredient.getName()), ingredient.getQuantity(), ingredient.getUnit());
            ingredients.add(ingredientItem);
        }
        return ingredients;
    }
}
