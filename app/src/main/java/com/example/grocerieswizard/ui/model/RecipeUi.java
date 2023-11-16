package com.example.grocerieswizard.ui.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class RecipeUi implements Parcelable {

    private String recipeName;
    private String instructions;
    private List<IngredientUi> ingredients;
    private boolean isSwiped;
    private boolean isFav;
    private boolean isCart;
    private int id;
    private String image;

    public RecipeUi(String recipeName, List<IngredientUi> ingredients, String instructions) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        isSwiped = false;
        isFav = false;
        isCart = false;
    }

    protected RecipeUi(Parcel in) {
        recipeName = in.readString();
        image = in.readString();
        instructions = in.readString();
        ingredients = in.createTypedArrayList(IngredientUi.CREATOR);
    }

    public static final Parcelable.Creator<RecipeUi> CREATOR = new Parcelable.Creator<RecipeUi>() {
        @Override
        public RecipeUi createFromParcel(Parcel source) {
            return new RecipeUi(source);
        }

        @Override
        public RecipeUi[] newArray(int size) {
            return new RecipeUi[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeString(image);
        dest.writeString(instructions);
        dest.writeTypedList(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<IngredientUi> getIngredients() {
        return ingredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(List<IngredientUi> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isSwiped() {
        return isSwiped;
    }

    public void setSwiped(boolean swiped) {
        isSwiped = swiped;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}