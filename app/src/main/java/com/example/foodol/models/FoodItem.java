package com.example.foodol.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {

    private String foodItem;
    private int quantity;
    private FoodContact provider;

    FoodItem() {}

    public FoodItem(String foodItem, int quantity, FoodContact provider) {
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.provider = provider;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public FoodContact getProvider() {
        return provider;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodItem);
        dest.writeInt(this.quantity);
        dest.writeParcelable(this.provider, flags);
    }

    protected FoodItem(Parcel in) {
        this.foodItem = in.readString();
        this.quantity = in.readInt();
        this.provider = in.readParcelable(FoodContact.class.getClassLoader());
    }

    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel source) {
            return new FoodItem(source);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}
