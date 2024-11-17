package com.example.btlon.AdminFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.btlon.R;


public class AdminUserSettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_user_setting, container, false);

        // Find the button and set an OnClickListener
        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);  // Ensure the button ID is correct

        btBackToSettings.setOnClickListener(v -> {
            // Find NavController and pop the back stack to go back to the previous fragment
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(R.id.settingsFragment, false);  // Pop back to SettingsFragment
        });

        return view;
    }
}