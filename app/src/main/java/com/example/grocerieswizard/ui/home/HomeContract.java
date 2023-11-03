package com.example.grocerieswizard.ui.home;

import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public interface HomeContract {
    interface View {
        void showRecipes(List<RecipeUi> recipes);

        void showDeleteConfirmation(RecipeUi recipe);

        void showEditRecipe(RecipeUi recipe);

        void showRecipeShare(RecipeUi recipe);
        void showRecipeDetails(RecipeUi recipe);
    }

    interface Presenter {
        void bindView(HomeContract.View view);
        void unbindView();

        void loadRecipes();

        Boolean isRecipeSelected(int id);

        int updateRecipe(RecipeUi oldRecipeUi);

        void insertRecipe(RecipeUi recipeUi);

        void deleteRecipe(RecipeUi recipeUi);

        void deleteSelectedRecipe(int recipeId);

        void insertSelectedRecipe(int recipeId);

        boolean isRecipeFavorite(int id);

        void insertRecipeFav(int recipeId);

        void deleteRecipeFav(int recipeId);

        void showDetails(RecipeUi recipe);
    }
}
