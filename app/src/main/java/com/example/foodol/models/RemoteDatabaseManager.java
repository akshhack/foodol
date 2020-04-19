package com.example.foodol.models;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

public class RemoteDatabaseManager {

    private static final String TAG = "RDM";

    private static final String FOOD_CONTACTS = "food_contacts";
    private static final String FOOD_ITEMS = "food_items";

    private static RemoteDatabaseManager remoteDatabaseManager = new RemoteDatabaseManager();
    private FirebaseFirestore mDatabase;

    private RemoteDatabaseManager() {
        // singleton class.
        this.mDatabase = FirebaseFirestore.getInstance();

        // set local caching when network is down
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        this.mDatabase.setFirestoreSettings(settings);
    }

    public static RemoteDatabaseManager getInstance() {
        return remoteDatabaseManager;
    }

    public void createFoodContact(String userAuthID, FoodContact foodContact,
                                  OnCompleteListener<Void> onCompleteListener) {
        Log.d(TAG, "createFoodContact: " + userAuthID);
        // Add document with specific authID
        this.mDatabase.collection(FOOD_CONTACTS).
                document(userAuthID).
                set(foodContact).
                addOnCompleteListener(onCompleteListener);
    }

    public void getFoodContact(String userAuthID,
                               OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        // Get document with specific authID
        this.mDatabase.collection(FOOD_CONTACTS).
                document(userAuthID).
                get().
                addOnCompleteListener(onCompleteListener);
    }

    public void observeAllFoodItems(EventListener<QuerySnapshot> onEventListener) {
        CollectionReference foodItems = this.mDatabase.collection(FOOD_ITEMS);
        foodItems.addSnapshotListener(onEventListener);
    }

    public void createFoodItem(FoodItem foodItem,
                               OnCompleteListener<DocumentReference> onCompleteListener) {
        this.mDatabase.collection(FOOD_ITEMS).
                add(foodItem).
                addOnCompleteListener(onCompleteListener);
    }

    public boolean isUsernameValid() {
        return true;
    }
}
