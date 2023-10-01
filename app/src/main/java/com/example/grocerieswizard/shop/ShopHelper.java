package com.example.grocerieswizard.shop;

import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.shop.subshop.SubShoppingItem;

import java.util.ArrayList;
import java.util.Map;

public interface ShopHelper {

    ArrayList<ShoppingItem> generateShoppingItems(ArrayList<RecipeModel> recipes);

    String generateTotal(Map<SubShoppingItem, Boolean> subShoppingItems);
}