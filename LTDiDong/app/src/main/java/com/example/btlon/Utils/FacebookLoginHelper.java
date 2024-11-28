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

    private void firebaseAuthWithFacebook(AccessToken token, final FacebookLoginListener listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        PreferenceManager preferenceManager = new PreferenceManager(activity);
                        String role = preferenceManager.getUserRole();
                        preferenceManager.saveLoginState(true, "facebook", user.getUid(), null, role);

                        listener.onLoginSuccess(user);
                    } else {
                        listener.onLoginError(task.getException());
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface FacebookLoginListener {
        void onLoginSuccess(FirebaseUser user);

        void onLoginCancel();

        void onLoginError(Exception e);
    }

    public static void logout(FacebookLogoutListener listener) {
        FirebaseAuth.getInstance().signOut();

        if (AccessToken.getCurrentAccessToken() != null) {
            com.facebook.login.LoginManager.getInstance().logOut();
        }

        listener.onLogoutSuccess();
    }

    public interface FacebookLogoutListener {
        void onLogoutSuccess();
    }
}
