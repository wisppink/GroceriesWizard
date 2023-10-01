package com.example.grocerieswizard.shop;

import com.example.grocerieswizard.shop.subshop.SubShoppingItem;

import java.util.HashMap;
import java.util.Map;

public class ShoppingItem {
    private final String ingredientName;

    private final Map<SubShoppingItem, Boolean> subShoppingItems = new HashMap<>();

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

    public Boolean ControlAllValuesTrue(){
        for (Boolean value : getSubShoppingItems().values()) {
            if (!value) {
                return false;
            }
        }
        return true;
    }

    public void setEverySubValue(boolean b){
        for (Map.Entry<SubShoppingItem, Boolean> entry : getSubShoppingItems().entrySet()) {
            entry.setValue(b);
        }
    }
}
