package com.example.grocerieswizard.di;

import android.content.Context;

import com.example.grocerieswizard.data.local.GWDatabase;
import com.example.grocerieswizard.data.local.RecipeLocalDataSource;
import com.example.grocerieswizard.data.local.RecipeLocalDataSourceImpl;
import com.example.grocerieswizard.data.remote.RecipeRemoteDataSource;
import com.example.grocerieswizard.data.remote.RecipeRemoteDataSourceImpl;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.data.repo.RecipeRepositoryImpl;
import com.example.grocerieswizard.data.repo.RepoMapper;
import com.example.grocerieswizard.ui.UiMapper;

public class GroceriesWizardInjector {
    private static GroceriesWizardInjector instance = null;

    private final RecipeRepository recipeRepository;
    private final UiMapper uiMapper;
    private final RepoMapper repoMapper;

    private GroceriesWizardInjector(Context context) {
        GWDatabase database = GWDatabase.getInstance(context);
        RecipeLocalDataSource recipeLocalDataSource = new RecipeLocalDataSourceImpl(database.cartDao(),database.favDao(),database.ingredientDao(), database.recipeDao());
        RecipeRemoteDataSource recipeRemoteDataSource = new RecipeRemoteDataSourceImpl();
        uiMapper = new UiMapper();
        repoMapper = new RepoMapper();
        recipeRepository = new RecipeRepositoryImpl(recipeLocalDataSource, recipeRemoteDataSource, repoMapper);
    }

    public static void init(Context context) {
        instance = new GroceriesWizardInjector(context);
    }

    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }

    public UiMapper getUiMapper() {
        return uiMapper;
    }

    public RepoMapper getRepoMapper() {
        return repoMapper;
    }

    public static synchronized GroceriesWizardInjector getInstance() {
        if (instance == null)
            throw new IllegalStateException("GroceriesWizardInjector no initialized");

        return instance;
    }
}
