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
        if (view != null) {
            this.view = view;
        }
    }

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
    @Override
    public void searchMeal(String inputText, TextView editRecipeHowToPrepare, ImageView addImage) {
        recipeRepository.searchMeals(inputText, new RepositoryCallback<List<RecipeItem>>() {
            @Override
            public void onSuccess(List<RecipeItem> data) {
                if (data.isEmpty())
                    return;
                //TODO:recipe image

                                /*
                                Picasso.get().load(data.get(0).getImageUrl()).resize(150, 150).centerCrop().into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        data.get(0).setImageBitmap(bitmap);
                                        Log.d(TAG, "onBitmapLoaded: success");
                                        Log.d(TAG, "onBitmapLoaded: recipe image bitmap: " + data.get(0).getImageBitmap());
                                        showAlertDialogForFoundRecipe(uiMapper.toRecipeUi(data.get(0)), binding.editRecipeHowToPrepare, binding.addImage,bitmap);
                                        recipeUi.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        Log.e(TAG, "onBitmapFailed: ", e);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });

                                */

                if (view != null) {
                    Log.d(TAG, "onSuccess: data0: " + data.get(0).getImage());
                    view.showAlertDialogForFoundRecipe(uiMapper.toRecipeUi(data.get(0)), editRecipeHowToPrepare, addImage);
                }


                //,bitmap
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
