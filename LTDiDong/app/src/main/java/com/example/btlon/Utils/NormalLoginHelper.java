package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.btlon.Data.SqliteHelper;
import com.example.btlon.Ui.Admin.AdminActivity;
import com.example.btlon.Ui.Home.HomeActivity;

public class NormalLoginHelper {

    private SqliteHelper sqliteHelper;
    private Context context;

    public NormalLoginHelper(Context context) {
        this.context = context;
        this.sqliteHelper = new SqliteHelper(context);
    }

    // Kiểm tra đăng nhập
    public void normalLogin(String user, String password) {
        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            String loggedInUserName = sqliteHelper.checkLogin(user, password);
            if (loggedInUserName != null) {
                Intent intent = user.equals("admin") ? new Intent(context, AdminActivity.class)
                        : new Intent(context, HomeActivity.class);
                intent.putExtra("USERNAME", loggedInUserName);
                context.startActivity(intent);
                if (context instanceof Activity) {
                    ((Activity) context).finish(); // Đóng màn hình đăng nhập
                }
            } else {
                Toast.makeText(context, "Tài khoản không hợp lệ", Toast.LENGTH_LONG).show();
            }
        }
    }
}
