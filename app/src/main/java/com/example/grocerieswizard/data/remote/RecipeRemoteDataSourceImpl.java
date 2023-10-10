package com.example.grocerieswizard.data.remote;

import com.example.grocerieswizard.data.remote.model.MealResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRemoteDataSourceImpl implements RecipeRemoteDataSource {

    private final MealService mealApi;

    public RecipeRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealApi = retrofit.create(MealService.class);
    }

    @Override
    public Call<MealResponse> searchMeals(String query) {
        return mealApi.searchMeals(query);
    }
}