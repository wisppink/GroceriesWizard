package com.example.grocerieswizard.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.grocerieswizard.addRecipe.AddInterface;

public class IngredientUi implements Parcelable {
    private String name;
    private double quantity;
    private String unit;
    private int id;
    private long recipeId;
    private AddInterface addinterface;

    public IngredientUi(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    protected IngredientUi(Parcel in) {
        name = in.readString();
        quantity = in.readDouble();
        unit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(quantity);
        dest.writeString(unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " " + getQuantity() + " " + getUnit();
    }

    public static final Creator<IngredientUi> CREATOR = new Creator<IngredientUi>() {
        @Override
        public IngredientUi createFromParcel(Parcel in) {
            return new IngredientUi(in);
        }

        @Override
        public IngredientUi[] newArray(int size) {
            return new IngredientUi[size];
        }
    };

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public AddInterface getInterface() {
        return addinterface;
    }

    public void setInterface(AddInterface addInterface) {
        this.addinterface = addInterface;
    }
}
