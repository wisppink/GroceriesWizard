package com.example.grocerieswizard.interfaces;

import com.example.grocerieswizard.models.IngredientModel;

public interface AddInterface {
    void onItemEdit(IngredientModel ingredientModel);

    void onItemDelete(IngredientModel ingredientModel);
}
