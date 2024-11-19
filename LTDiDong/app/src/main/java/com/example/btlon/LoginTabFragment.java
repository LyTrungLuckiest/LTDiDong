package com.example.btlon;

import android.app.Activity;
import android.content.Intent;
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
                // Handle login success for Facebook
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLoginCancel() {
                // Handle login cancel for Facebook
                Toast.makeText(getContext(), "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginError(Exception e) {
                // Handle login error for Facebook
                Toast.makeText(getContext(), "Facebook login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void normalLogin() {
        String user = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            String loggedInUserName = sqliteHelper.checkLogin(user, password);
            if (loggedInUserName != null) {
                Intent intent = user.equals("admin") ? new Intent(getActivity(), AdminActivity.class)
                        : new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("USERNAME", loggedInUserName);
                startActivity(intent);
                getActivity().finish(); // Close login screen
            } else {
                Toast.makeText(getContext(), "Tài khoản không hợp lệ", Toast.LENGTH_LONG).show();
            }
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
