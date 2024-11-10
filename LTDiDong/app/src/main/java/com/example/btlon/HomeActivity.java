package com.example.btlon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FirstFragment())
                .addToBackStack(null)
                .commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment= null;
                switch (tab.getPosition()){
                    case  0:
                        fragment= new FirstFragment();
                        break;
                    case  1:
                        fragment= new SecondFragment();
                        break;
                    case  2:
                        fragment= new ThirdFragment();
                        break;
                    case  3:
                        fragment= new FourFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }




}