package com.example.grocerieswizard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class IngredientModel implements Parcelable {
    private String name;
    private double quantity;
    private String unit;

    public IngredientModel(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    protected IngredientModel(Parcel in) {
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

    public static final Creator<IngredientModel> CREATOR = new Creator<IngredientModel>() {
        @Override
        public IngredientModel createFromParcel(Parcel in) {
            return new IngredientModel(in);
        }

        @Override
        public IngredientModel[] newArray(int size) {
            return new IngredientModel[size];
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
}
