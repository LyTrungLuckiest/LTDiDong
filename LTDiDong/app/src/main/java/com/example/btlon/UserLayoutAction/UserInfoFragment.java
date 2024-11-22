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
import android.widget.Toast;

import com.example.btlon.R;

public class UserInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
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
            // In thông tin chi tiết về lỗi khi có ngoại lệ
            e.printStackTrace();
            // Có thể thêm thông báo vào logcat để dễ theo dõi hơn
            Log.e("UserInfoFragment", "Error setting up back button: " + e.getMessage());
        }
    }

}
