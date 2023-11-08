package com.example.grocerieswizard.data.local.model;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "recipe")
public class RecipeItem {
    @PrimaryKey(autoGenerate = true)
    long id;
    String name;
    String instructors;
    List<IngredientItem> ingredientList;
    boolean isFav;
    String image;
    boolean isCart;

    public RecipeItem(String name, String instructors, List<IngredientItem> ingredientList) {
        this.name = name;
        this.instructors = instructors;
        this.ingredientList = ingredientList;
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

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }

    public List<IngredientItem> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<IngredientItem> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public boolean isCart() {
        return isCart;
    }

    public void setCart(boolean cart) {
        isCart = cart;
    }

    public String getImage() {
        Log.d("item", "getImage: " + image);
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
