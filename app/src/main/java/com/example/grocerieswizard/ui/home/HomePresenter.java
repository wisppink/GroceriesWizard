package com.example.grocerieswizard.ui.home;

import android.util.Log;

import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.stream.Collectors;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private final RecipeRepository recipeRepository;
    private final UiMapper uiMapper;

    public HomePresenter(RecipeRepository recipeRepository, UiMapper uiMapper) {
        this.recipeRepository = recipeRepository;
        this.uiMapper = uiMapper;
    }
    @Override
    public void bindView(HomeContract.View view){
         this.view = view;
    }
    @Override
    public void unbindView(){
        this.view = null;
    }

    // Load recipes and return a list of RecipeUi
    public void loadRecipes() {
        if(view!=null){
            view.showRecipes(recipeRepository.getAllRecipes().stream()
                    .map(uiMapper::toRecipeUi)
                    .collect(Collectors.toList()));}

    }

    public Boolean isRecipeSelected(int id) {
        return recipeRepository.isRecipeInCart(id);
    }

    public int updateRecipe(RecipeUi oldRecipeUi) {
        return recipeRepository.updateRecipe(oldRecipeUi.getId(), uiMapper.toRecipe(oldRecipeUi));
    }

    public void insertRecipe(RecipeUi recipeUi) {
        recipeRepository.insertRecipe(uiMapper.toRecipe(recipeUi));
    }

    public void deleteRecipe(RecipeUi recipeUi) {
        recipeRepository.deleteRecipe(recipeUi);
        view.showDeleteConfirmation(recipeUi);
    }

    public void deleteSelectedRecipe(int recipeId) {
        recipeRepository.deleteCartItem(recipeId);
    }

    public void insertSelectedRecipe(int recipeId) {
        CartItem cartItem = new CartItem(recipeId);
        recipeRepository.insertCartItem(cartItem);
    }

    public boolean isRecipeFavorite(int id) {
        return recipeRepository.isRecipeFavorite(id);
    }

    public void insertRecipeFav(int recipeId) {
        recipeRepository.insertRecipeFav(recipeId);
    }

    public void deleteRecipeFav(int recipeId) {
        recipeRepository.deleteRecipeFromFavorites(recipeId);
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
}
