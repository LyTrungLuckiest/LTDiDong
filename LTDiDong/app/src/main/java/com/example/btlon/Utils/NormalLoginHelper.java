package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.btlon.Data.UserTableHelper;
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
        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            try {
                boolean isValidUser = userTableHelper.checkLogin(user, password);

                if (isValidUser) {

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

                    PreferenceManager preferenceManager = new PreferenceManager(context);
                    preferenceManager.saveLoginState(true, "custom", String.valueOf(userId), null
                    );
                    Log.d("Login", "User login state saved: " + preferenceManager.isLoggedIn());


                    Intent intent = user.equals("admin") ? new Intent(context, AdminActivity.class)
                            : new Intent(context, HomeActivity.class);
                    intent.putExtra("USERNAME", user);
                    context.startActivity(intent);

                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else {
                    Toast.makeText(context, "Tài khoản không hợp lệ", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NormalLoginHelper", "Error during login: " + e.getMessage());
            }
        }
    }
}
