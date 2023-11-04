package com.example.grocerieswizard.ui.fav;

import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;
import java.util.stream.Collectors;

public class FavPresenter implements FavContract.Presenter {

    FavContract.View view;
    private final RecipeRepository recipeRepository;
    private final UiMapper uiMapper;

    public FavPresenter(RecipeRepository recipeRepository, UiMapper uiMapper) {
        this.recipeRepository = recipeRepository;
        this.uiMapper = uiMapper;
    }

    public void bindView(FavContract.View view) {
        this.view = view;
    }

    public void setFavList() {
        List<RecipeUi> recipeUis = recipeRepository.getFavoriteRecipes().stream()
                .map(favItem -> {
                    RecipeItem recipe = recipeRepository.getRecipeByRecipeId(favItem.getRecipeId());
                    recipe.setFav(true);
                    return uiMapper.toRecipeUi(recipe);
                })
                .collect(Collectors.toList());
        if (view != null) {
            view.showFavList(recipeUis);
        }
    }

    public void removeFromFavorites(RecipeUi recipeUi) {
        recipeRepository.deleteRecipeFromFavorites(recipeUi);
        if (view != null) {
            view.removeFromFavorites(recipeUi);
        }
    }

    public boolean isRecipeInCart(int id) {
        return recipeRepository.isRecipeInCart(id);
    }

    public void insertCartItem(RecipeUi recipeUi) {
        recipeRepository.insertCartItem(recipeUi);
    }

    public void removeFromCart(RecipeUi recipeUi) {
        recipeRepository.deleteRecipeFromCart(recipeUi);
    }
}
