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

    public RecipeModel(String recipeName, List<IngredientModel> ingredients, String howToPrepare, Uri recipeImageUri) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.howToPrepare = howToPrepare;
        this.recipeImageUri = recipeImageUri;
    }

    protected RecipeModel(Parcel in) {
        recipeName = in.readString();
        recipeImageUri = in.readParcelable(Uri.class.getClassLoader());
        howToPrepare = in.readString();
        ingredients = in.createTypedArrayList(IngredientModel.CREATOR);
    }

    public static final Parcelable.Creator<RecipeModel> CREATOR = new Parcelable.Creator<RecipeModel>() {
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
        dest.writeParcelable(recipeImageUri, flags);
        dest.writeString(howToPrepare);
        dest.writeList(ingredients);
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
}
