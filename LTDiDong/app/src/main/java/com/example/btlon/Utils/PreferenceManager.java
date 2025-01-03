package com.example.btlon.Utils;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.btlon.Ui.Home.HomeActivity;
import com.example.btlon.Ui.Login.LoginActivity;

public class PreferenceManager {
    private static android.content.SharedPreferences sharedPref;
    private android.content.SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPref = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    // Lưu trạng thái đăng nhập
    public void saveLoginState(boolean isLoggedIn, String loginMethod, String userId, String token, String role) {
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn);
        editor.putString("LOGIN_METHOD", loginMethod); // "google", "facebook", "custom"
        editor.putString("USER_ID", userId); // ID từ backend, Google hoặc Facebook
        editor.putString("ACCESS_TOKEN", token); // Token truy cập (nếu có)
        editor.putString("ROLE", role); // "user" hoặc "admin")
        editor.apply();
    }

    // Lấy trạng thái đăng nhập
    public static boolean isLoggedIn() {
        return sharedPref.getBoolean("IS_LOGGED_IN", false);
    }

    public String getLoginMethod() {
        return sharedPref.getString("LOGIN_METHOD", "");
    }

    public static String getUserId() {
        String userId = sharedPref.getString("USER_ID", "");
        Log.d("PreferenceManager", "Lấy userId: " + userId); // Thêm log để kiểm tra
        return userId;
    }


    public String getAccessToken() {
        return sharedPref.getString("ACCESS_TOKEN", "");
    }
    public String getUserRole() {
        return sharedPref.getString("ROLE", "");
    }


    public void clearLoginState() {
        editor.clear();
        editor.apply();
    }


    public void checkLoginStatus(Context context) {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        if (preferenceManager.isLoggedIn()) {
            Toast.makeText(context, "Bạn đã đăng nhập!", Toast.LENGTH_SHORT).show();
            // Nếu đã đăng nhập, điều hướng đến màn hình Home
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        } else {
            // Nếu chưa đăng nhập, hiển thị thông báo và điều hướng đến màn hình Login
            Toast.makeText(context, "Vui lòng đăng nhập để tiếp tục!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
