package com.example.btlon;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        // Thiết lập NavController cho BottomNavigationView
        BottomNavigationView bottomNavigationView =(BottomNavigationView) findViewById(R.id.bottomNavigation);
        NavController navController =(NavController) Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Đặt listener để ẩn BottomNavigationView khi điều hướng đến một fragment nào đó
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Kiểm tra fragment hiện tại và ẩn BottomNavigationView nếu cần
            if (destination.getId() == R.id.adminUserSettingFragment) { // Thay `someFragment` bằng ID của fragment bạn muốn ẩn navBot
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }
}
