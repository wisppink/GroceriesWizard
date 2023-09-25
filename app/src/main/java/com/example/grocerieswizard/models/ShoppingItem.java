package com.example.grocerieswizard.models;

import com.example.grocerieswizard.adapters.ShopAdapter;
import com.example.grocerieswizard.interfaces.ShopInterface;

import java.util.HashMap;
import java.util.Map;

public class ShoppingItem {
    private final String ingredientName;

    private final Map<SubShoppingItem, Boolean> subShoppingItems = new HashMap<>();
    private ShopAdapter shopAdapter;
    private ShopInterface shopInterface;

    public ShoppingItem(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Map<SubShoppingItem, Boolean> getSubShoppingItems() {
        return subShoppingItems;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void addSubItem(SubShoppingItem subItem, boolean isChecked) {
        subShoppingItems.put(subItem, isChecked);

    }

    public void setAdapter(ShopAdapter shopAdapter) {
        this.shopAdapter = shopAdapter;
    }

    public ShopAdapter getAdapter() {
        return shopAdapter;
    }

    public String getTotal(Map<SubShoppingItem, Boolean> subShoppingItemBooleanMap) {
        return shopInterface.generateTotal(subShoppingItemBooleanMap);
    }

    public void setShopInterface(ShopInterface shopInterface) {
        this.shopInterface = shopInterface;
    }
}
