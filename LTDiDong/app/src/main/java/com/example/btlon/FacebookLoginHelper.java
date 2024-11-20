package com.example.btlon;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

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

    private static FacebookLoginHelper instance;
    private CallbackManager callbackManager;
    private Activity activity;

    // Khởi tạo FacebookLoginHelper với Activity
    private FacebookLoginHelper(Activity activity) {
        this.activity = activity;
        this.callbackManager = CallbackManager.Factory.create();
    }

    // Singleton pattern để lấy instance của FacebookLoginHelper
    public static synchronized FacebookLoginHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new FacebookLoginHelper(activity);
        }

        return instance;
    }

    // Cài đặt đăng nhập Facebook
    public void setupFacebookLogin(LoginButton loginButton, final FacebookLoginListener listener) {
        loginButton.setPermissions("email", "public_profile");

        // Đăng ký callback
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Đăng nhập Facebook thành công, xác thực với Firebase
                AccessToken accessToken = loginResult.getAccessToken();
                firebaseAuthWithFacebook(accessToken, listener);
            }

            @Override
            public void onCancel() {
                listener.onLoginCancel();  // Xử lý khi người dùng hủy đăng nhập
            }

            @Override
            public void onError(FacebookException error) {
                listener.onLoginError(error);  // Xử lý khi có lỗi xảy ra
            }
        });
    }

    // Xác thực Firebase với Facebook AccessToken
    private void firebaseAuthWithFacebook(AccessToken token, final FacebookLoginListener listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        listener.onLoginSuccess(user);  // Đăng nhập thành công
                    } else {
                        listener.onLoginError(task.getException());  // Xử lý khi đăng nhập thất bại
                    }
                });
    }

    // Chuyển kết quả vào callback manager
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Định nghĩa interface cho listener
    public interface FacebookLoginListener {
        void onLoginSuccess(FirebaseUser user);  // Khi đăng nhập thành công

        void onLoginCancel();  // Khi người dùng hủy đăng nhập

        void onLoginError(Exception e);  // Khi có lỗi xảy ra
    }
    public static void logout(FacebookLogoutListener listener) {
        FirebaseAuth.getInstance().signOut(); // Đăng xuất khỏi Firebase

        if (AccessToken.getCurrentAccessToken() != null) {
            com.facebook.login.LoginManager.getInstance().logOut(); // Đăng xuất khỏi Facebook
        }

        listener.onLogoutSuccess(); // Gọi callback khi đăng xuất thành công
    }

    // Định nghĩa interface để thông báo kết quả đăng xuất
    public interface FacebookLogoutListener {
        void onLogoutSuccess();
    }

}


