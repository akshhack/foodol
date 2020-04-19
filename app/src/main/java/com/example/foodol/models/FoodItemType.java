package com.example.foodol.models;

import java.util.ArrayList;
import java.util.List;

// Will be used as key in firestore
public enum FoodItemType {
    MILK("milk"),
    EGGS("eggs"),
    BREAD("bread");

    String type;

    FoodItemType(String type) {
        this.type = type;
    }

    String getFoodItemType() {
        return this.type;
    }

    public static FoodItemType fromString(String foodItemTypeAsString) {
        for (FoodItemType b : FoodItemType.values()) {
            if (b.type.equalsIgnoreCase(foodItemTypeAsString)) {
                return b;
            }
        }
        return null;
    }

    public static List<String> getValuesAsStringList() {
        List<String> strings = new ArrayList<>();
        for (FoodItemType foodItemType : FoodItemType.values()) {
            strings.add(foodItemType.getFoodItemType());
        }
        return strings;
    }
}
