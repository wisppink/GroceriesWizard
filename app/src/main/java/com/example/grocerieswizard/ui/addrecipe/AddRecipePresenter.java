package com.example.grocerieswizard.ui.addrecipe;

import android.util.Log;

import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.data.repo.RepositoryCallback;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public class AddRecipePresenter implements AddRecipeContract.Presenter {

    private final RecipeRepository recipeRepository;
    private final UiMapper uiMapper;
    private static final String TAG = "AddRecipePresenter";
    private AddRecipeContract.View view;

    public AddRecipePresenter(RecipeRepository recipeRepository, UiMapper uiMapper) {
        this.recipeRepository = recipeRepository;
        this.uiMapper = uiMapper;
    }

    public void unbindView() {
        this.view = null;
    }

    @Override
    public void PositiveButton(RecipeItem recipeItem) {
        view.onPositiveButtonCalled(uiMapper.toRecipeUi(recipeItem));
    }

    @Override
    public void negativeButton() {
        view.onNegativeButtonCalled();
    }

    @Override
    public void deleteIngredient(IngredientUi ingredient) {
        recipeRepository.deleteIngredient(uiMapper.toIngredient(ingredient));
    }

    @Override
    public void deleteRecipe(RecipeUi recipeUi) {
        recipeRepository.deleteRecipe(recipeUi);
    }

    @Override
    public void insertRecipe(RecipeUi recipeUi) {
        recipeRepository.insertRecipe(uiMapper.toRecipe(recipeUi));
    }

    @Override
    public void searchMeal(String inputText) {
        recipeRepository.searchMeals(inputText, new RepositoryCallback<List<RecipeItem>>() {
            @Override
            public void onSuccess(List<RecipeItem> data) {
                if (data.isEmpty()) {
                    if (view != null) {
                        Log.d(TAG, "No recipes found");
                    }
                } else {
                    if (view != null) {
                        Log.d(TAG, "onSuccess: data0: " + data.get(0).getImage());
                        view.showAlertDialogForFoundRecipe(data.get(0));
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError", e);
            }
        });
    }

    @Override
    public void insertIngredient(IngredientUi ingredientUi) {
        IngredientItem ingredient = uiMapper.toIngredient(ingredientUi);
        recipeRepository.insertIngredient(ingredient);
    }

    @Override
    public void bindView(AddRecipeContract.View view) {
        this.view = view;
    }
}
