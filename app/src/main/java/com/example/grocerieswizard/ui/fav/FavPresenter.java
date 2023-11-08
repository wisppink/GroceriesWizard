package com.example.grocerieswizard.ui.fav;

import android.util.Log;

import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.stream.Collectors;

public class FavPresenter implements FavContract.Presenter {

    FavContract.View view;
    private final RecipeRepository recipeRepository;
    private final UiMapper uiMapper;
    private static final String TAG = "FavPresenter";

    public FavPresenter(RecipeRepository recipeRepository, UiMapper uiMapper) {
        this.recipeRepository = recipeRepository;
        this.uiMapper = uiMapper;
    }

    public void bindView(FavContract.View view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    public void loadRecipes() {
        if (view != null) {
            view.showRecipes(recipeRepository.getFavoriteRecipes().stream()
                    .map(favItem -> {
                        RecipeItem recipe = recipeRepository.getRecipeByRecipeId(favItem.getRecipeId());
                        recipe.setFav(true);
                        return uiMapper.toRecipeUi(recipe);
                    })
                    .collect(Collectors.toList()));
        }
    }

    public void onToggleFavoriteRecipeClick(RecipeUi recipeUi) {
        Log.d(TAG, "onToggleFavoriteRecipeClick: recipeUi came to the presenter as: " + recipeUi.isFav());
        if (recipeUi.isFav()) {
            recipeUi.setFav(false);
           view.showDeleteConfirmation(recipeUi);
        } else {
            recipeUi.setFav(true);
            recipeRepository.insertRecipeFav(recipeUi);
            view.recipeAddedToFavorites(recipeUi);
        }
    }

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

    public void deleteFromDB(RecipeUi recipe) {
        recipeRepository.deleteRecipeFromFavorites(recipe);
        view.onRecipeDeleted(recipe);
    }
}

