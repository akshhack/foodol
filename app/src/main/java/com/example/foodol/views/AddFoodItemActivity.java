package com.example.foodol.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodol.R;
import com.example.foodol.models.AuthenticationManager;
import com.example.foodol.models.FoodContact;
import com.example.foodol.models.FoodItem;
import com.example.foodol.models.FoodItemType;
import com.example.foodol.models.RemoteDatabaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class AddFoodItemActivity extends AppCompatActivity {

    private static final String TAG = "AFI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        //Find TextView control
        final AutoCompleteTextView acTextView = findViewById(R.id.foodItemList);
        final EditText qty = findViewById(R.id.addFoodItemQty);
        final Button submitItem = findViewById(R.id.submitItem);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(AddFoodItemActivity.this,
                                    android.R.layout.select_dialog_singlechoice,
                                    FoodItemType.getValuesAsStringList());

        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(1);
        //Set the adapter
        acTextView.setAdapter(adapter);

        cancelLoading();

        submitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validData(acTextView.getText().toString(), qty.getText().toString())) {
//                    AuthenticationManager.getInstance().getCurrentUser().getIdToken(true).
//                        addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<GetTokenResult> task) {
//                                if (task.getResult() != null) {

                                    // Get authID
                                    String authID = AuthenticationManager.getInstance().getCurrentUser().getUid();//task.getResult().getToken();

                                    RemoteDatabaseManager.getInstance().getFoodContact(authID,
                                            new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null && document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    FoodContact foodContact = document.toObject(FoodContact.class);

                                                    FoodItem newFoodItem = new FoodItem(acTextView.getText().toString(),
                                                            Integer.valueOf(qty.getText().toString()),
                                                                    foodContact);

                                                    RemoteDatabaseManager.getInstance().createFoodItem(newFoodItem,
                                                            new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "onComplete: new good item created successfully");
                                                                Toast.makeText(AddFoodItemActivity.this, "New food item added", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Log.d(TAG, "onComplete: something went wrong " + task.getException());
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
//                                }
//                            }
//                        });
                }
            }
        });

    }

    private void startLoading() {
        LinearLayout mainViewContainer = findViewById(R.id.main_view_container_2);
        LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view_2);

        lottieAnimationView.setVisibility(View.VISIBLE);
        mainViewContainer.setVisibility(View.GONE);
    }

    private void cancelLoading() {
        // by default views start with loading
        LinearLayout mainViewContainer = findViewById(R.id.main_view_container_2);
        LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view_2);

        lottieAnimationView.setVisibility(View.GONE);
        mainViewContainer.setVisibility(View.VISIBLE);
    }

    private boolean validData(String foodItem, String qty) {
        // TODO: implement better verification later
        return foodItem.length() != 0 && qty.length() != 0;
    }
}
