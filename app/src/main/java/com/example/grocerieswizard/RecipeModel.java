package com.example.grocerieswizard;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RecipeModel implements Parcelable {

    private String recipeName;
    private Uri recipeImageUri;
    private String howToPrepare;
    private List<IngredientModel> ingredients;
    private boolean isSwiped;

    public RecipeModel(String recipeName, List<IngredientModel> ingredients, String howToPrepare, Uri recipeImageUri) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.howToPrepare = howToPrepare;
        this.recipeImageUri = recipeImageUri;
        isSwiped = false;
    }

    protected RecipeModel(Parcel in) {
        recipeName = in.readString();
        recipeImageUri = in.readParcelable(Uri.class.getClassLoader());
        howToPrepare = in.readString();
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
        dest.writeString(howToPrepare);
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

    public String getHowToPrepare() {
        return howToPrepare;
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

    public void setHowToPrepare(String howToPrepare) {
        this.howToPrepare = howToPrepare;
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

}
