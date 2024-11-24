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
import android.widget.Button;

import com.example.btlon.R;


public class UserPointFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_point, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Button btUserAddressBack = view.findViewById(R.id.btUserPointBack);
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