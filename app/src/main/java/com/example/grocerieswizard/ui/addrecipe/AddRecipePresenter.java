package com.example.grocerieswizard.ui.addrecipe;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

    public void bindView(AddRecipeContract.View view) {
        this.view = view;
    }

    // TODO: Not used, should be used in onStop lifecycle method of the fragment.
    public void unbindView() {
        this.view = null;
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

    // TODO: We shouldn't pass views to presenter.
    @Override
    public void searchMeal(String inputText, TextView editRecipeHowToPrepare, ImageView addImage) {
        recipeRepository.searchMeals(inputText, new RepositoryCallback<List<RecipeItem>>() {
            @Override
            public void onSuccess(List<RecipeItem> data) {
                if (data.isEmpty()) return;
                if (view != null) {
                    Log.d(TAG, "onSuccess: data0: " + data.get(0).getImage());
                    view.showAlertDialogForFoundRecipe(uiMapper.toRecipeUi(data.get(0)), editRecipeHowToPrepare, addImage);
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
}
