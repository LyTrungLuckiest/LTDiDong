package com.example.btlon.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.btlon.Ui.Login.LoginTabFragment;
import com.example.btlon.Ui.Login.SignupTabFragment;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new LoginTabFragment();
        } else {
            fragment = new SignupTabFragment();
        }
        fragmentMap.put(position, fragment); // Lưu fragment vào Map
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }

    public Fragment getFragmentAt(int position) {
        return fragmentMap.get(position);
    }
}
