package com.example.grocerieswizard;

import com.example.grocerieswizard.models.Meal;
@SuppressWarnings("unused")
public class MealResponse {
    private Meal[] meals;

    public Meal[] getMeals() {
        return meals;
    }
    public Meal getMeal(int i) {
        if (meals != null && i >= 0 && i < meals.length) {
            return meals[i];
        } else {
            return null;
        }
    }

}

