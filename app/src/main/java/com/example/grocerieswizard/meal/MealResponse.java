package com.example.grocerieswizard.meal;

@SuppressWarnings("unused")
public class MealResponse {
    private MealService.Meal[] meals;

    public MealService.Meal[] getMeals() {
        return meals;
    }
    public MealService.Meal getMeal(int i) {
        if (meals != null && i >= 0 && i < meals.length) {
            return meals[i];
        } else {
            return null;
        }
    }

}

