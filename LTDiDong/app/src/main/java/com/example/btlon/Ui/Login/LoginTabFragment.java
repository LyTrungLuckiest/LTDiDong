package com.example.btlon.Ui.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.btlon.Data.SqliteHelper;
import com.example.btlon.R;
import com.example.btlon.Ui.Home.HomeActivity;
import com.example.btlon.Utils.FacebookLoginHelper;
import com.example.btlon.Utils.GoogleSignInHelper;
import com.example.btlon.Utils.KeyboardHelper;
import com.example.btlon.Utils.NormalLoginHelper;
import com.example.btlon.Utils.PasswordToggleHelper;
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
    private ToggleButton toggle;
    private SignInButton googleLoginButton;
    private LoginButton facebookLoginButton;
    private SqliteHelper sqliteHelper;

    private static final int RC_SIGN_IN = 9001;

    private ActivityResultCallback<ActivityResult> googleSignInResultCallback = result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
            GoogleSignInHelper.getInstance().firebaseAuthWithGoogle(account, getActivity());
        } else {
            Toast.makeText(getContext(), "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show();
        }
    };

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), googleSignInResultCallback
    );

    private CallbackManager callbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);

        usernameEditText = view.findViewById(R.id.login_user);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.btLogin);
        googleLoginButton = view.findViewById(R.id.sign_in_button);
        facebookLoginButton = view.findViewById(R.id.login_button_facebook);
        toggle = view.findViewById(R.id.togglePasswordVisibility);

        new PasswordToggleHelper(passwordEditText, toggle);
        KeyboardHelper.hideKeyboardOnEnter(passwordEditText, getContext());

        sqliteHelper = new SqliteHelper(getContext());

        callbackManager = CallbackManager.Factory.create();

        loginButton.setOnClickListener(v -> {
            String user = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            NormalLoginHelper normalLoginHelper = new NormalLoginHelper(getContext());
            normalLoginHelper.normalLogin(user, password);

        });

        googleLoginButton.setOnClickListener(v -> signInWithGoogle());

        facebookLoginButton.setOnClickListener(v -> loginWithFacebook());

        FacebookLoginHelper.getInstance(getActivity()).setupFacebookLogin(facebookLoginButton, new FacebookLoginHelper.FacebookLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                // Lấy userId từ FirebaseUser
                String userId = user.getUid(); // Đây là UID của người dùng trong Firebase Authentication

                // Lưu userId vào SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", userId); // Lưu userId dưới dạng String
                editor.apply();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLoginCancel() {
                Toast.makeText(getContext(), "Đăng nhập Facebook đã bị hủy", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginError(Exception e) {
                Toast.makeText(getContext(), "Đăng nhập Facebook thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void signInWithGoogle() {
        GoogleSignInHelper.getInstance().signInWithGoogle(getActivity(), googleSignInLauncher);
    }

    private void loginWithFacebook() {
        FacebookLoginHelper.getInstance(getActivity()).setupFacebookLogin(facebookLoginButton, new FacebookLoginHelper.FacebookLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                Toast.makeText(getContext(), "Đăng nhập Facebook thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLoginCancel() {
                Toast.makeText(getContext(), "Đăng nhập Facebook đã bị hủy", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginError(Exception e) {
                Toast.makeText(getContext(), "Đăng nhập Facebook thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookLoginHelper.getInstance(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

}
