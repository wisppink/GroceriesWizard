package com.example.grocerieswizard.meal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String query);
}

