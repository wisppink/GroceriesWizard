package com.example.grocerieswizard.shop;

import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.shop.subshop.SubShoppingItem;

import java.util.ArrayList;

public interface ShopHelper {

    ArrayList<ShoppingItem> generateShoppingItems(ArrayList<RecipeModel> recipes);

    String generateTotal(ArrayList<SubShoppingItem> subShoppingItems);
}