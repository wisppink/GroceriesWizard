package com.example.grocerieswizard.data.local.typeConverter;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.grocerieswizard.data.local.model.IngredientItem;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientItemListConverter {
    @TypeConverter
    public static List<IngredientItem> fromString(String value) {
        Type listType = new TypeToken<List<IngredientItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<IngredientItem> ingredientList) {
        Gson gson = new Gson();
        return gson.toJson(ingredientList);
    }
}
