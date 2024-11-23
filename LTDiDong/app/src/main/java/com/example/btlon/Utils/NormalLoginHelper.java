package com.example.btlon.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
            boolean isValidUser = userTableHelper.checkLogin(user, password);

            if (isValidUser) {
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
        }
    }
}
