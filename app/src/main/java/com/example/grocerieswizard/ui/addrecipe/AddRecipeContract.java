package com.example.grocerieswizard.ui.addrecipe;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

public interface AddRecipeContract {

    interface View{
        void showAlertDialogForFoundRecipe(RecipeUi recipeUi, TextView howToPrepare, ImageView addImage);
    }

    interface Presenter{

        void deleteIngredient(IngredientUi ingredient);

        void deleteRecipe(RecipeUi recipeUi);

        void insertRecipe(RecipeUi recipeUi);

        void searchMeal(String inputText, TextView editRecipeHowToPrepare, ImageView addImage);

        void insertIngredient(IngredientUi ingredientUi);
    }
}
