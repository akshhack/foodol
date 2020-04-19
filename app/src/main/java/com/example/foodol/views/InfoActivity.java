package com.example.foodol.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodol.R;
import com.example.foodol.models.AuthenticationManager;
import com.example.foodol.models.FoodContact;
import com.example.foodol.models.RemoteDatabaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;


public class InfoActivity extends AppCompatActivity {

    private static final String TAG = "INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final EditText name = findViewById(R.id.setName);
        final EditText phone = findViewById(R.id.setPhone);
        final EditText postalAddress = findViewById(R.id.setPostalAddress);

        Button submitInfo = findViewById(R.id.submitInfo);

        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validData(name.getText().toString(), phone.getText().toString(),
                        postalAddress.getText().toString())) {


//                    AuthenticationManager.getInstance().getCurrentUser().getIdToken(true).
//                            addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<GetTokenResult> task) {
//                                    if (task.getResult() != null) {

                                        // Get authID
                                        String authID = AuthenticationManager.getInstance().getCurrentUser().getUid(); //task.getResult().getToken();

                                        // Create new foodContact
                                        FoodContact newFoodContact = new FoodContact(name.getText().toString(),
                                                phone.getText().toString(),
                                                postalAddress.getText().toString());

                                        RemoteDatabaseManager.getInstance()
                                                .createFoodContact(authID, newFoodContact, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d(TAG, "onComplete: profile complete");
                                                        Toast.makeText(InfoActivity.this, "Profile complete",
                                                                Toast.LENGTH_SHORT).show();

                                                        // Start list activity
                                                        Intent intent = new Intent(InfoActivity.this,
                                                                FoodListActivity.class);
                                                        startActivity(intent);
                                                    }
                                                });
//                                    }
//                                }
//                            });

                }



            }
        });
    }

    private boolean validData(String name, String phone, String postalAddress) {
        // TODO: implement better verification later
        return name.length() != 0 && phone.length() != 0 && postalAddress.length() != 0;
    }
}
