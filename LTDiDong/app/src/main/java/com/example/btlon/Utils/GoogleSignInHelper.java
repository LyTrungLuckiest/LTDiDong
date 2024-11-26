package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.example.btlon.R;
import com.example.btlon.Ui.Home.HomeActivity;
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
        mAuth = FirebaseAuth.getInstance();
    }

    public static GoogleSignInHelper getInstance() {
        if (instance == null) {
            instance = new GoogleSignInHelper();
        }
        return instance;
    }

    public void signInWithGoogle(Activity activity, ActivityResultLauncher<Intent> googleSignInLauncher) {
        signOut(activity, () -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

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
                onSignOutComplete.run();
            }
        });
    }

    public static GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("R.string.default_web_client_id")
                .requestEmail()
                .build();
    }

    public void handleSignInResult(Task<GoogleSignInAccount> task, Activity activity) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account, activity);
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "Google sign-in failed", e);
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user, activity);
                    } else {
                        updateUI(null, activity);
                    }
                });
    }

    private void updateUI(FirebaseUser user, Activity activity) {
        if (user != null) {

            PreferenceManager preferenceManager = new PreferenceManager(activity);
            preferenceManager.saveLoginState(true, "google", user.getUid(), null);


            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {
            Log.e("GoogleSignIn", "Authentication failed");
            Toast.makeText(activity, "Lỗi xác thực người dùng.", Toast.LENGTH_SHORT).show();
        }
    }
}
