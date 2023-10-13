package com.example.grocerieswizard.addRecipe;

import com.example.grocerieswizard.ui.model.IngredientUi;

public interface AddInterface {
    void onItemEdit(IngredientUi ingredientUi);

    void onItemDelete(IngredientUi ingredientUi);
}
