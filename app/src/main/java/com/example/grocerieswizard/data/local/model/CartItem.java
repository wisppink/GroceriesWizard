package com.example.grocerieswizard.data.local.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",
        foreignKeys = @ForeignKey(
                entity = RecipeItem.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class CartItem {
    @PrimaryKey
    long id;
    long recipeId;

    public CartItem(long recipeId) {
        this.recipeId = recipeId;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

}
