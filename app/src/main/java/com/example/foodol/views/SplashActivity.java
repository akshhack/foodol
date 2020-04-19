package com.example.foodol.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodol.R;
import com.example.foodol.models.AuthenticationManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (AuthenticationManager.getInstance().getCurrentUser() != null) {
            // user already signed in
            Intent intent = new Intent(this, FoodListActivity.class);
            startActivity(intent);
        } else {
            // redirect to log in screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
