package com.example.foodol.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodContact implements Parcelable {

    private String name;
    private String phone;
    private String postalAddress;

    public FoodContact() {}

    public FoodContact(String name, String phone, String postalAddress) {
        this.name = name;
        this.phone = phone;
        this.postalAddress = postalAddress;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPostalAddress() {
        return this.postalAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.postalAddress);
    }

    protected FoodContact(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.postalAddress = in.readString();
    }

    public static final Parcelable.Creator<FoodContact> CREATOR = new Parcelable.Creator<FoodContact>() {
        @Override
        public FoodContact createFromParcel(Parcel source) {
            return new FoodContact(source);
        }

        @Override
        public FoodContact[] newArray(int size) {
            return new FoodContact[size];
        }
    };
}
