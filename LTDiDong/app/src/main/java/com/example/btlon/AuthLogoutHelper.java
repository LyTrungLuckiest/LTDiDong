package com.example.btlon;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class AuthLogoutHelper {

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    // Constructor để nhận FirebaseAuth và GoogleSignInClient
    public AuthLogoutHelper(FirebaseAuth firebaseAuth, GoogleSignInClient googleSignInClient) {
        this.mAuth = firebaseAuth;
        this.googleSignInClient = googleSignInClient;
    }

    // Phương thức đăng xuất
    public void logout(Activity activity) {
        // Đăng xuất Google
        if (googleSignInClient != null) {
            googleSignInClient.signOut().addOnCompleteListener(activity, task -> {
                Toast.makeText(activity, "Đã đăng xuất khỏi Google!", Toast.LENGTH_SHORT).show();
            });
        }

        // Đăng xuất Facebook
        LoginManager.getInstance().logOut();

        // Đăng xuất Firebase
        mAuth.signOut();

        // Hiển thị thông báo chung
        Toast.makeText(activity, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển người dùng về màn hình đăng nhập
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
