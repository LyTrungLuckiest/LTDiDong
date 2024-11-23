package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.btlon.Ui.Login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class AuthLogoutHelper {

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    public AuthLogoutHelper(FirebaseAuth firebaseAuth, GoogleSignInClient googleSignInClient) {
        this.mAuth = firebaseAuth;
        this.googleSignInClient = googleSignInClient;
    }

    public void logout(Activity activity) {
        if (googleSignInClient != null) {
            googleSignInClient.signOut().addOnCompleteListener(activity, task -> {
                Toast.makeText(activity, "Đã đăng xuất khỏi Google!", Toast.LENGTH_SHORT).show();
            });
        }

        LoginManager.getInstance().logOut();

        mAuth.signOut();

        Toast.makeText(activity, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
