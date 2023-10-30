package com.example.grocerieswizard.data.repo;

import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.remote.model.Meal;
import com.example.grocerieswizard.data.remote.model.MealResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RepoMapper {
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
}
