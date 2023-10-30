package com.example.grocerieswizard.data.local.typeConverter;

import androidx.room.TypeConverter;

import com.example.grocerieswizard.data.local.model.IngredientDesc;

public class IngredientDescConverter {
    @TypeConverter
    public static IngredientDesc fromString(String value) {
        return new IngredientDesc(value.trim());
    }

    @TypeConverter
    public static String toString(IngredientDesc ingredientDesc) {
       return ingredientDesc.getName() + " ";
    }
}
