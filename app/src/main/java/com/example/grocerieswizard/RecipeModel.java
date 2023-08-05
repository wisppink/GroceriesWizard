package com.example.grocerieswizard;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class RecipeModel implements Parcelable {

    private String recipeName;
    private Uri recipeImageUri;
    private String howToPrepare;
    private String ingredients;
    public RecipeModel(String recipeName, String ingredients, String howToPrepare, Uri recipeImageUri) {
        this.recipeName = recipeName;
        this.recipeImageUri = recipeImageUri;
        this.howToPrepare = howToPrepare;
        this.ingredients = ingredients;
    }

    protected RecipeModel(Parcel in) {
        recipeName = in.readString();
        recipeImageUri = Uri.parse(in.readString());
        howToPrepare = in.readString();
        ingredients = in.readString();
    }


    public static final Creator<RecipeModel> CREATOR = new Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel in) {
            return new RecipeModel(in);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeString(String.valueOf(recipeImageUri));
        dest.writeString(howToPrepare);
        dest.writeString(ingredients);
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

    public String getIngredients() {
        return ingredients;
    }
}
