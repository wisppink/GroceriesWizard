package com.example.grocerieswizard.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RecipeModel implements Parcelable {

    private String recipeName;
    private Uri recipeImageUri;
    private String instructions;
    private List<IngredientModel> ingredients;
    private boolean isSwiped;
    private int id;
    private boolean isSelected;

    private boolean isFavorite;

    public RecipeModel(String recipeName, List<IngredientModel> ingredients, String instructions, Uri recipeImageUri) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeImageUri = recipeImageUri;
        isSwiped = false;
        isSelected = false;

    }

    protected RecipeModel(Parcel in) {
        recipeName = in.readString();
        recipeImageUri = in.readParcelable(Uri.class.getClassLoader());
        instructions = in.readString();
        ingredients = in.createTypedArrayList(IngredientModel.CREATOR);
    }

    public static final Parcelable.Creator<RecipeModel> CREATOR = new Parcelable.Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel source) {
            return new RecipeModel(source);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeParcelable(recipeImageUri, flags);
        dest.writeString(instructions);
        dest.writeTypedList(ingredients); // Changed from writeList to writeTypedList
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Uri getRecipeImageUri() {
        return recipeImageUri;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void addIngredient(IngredientModel ingredient) {
        ingredients.add(ingredient);
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeImageUri(Uri recipeImageUri) {
        this.recipeImageUri = recipeImageUri;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    // Getter and Setter for isSwiped
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}
