package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Intent;

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

    // Singleton Pattern
    private FacebookLoginHelper(Activity activity) {
        this.activity = activity;
        this.callbackManager = CallbackManager.Factory.create();
    }

    public static synchronized FacebookLoginHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new FacebookLoginHelper(activity);
        }
        return instance;
    }

    // Thiết lập đăng nhập Facebook
    public void setupFacebookLogin(LoginButton loginButton, final FacebookLoginListener listener) {
        loginButton.setPermissions("email", "public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                firebaseAuthWithFacebook(accessToken, listener);
            }

            @Override
            public void onCancel() {
                listener.onLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                listener.onLoginError(error);
            }
        });
    }

    // Xác thực Firebase với token Facebook
    private void firebaseAuthWithFacebook(AccessToken token, final FacebookLoginListener listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Lấy role người dùng và lưu trạng thái đăng nhập
                        PreferenceManager preferenceManager = new PreferenceManager(activity);
                        String role = preferenceManager.getUserRole();  // Lấy role từ shared preferences
                        preferenceManager.saveLoginState(true, "facebook", user.getUid(), null, role);

                        listener.onLoginSuccess(user);
                    } else {
                        listener.onLoginError(task.getException());
                    }
                });
    }

    // Xử lý kết quả trả về từ Facebook
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Đăng xuất từ Firebase và Facebook
    public static void logout(FacebookLogoutListener listener) {
        // Đăng xuất Firebase
        FirebaseAuth.getInstance().signOut();

        // Đăng xuất Facebook
        if (AccessToken.getCurrentAccessToken() != null) {
            com.facebook.login.LoginManager.getInstance().logOut();
        }

        // Thông báo listener sau khi đăng xuất
        listener.onLogoutSuccess();
    }

    // Interface cho các sự kiện đăng nhập thành công, hủy và lỗi
    public interface FacebookLoginListener {
        void onLoginSuccess(FirebaseUser user);

        void onLoginCancel();

        void onLoginError(Exception e);
    }

    // Interface cho sự kiện đăng xuất thành công
    public interface FacebookLogoutListener {
        void onLogoutSuccess();
    }
}
