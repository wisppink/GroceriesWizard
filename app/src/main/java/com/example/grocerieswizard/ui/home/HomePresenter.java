package com.example.grocerieswizard.ui.home;

import android.util.Log;

import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.stream.Collectors;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private final RecipeRepository recipeRepository;
    private final UiMapper uiMapper;
    private static final String TAG = "HomePresenter";

    public HomePresenter(RecipeRepository recipeRepository, UiMapper uiMapper) {
        this.recipeRepository = recipeRepository;
        this.uiMapper = uiMapper;
    }

    @Override
    public void bindView(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    // Load recipes and return a list of RecipeUi
    public void loadRecipes() {
        if (view != null) {
            view.showRecipes(recipeRepository.getAllRecipes().stream()
                    .map(uiMapper::toRecipeUi)
                    .collect(Collectors.toList()));
        }

    }

    public int updateRecipe(RecipeUi oldRecipeUi) {
        return recipeRepository.updateRecipe(oldRecipeUi.getId(), uiMapper.toRecipe(oldRecipeUi));
    }

    public void deleteRecipe(RecipeUi recipeUi) {
        view.showDeleteConfirmation(recipeUi);
    }
    @Override
    public void showDetails(RecipeUi recipe) {
        if (recipe != null) {
            if (!recipe.isSwiped()) {
                recipe.setSwiped(false);
                view.showRecipeDetails(recipe);
            } else {
                Log.d("MainActivity", "recipe model null");
            }
        }
    }

    @Override
    public void deleteFromDB(RecipeUi recipe) {
        recipeRepository.deleteRecipe(recipe);
        view.onRecipeDeleted(recipe);
    }

    @Override
    public void onToggleFavoriteRecipeClick(RecipeUi recipeUi) {
        Log.d(TAG, "onToggleFavoriteRecipeClick: recipeUi came to the presenter as: " + recipeUi.isFav());
        if (recipeUi.isFav()) {
            recipeUi.setFav(false);
            recipeRepository.deleteRecipeFromFavorites(recipeUi);
            view.recipeRemovedFromFavorites(recipeUi);
        } else {
            recipeUi.setFav(true);
            recipeRepository.insertRecipeFav(recipeUi);
            view.recipeAddedToFavorites(recipeUi);
        }
    }

    @Override
    public void onToggleCartRecipeClick(RecipeUi recipeUi) {
        Log.d(TAG, "onToggleCartRecipeClick: recipeUi came to the presenter as: " + recipeUi.isCart());
        if (recipeUi.isCart()) {
            recipeUi.setCart(false);
            recipeRepository.deleteRecipeFromCart(recipeUi);
            view.recipeRemovedFromCart(recipeUi);
        } else {
            recipeUi.setCart(true);
            recipeRepository.insertCartItem(recipeUi);
            view.recipeAddedToCart(recipeUi);
        }
    }
}
