package com.example.grocerieswizard.data.repo.model;

import com.example.grocerieswizard.data.remote.model.Meal;
import com.example.grocerieswizard.data.remote.model.MealResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RepoMapper {
    public List<Recipe> toRecipes(MealResponse mealResponse) {
        if (mealResponse.getMeals() == null) return new ArrayList<>();
        return Arrays.stream(mealResponse.getMeals())
                .map(this::toRecipe)
                .collect(Collectors.toList());
    }

    Recipe toRecipe(Meal meal) {
        return new Recipe(meal.getStrMeal(), meal.getIngredients(), meal.getStrInstructions(), meal.getStrMealThumb());
    }
}
