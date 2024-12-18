package com.example.btlon.Ui.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btlon.Model.UserTableHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.KeyboardHelper;
import com.example.btlon.Utils.PasswordToggleHelper;
import com.google.android.material.tabs.TabLayout;

public class SignupTabFragment extends Fragment {

    private EditText edtUsername, edtPassword, edtConfirmPassword,edtName;
    private Button btSignup;
    private UserTableHelper userTableHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_signup_tab_fragment, container, false);


        edtUsername = view.findViewById(R.id.signup_user);
        edtPassword = view.findViewById(R.id.signup_password);
        edtConfirmPassword = view.findViewById(R.id.signup_passwor_checkout);
        btSignup = view.findViewById(R.id.btnSignup);
        edtName= view.findViewById(R.id.signup_name);

        new PasswordToggleHelper(edtPassword, view.findViewById(R.id.togglePasswordVisibility1));
        new PasswordToggleHelper(edtConfirmPassword, view.findViewById(R.id.togglePasswordVisibilityCheckouty2));

        KeyboardHelper.hideKeyboardOnEnter(edtConfirmPassword, getContext());

        userTableHelper = new UserTableHelper(getContext());

        btSignup.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            String name = edtName.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                if (userTableHelper.checkUsernameExists(username)) {
                    Toast.makeText(getContext(), "Tên người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    boolean isInserted = userTableHelper.addUser(username, password);
                    if (isInserted) {
                        Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    ResetForm();
                        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
                        if (tabLayout != null) {
                            TabLayout.Tab tab = tabLayout.getTabAt(0);
                            if (tab != null) tab.select();
                        }
                    } else {
                        Toast.makeText(getContext(), "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    public void ResetForm() {
        edtUsername.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
    }
}
