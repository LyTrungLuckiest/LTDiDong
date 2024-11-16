package com.example.btlon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.google.android.gms.common.SignInButton;

public class LoginTabFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private SqliteHelper sqliteHelper;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab_fagment, container, false);

        // Initialize views
        usernameEditText = view.findViewById(R.id.login_user);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.btLogin);
        SignInButton signInButton = view.findViewById(R.id.sign_in_button); // Button Google Sign-In

        sqliteHelper = new SqliteHelper(getContext());
        mAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Web Client ID trong strings.xml
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Đăng ký sự kiện cho nút Google Sign-In
        signInButton.setOnClickListener(v -> signInWithGoogle());

        // Đăng ký sự kiện cho nút đăng nhập bình thường
        loginButton.setOnClickListener(v -> {
            String user = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                // Kiểm tra tài khoản trong cơ sở dữ liệu
                String loggedInUserName = sqliteHelper.checkLogin(user, password);

                if (loggedInUserName != null) {
                    // Đăng nhập thành công, chuyển sang màn hình chính
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("USERNAME", loggedInUserName);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    // Thông báo lỗi nếu tài khoản không hợp lệ
                    Toast.makeText(getContext(), "Tài khoản không hợp lệ ", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    // Hàm thực hiện đăng nhập Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("Login", "Google sign-in failed", e);
                Toast.makeText(getContext(), "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("Login", "Attempting Firebase Auth with Google...");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("Login", "Firebase Auth Successful: " + user.getEmail());
                        updateUI(user);
                    } else {
                        Log.w("Login", "Firebase Auth Failed", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d("Login", "User is authenticated: " + user.getDisplayName());
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();  // Đảm bảo rằng activity hiện tại sẽ bị đóng.
        } else {
            Log.d("Login", "Authentication failed");
            Toast.makeText(getContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
