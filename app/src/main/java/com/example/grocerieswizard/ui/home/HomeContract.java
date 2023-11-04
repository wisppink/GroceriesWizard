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

        void recipeAddedToFavorites(RecipeUi recipeUi);

        void recipeRemovedFromFavorites(RecipeUi recipeUi);

        void onRecipeDeleted(RecipeUi recipe);

        void recipeAddedToCart(RecipeUi recipeUi);

        void recipeRemovedFromCart(RecipeUi recipeUi);
    }

    interface Presenter {
        void bindView(HomeContract.View view);
        void unbindView();

        void loadRecipes();

        int updateRecipe(RecipeUi oldRecipeUi);

        void deleteRecipe(RecipeUi recipeUi);

        void showDetails(RecipeUi recipe);

        void deleteFromDB(RecipeUi recipe);

        void onToggleFavoriteRecipeClick(RecipeUi recipeUi);

        void onToggleCartRecipeClick(RecipeUi recipeUi);
    }
}
