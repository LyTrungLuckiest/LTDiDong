package com.example.btlon;

import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FacebookLoginHelper {

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    // Singleton instance
    private static FacebookLoginHelper instance;

    // Private constructor to prevent direct instantiation
    private FacebookLoginHelper() {
        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        callbackManager = CallbackManager.Factory.create(); // Initialize CallbackManager
    }

    // Singleton getInstance() method
    public static FacebookLoginHelper getInstance() {
        if (instance == null) {
            instance = new FacebookLoginHelper();
        }
        return instance;
    }

    // Setup Facebook Login
    public void setupFacebookLogin(LoginButton loginButton, final FacebookLoginListener listener) {
        loginButton.setReadPermissions("email", "public_profile"); // Set required permissions

        // Register the callback for Facebook login
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken(); // Get Facebook token
                handleFacebookAccessToken(token, listener); // Handle token and log in to Firebase
            }

            @Override
            public void onCancel() {
                Log.d("FacebookLogin", "onCancel");
                listener.onLoginCancel(); // Notify on cancel
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FacebookLogin", "onError: " + error.getMessage());
                listener.onLoginError(error); // Notify on error
            }
        });
    }

    // Handle Facebook token and sign in to Firebase
    private void handleFacebookAccessToken(AccessToken token, final FacebookLoginListener listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken()); // Create Firebase credential

        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser(); // Get Firebase user
                listener.onLoginSuccess(user); // Notify on success
            } else {
                Log.w("FacebookLogin", "signInWithCredential:failure", task.getException());
                listener.onLoginError(task.getException()); // Notify on failure
            }
        });
    }

    // Handle the result from Facebook login activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data); // Forward result to CallbackManager
    }

    // Interface for Facebook login results
    public interface FacebookLoginListener {
        void onLoginSuccess(FirebaseUser user); // On success
        void onLoginCancel(); // On cancel
        void onLoginError(Exception e); // On error
    }
}
