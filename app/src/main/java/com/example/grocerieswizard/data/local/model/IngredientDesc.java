package com.example.grocerieswizard.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient_desc")
public class IngredientDesc {
    @PrimaryKey
    long id;
    String name;

    public IngredientDesc(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
