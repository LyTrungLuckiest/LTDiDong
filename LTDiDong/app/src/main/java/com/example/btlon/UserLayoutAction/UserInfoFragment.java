package com.example.btlon.UserLayoutAction;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.btlon.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UserInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);


        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Button btUserAddressBack = view.findViewById(R.id.btUserInfoBack);
            if (btUserAddressBack != null){
                NavController navController = Navigation.findNavController(view);
                btUserAddressBack.setOnClickListener(v -> navController.popBackStack());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("UserInfoFragment", "Lỗi khi thiết lập nút quay lại: " + e.getMessage());
        }
    }
}
