package com.example.grocerieswizard.ui.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.grocerieswizard.addRecipe.IngredientModel;

import java.util.List;

public class RecipeUi implements Parcelable {

    private String recipeName;
    private Bitmap imageBitmap;
    private String instructions;
    private List<IngredientModel> ingredients;
    private boolean isSwiped;
    private int id;
    private final String TAG = "RecipeModel";

    public RecipeUi(String recipeName, List<IngredientModel> ingredients, String instructions, Bitmap imageBitmap) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageBitmap = imageBitmap;
        isSwiped = false;
    }

    protected RecipeUi(Parcel in) {
        recipeName = in.readString();
        imageBitmap = in.readParcelable(Uri.class.getClassLoader());
        instructions = in.readString();
        ingredients = in.createTypedArrayList(IngredientModel.CREATOR);
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
        dest.writeParcelable(imageBitmap, flags);
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

    public Bitmap getImageBitmap() {
        if (imageBitmap != null) {
            Log.d(TAG, "getImageBitmap: " + imageBitmap);
        } else {
            Log.d(TAG, "getImageBitmap:  null");
        }
        return imageBitmap;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
        if (imageBitmap != null)
            Log.d(TAG, imageBitmap.toString());
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
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

}