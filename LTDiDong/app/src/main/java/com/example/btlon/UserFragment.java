package com.example.btlon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private Button loginLogoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Lấy nút từ layout
        loginLogoutButton = view.findViewById(R.id.bt_login_logout);

        // Kiểm tra trạng thái đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);

        // Cập nhật văn bản nút dựa trên trạng thái đăng nhập
        if (isLogin) {
            loginLogoutButton.setText("Đăng xuất");
        } else {
            loginLogoutButton.setText("Đăng nhập");
        }

        // Đăng ký sự kiện click cho nút
        loginLogoutButton.setOnClickListener(v -> {
            if (isLogin) {
                // Nếu đã đăng nhập, thực hiện đăng xuất
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLogin", false);  // Đánh dấu là đã đăng xuất
                editor.apply();

                // Cập nhật lại văn bản nút
                loginLogoutButton.setText("Đăng nhập");

                // Thực hiện hành động đăng xuất (có thể chuyển hướng về màn hình đăng nhập)
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                // Nếu chưa đăng nhập, chuyển sang màn hình đăng nhập
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
