package com.example.grocerieswizard.interfaces;

import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;
import java.util.Map;

public interface ShopInterface {

    ArrayList<ShoppingItem> generateShoppingItems(ArrayList<RecipeModel> recipes);

    String generateTotal(Map<SubShoppingItem, Boolean> subShoppingItems);
}
