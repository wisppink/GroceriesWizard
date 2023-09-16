package com.example.grocerieswizard.interfaces;

import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.models.ShoppingItem;

import java.util.ArrayList;

public interface ShopInterface {

    ArrayList<ShoppingItem> generateShoppingItems(ArrayList<RecipeModel> recipes);
}
