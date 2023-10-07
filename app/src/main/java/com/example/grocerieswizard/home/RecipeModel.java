package com.example.grocerieswizard.home;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.grocerieswizard.Fav.FavInterface;
import com.example.grocerieswizard.Fav.FavRecyclerViewAdapter;
import com.example.grocerieswizard.addRecipe.IngredientModel;

import java.util.List;

public class RecipeModel implements Parcelable {

    private String recipeName;
    private Bitmap imageBitmap;
    private String instructions;
    private List<IngredientModel> ingredients;
    private boolean isSwiped;
    private int id;
    private boolean isSelected;
    private final String TAG = "RecipeModel";
    private FavInterface favInterface;
    private FavRecyclerViewAdapter favAdapter;

    public RecipeModel(String recipeName, List<IngredientModel> ingredients, String instructions, Bitmap imageBitmap) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageBitmap = imageBitmap;
        isSwiped = false;
        isSelected = false;
    }

    protected RecipeModel(Parcel in) {
        recipeName = in.readString();
        imageBitmap = in.readParcelable(Uri.class.getClassLoader());
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public FavInterface getFavInterface() {
        return favInterface;
    }

    public void setFavInterface(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    public FavRecyclerViewAdapter getFavAdapter() {
        return favAdapter;
    }

    public void setFavAdapter(FavRecyclerViewAdapter favAdapter) {
        this.favAdapter = favAdapter;
    }

}