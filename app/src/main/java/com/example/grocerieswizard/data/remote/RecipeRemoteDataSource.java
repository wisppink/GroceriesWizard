package com.example.grocerieswizard.data.remote;

import com.example.grocerieswizard.data.remote.model.MealResponse;

import retrofit2.Call;

public interface RecipeRemoteDataSource {
    Call<MealResponse> searchMeals(String query);
}