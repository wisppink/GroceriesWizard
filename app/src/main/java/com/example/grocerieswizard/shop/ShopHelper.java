package com.example.grocerieswizard.shop;

import com.example.grocerieswizard.ui.model.RecipeUi;
import com.example.grocerieswizard.shop.subshop.SubShoppingItem;

import java.util.ArrayList;
import java.util.List;

public interface ShopHelper {

    ArrayList<ShoppingItem> generateShoppingItems(List<RecipeUi> recipeUis);

    String generateTotal(ArrayList<SubShoppingItem> subShoppingItems);
}