package com.example.btlon;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

public class LoginTabFragment extends Fragment {

    private boolean isLogin = false;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private SignInButton googleLoginButton;
    private LoginButton facebookLoginButton;
    private SqliteHelper sqliteHelper;

    private static final int RC_SIGN_IN = 9001;

    private ActivityResultCallback<ActivityResult> googleSignInResultCallback = result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
            // Handle Firebase login here
            GoogleSignInHelper.getInstance().firebaseAuthWithGoogle(account, getActivity());
        } else {
            Toast.makeText(getContext(), "Google sign-in failed", Toast.LENGTH_SHORT).show();
        }
    };

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), googleSignInResultCallback
    );

    private CallbackManager callbackManager; // Initialize Facebook callback manager

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab_fagment, container, false);

        // Initialize views
        usernameEditText = view.findViewById(R.id.login_user);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.btLogin);
        googleLoginButton = view.findViewById(R.id.sign_in_button);
        facebookLoginButton = view.findViewById(R.id.login_button_facebook);

        sqliteHelper = new SqliteHelper(getContext());

        // Initialize Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create();

        // Event for normal login
        loginButton.setOnClickListener(v -> normalLogin());

        // Event for Google Login
        googleLoginButton.setOnClickListener(v -> signInWithGoogle());

        // Event for Facebook Login
        facebookLoginButton.setOnClickListener(v -> loginWithFacebook());

        // Set up Facebook Login
        FacebookLoginHelper.getInstance().setupFacebookLogin(facebookLoginButton, new FacebookLoginHelper.FacebookLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                String loggedInUserName = user.getDisplayName();
                if (loggedInUserName != null) {
                    // Save login state in SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin", true);
                    editor.putString("USERNAME", loggedInUserName);
                    editor.apply();
                    Intent intent = user.getEmail().equals("admin") ? new Intent(getActivity(), AdminActivity.class) :
                            new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("USERNAME", loggedInUserName);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Tài khoản không hợp lệ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoginCancel() {
                Toast.makeText(getContext(), "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginError(Exception e) {
                Toast.makeText(getContext(), "Facebook login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void normalLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        String loggedInUserName = sqliteHelper.checkLogin(username, password);
        if (loggedInUserName != null) {
            // Lưu trạng thái đăng nhập vào SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogin", true);
            editor.putString("USERNAME", loggedInUserName);
            editor.apply();

            // Chuyển hướng tới màn hình phù hợp
            if ("admin".equalsIgnoreCase(username)) {
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                intent.putExtra("USERNAME", loggedInUserName);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("USERNAME", loggedInUserName);
                startActivity(intent);
            }


            // Đóng màn hình đăng nhập
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Tài khoản không hợp lệ!", Toast.LENGTH_LONG).show();
        }

    }

    private void signInWithGoogle() {
        GoogleSignInHelper.getInstance().signInWithGoogle(getActivity(), googleSignInLauncher);
    }

    private void loginWithFacebook() {
        // Trigger Facebook login with the helper
        FacebookLoginHelper.getInstance().setupFacebookLogin(facebookLoginButton, new FacebookLoginHelper.FacebookLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                // Handle Facebook login success
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLoginCancel() {
                // Handle Facebook login cancel
                Toast.makeText(getContext(), "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginError(Exception e) {
                // Handle Facebook login error
                Toast.makeText(getContext(), "Facebook login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Forward the result to the Facebook Login helper
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
