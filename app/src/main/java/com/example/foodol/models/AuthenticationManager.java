package com.example.foodol.models;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationManager {

    private FirebaseAuth mAuth;

    private AuthenticationManager() {
        // singleton class
        this.mAuth = FirebaseAuth.getInstance();
    }

    private static AuthenticationManager authenticationManager = new AuthenticationManager();

    public static AuthenticationManager getInstance() {
        return authenticationManager;
    }

    public FirebaseUser getCurrentUser() {
        return  mAuth.getCurrentUser();
    }

    public void signInUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public void signUpUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }
}
