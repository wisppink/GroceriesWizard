package com.example.grocerieswizard.ui.addrecipe;

import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

public interface AddRecipeContract {

    interface View {
        void showAlertDialogForFoundRecipe(RecipeItem recipeItem);

        void onPositiveButtonCalled(RecipeUi recipeUi);

        void onNegativeButtonCalled();
    }

    interface Presenter {

        void deleteIngredient(IngredientUi ingredient);

        void deleteRecipe(RecipeUi recipeUi);

        void insertRecipe(RecipeUi recipeUi);

        void searchMeal(String inputText);

        void insertIngredient(IngredientUi ingredientUi);

        void bindView(AddRecipeContract.View view);

        void unbindView();

        void PositiveButton(RecipeItem recipeItem);

        void negativeButton();

    }
}
