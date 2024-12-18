package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.btlon.Model.UserTableHelper;
import com.example.btlon.Ui.Admin.AdminActivity;
import com.example.btlon.Ui.Home.HomeActivity;

public class NormalLoginHelper {

    private Context context;
    private UserTableHelper userTableHelper;

    public NormalLoginHelper(Context context) {
        this.context = context;
        this.userTableHelper = new UserTableHelper(context);
    }

    public void normalLogin(String user, String password) {
        // Kiểm tra đầu vào
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Kiểm tra thông tin người dùng
            boolean isValidUser = userTableHelper.checkLogin(user, password);

            if (isValidUser) {
                // Lấy userId từ username
                int userId = userTableHelper.getUserIdByUsername(user);

                if (userId == -1) {
                    Toast.makeText(context, "Không thể tìm thấy thông tin tài khoản", Toast.LENGTH_LONG).show();
                    return;
                }

                // Lưu userId vào SharedPreferences
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userId", userId);  // Lưu userId của tài khoản đang đăng nhập
                editor.apply();

                // Lưu trạng thái đăng nhập vào PreferenceManager
                PreferenceManager preferenceManager = new PreferenceManager(context);
                if (userTableHelper.checkRole(String.valueOf(userId)).equals("Admin")) {
                    preferenceManager.saveLoginState(true, "custom", String.valueOf(userId), null, "Admin");
                } else if (userTableHelper.checkRole(String.valueOf(userId)).equals("User")) {
                    preferenceManager.saveLoginState(true, "custom", String.valueOf(userId), null, "User");
                } else {
                    preferenceManager.saveLoginState(true, "custom", String.valueOf(userId), null, "Staff");
                }

                // Đăng nhập thành công, chuyển hướng đến màn hình phù hợp
                Log.d("Login", "User login state saved: " + preferenceManager.isLoggedIn());
                Intent intent;
                if (userTableHelper.checkRole(String.valueOf(userId)).equals("Admin") || userTableHelper.checkRole(String.valueOf(userId)).equals("Staff")) {
                    intent = new Intent(context, AdminActivity.class);
                } else{
                    intent = new Intent(context, HomeActivity.class);
                }


                intent.putExtra("USERNAME", user);
                context.startActivity(intent);

                // Kết thúc hoạt động hiện tại nếu là Activity
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            } else {
                // Thông báo khi tài khoản không hợp lệ
                Toast.makeText(context, "Tài khoản không hợp lệ", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // Xử lý lỗi khi đăng nhập
            Toast.makeText(context, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("NormalLoginHelper", "Error during login: " + e.getMessage());
        }
    }
}
