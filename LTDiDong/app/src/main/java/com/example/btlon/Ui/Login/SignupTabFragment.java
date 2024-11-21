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

import com.example.btlon.Data.SqliteHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.KeyboardHelper;
import com.example.btlon.Utils.PasswordToggleHelper;
import com.google.android.material.tabs.TabLayout;

public class SignupTabFragment extends Fragment {

    private EditText edtUsername, edtPassword, edtConfirmPassword;
    private Button btSignup;
    private SqliteHelper sqliteHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        // Khởi tạo các thành phần giao diện
        edtUsername = view.findViewById(R.id.signup_user);
        edtPassword = view.findViewById(R.id.signup_password);
        edtConfirmPassword = view.findViewById(R.id.signup_passwor_checkout);
        btSignup = view.findViewById(R.id.btnSignup);
        new PasswordToggleHelper(edtPassword,view.findViewById(R.id.togglePasswordVisibility1));
        new PasswordToggleHelper(edtConfirmPassword,view.findViewById(R.id.togglePasswordVisibilityCheckouty2));
// Lắng nghe sự kiện Enter và ẩn bàn phím

        KeyboardHelper.hideKeyboardOnEnter(edtConfirmPassword, getContext());
        // Khởi tạo cơ sở dữ liệu
        sqliteHelper = new SqliteHelper(getContext());

        // Thiết lập sự kiện cho nút Đăng ký
        btSignup.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Kiểm tra xem mật khẩu có khớp hay không
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                // Ghi dữ liệu vào cơ sở dữ liệu, kiểm tra trùng lặp tài khoản
                boolean isInserted = sqliteHelper.insertData(username, password);
                if (isInserted) {
                    Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    // Chuyển sang tab Đăng nhập
                    TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
                    if (tabLayout != null) {
                        TabLayout.Tab tab = tabLayout.getTabAt(0); // Tab 0 là tab Đăng nhập
                        if (tab != null) tab.select();
                    }
                } else {
                    Toast.makeText(getContext(), "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
