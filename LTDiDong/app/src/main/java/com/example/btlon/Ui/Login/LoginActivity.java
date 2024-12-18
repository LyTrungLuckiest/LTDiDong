package com.example.btlon.Ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;


import com.example.btlon.Adapter.ViewPagerAdapter;
import com.example.btlon.R;
import com.example.btlon.Ui.Home.HomeActivity;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter adapter;
    private Button btnQuaylai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        btnQuaylai = findViewById(R.id.btnQuaylai);
        btnQuaylai.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);


        });


        tabLayout.addTab(tabLayout.newTab().setText("Đăng nhập"));
        tabLayout.addTab(tabLayout.newTab().setText("Đăng ký"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager2.setCurrentItem(position);

                // Lấy fragment hiện tại và reset các trường nhập liệu
                if (position == 0) { // Đăng nhập
                    LoginTabFragment loginFragment = (LoginTabFragment) adapter.getFragmentAt(0);
                    if (loginFragment != null) {
                        loginFragment.ResetForm();
                    }
                } else if (position == 1) { // Đăng ký
                    SignupTabFragment signupFragment = (SignupTabFragment) adapter.getFragmentAt(1);
                    if (signupFragment != null) {
                        signupFragment.ResetForm();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        ImageView backgroundImage = findViewById(R.id.background_image);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(backgroundImage);
    }
}
