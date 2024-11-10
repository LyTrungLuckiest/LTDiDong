package com.example.btlon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private SqliteHelper sqliteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab_fagment, container, false);

        // Initialize views
        usernameEditText = view.findViewById(R.id.login_user);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.btLogin);

        sqliteHelper = new SqliteHelper(getContext());

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
                    // Thông báo lỗi nếu tài khoản không tồn tại
                    Toast.makeText(getContext(), "Tài khoản không hợp lệ ", Toast.LENGTH_LONG).show();

                    // Tùy chọn: Tự động chuyển sang tab Đăng ký
                    /*TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
                      if (tabLayout != null) {
                        TabLayout.Tab tab = tabLayout.getTabAt(1); // Tab 1 là tab Đăng ký
                        if (tab != null) tab.select();
                    }*/
                }
            }
        });


        return view;
    }
}
