package com.example.grocerieswizard.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class IngredientItem {
    @PrimaryKey
    @NonNull
    IngredientDesc ingredientDesc;
    Double quantity;
    String unit;

    public IngredientItem(@NonNull IngredientDesc ingredientDesc, Double quantity, String unit) {
        this.ingredientDesc = ingredientDesc;
        this.quantity = quantity;
        this.unit = unit;
    }

    @NonNull
    public IngredientDesc getIngredientDesc() {
        return ingredientDesc;
    }

    public void setIngredientDesc(@NonNull IngredientDesc ingredientDesc) {
        this.ingredientDesc = ingredientDesc;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
