package com.example.btlon;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.android.gms.tasks.Task;

public class GoogleSignInHelper {

    private static GoogleSignInHelper instance;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private GoogleSignInHelper() {
        mAuth = FirebaseAuth.getInstance();  // Initialize FirebaseAuth instance
    }

    // Singleton pattern to ensure a single instance
    public static GoogleSignInHelper getInstance() {
        if (instance == null) {
            instance = new GoogleSignInHelper();
        }
        return instance;
    }

    // Method to start Google Sign-In process
    public void signInWithGoogle(Activity activity, ActivityResultLauncher<Intent> googleSignInLauncher) {
        signOut(activity, () -> {
            // After signing out, proceed to sign in
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    // Sign out from Google
    public void signOut(Activity activity, Runnable onSignOutComplete) {
        if (mGoogleSignInClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        }

        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            if (onSignOutComplete != null) {
                onSignOutComplete.run(); // Call the callback after sign-out
            }
        });
    }

    public static GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("R.string.default_web_client_id")
                .requestEmail()
                .build();
    }


    // Handle the sign-in result after user authentication
    public void handleSignInResult(Task<GoogleSignInAccount> task, Activity activity) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account, activity); // Authenticate with Firebase using Google credentials
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "Google sign-in failed", e);
        }
    }

    // Authenticate the user with Firebase using Google account
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user, activity);  // Update the UI with the user's information
                    } else {
                        updateUI(null, activity);  // If authentication fails, show error
                    }
                });
    }

    // Update UI after successful or failed login
    private void updateUI(FirebaseUser user, Activity activity) {
        if (user != null) {
            // If login is successful, navigate to the home screen
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {
            // If login fails, log the error and show appropriate message
            Log.e("GoogleSignIn", "Authentication failed");
        }
    }
}
