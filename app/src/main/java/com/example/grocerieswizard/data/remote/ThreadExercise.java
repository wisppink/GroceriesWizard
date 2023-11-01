package com.example.grocerieswizard.data.remote;

import android.os.AsyncTask;

import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.remote.model.MealResponse;
import com.example.grocerieswizard.data.repo.RepoMapper;
import com.example.grocerieswizard.data.repo.RepositoryCallback;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ThreadExercise extends AsyncTask<String, Void, MealResponse> {
    private final RepositoryCallback<List<RecipeItem>> callback;
    private Exception exception;
    private final RecipeRemoteDataSource remoteDataSource;
    private final RepoMapper repoMapper;

    public ThreadExercise(RepositoryCallback<List<RecipeItem>> callback, RecipeRemoteDataSource remoteDataSource, RepoMapper repoMapper) {
        this.callback = callback;
        this.remoteDataSource = remoteDataSource;
        this.repoMapper = repoMapper;
    }


    @Override
    protected MealResponse doInBackground(String... strings) {
        try {
            String query = strings[0];
            Call<MealResponse> call = remoteDataSource.searchMeals(query);
            Response<MealResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                exception = new Exception(response.message());
            }
        } catch (IOException e) {
            exception = new Exception(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(MealResponse result) {
        if (result != null) {
            callback.onSuccess(repoMapper.toRecipes(result));
        } else {
            callback.onError(exception);
        }
    }
}
