package com.example.grocerieswizard.data.local.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite",
        foreignKeys = @ForeignKey(
                entity = RecipeItem.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class FavItem {
    @PrimaryKey
    long id;
    int recipeId;

    public FavItem(int recipeId) {
        this.recipeId = recipeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}



