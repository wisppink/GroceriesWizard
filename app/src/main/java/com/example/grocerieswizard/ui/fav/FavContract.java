package com.example.grocerieswizard.ui.fav;

import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public interface FavContract {
    interface View {

        void recipeAddedToFavorites(RecipeUi recipeUi);

        void recipeAddedToCart(RecipeUi recipeUi);

        void recipeRemovedFromCart(RecipeUi recipeUi);

        void showRecipes(List<RecipeUi> recipeUiList);

        void showDeleteConfirmation(RecipeUi recipeUi);

        void onRecipeDeleted(RecipeUi recipe);
    }

    interface Presenter {

        void bindView(View view);

        void unbindView();
        void loadRecipes();

        void onToggleFavoriteRecipeClick(RecipeUi recipeUi);

        void onToggleCartRecipeClick(RecipeUi recipeUi);
    }
}
