package com.example.grocerieswizard.data.remote.model;

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
