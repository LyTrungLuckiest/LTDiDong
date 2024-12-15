package com.example.btlon.UserLayoutAction;

import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btlon.Data.UserTableHelper;
import com.example.btlon.Data.Users;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;



public class UserInfoFragment extends Fragment {

    Button btSave;
    EditText edtName, edtPhoneNumber;
    Users users;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_user_info, container, false);

            edtName = view.findViewById(R.id.edtName);
            edtPhoneNumber = view.findViewById(R.id.edtPhonenNumer);
            btSave = view.findViewById(R.id.btSaveInfo);
            PreferenceManager preferenceManager = new PreferenceManager(requireContext());
            int userId = Integer.parseInt(preferenceManager.getUserId());
            UserTableHelper userTableHelper = new UserTableHelper(requireContext());
            users = userTableHelper.getUserNamePhoneById(userId);


            // Initial setup: Load user data when the fragment is created or resumed
            loadUserData(users);

            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get user input
                    String name = edtName.getText().toString();
                    String phone = edtPhoneNumber.getText().toString();

                    // Ensure both fields are not empty
                    if (!name.isEmpty() && !phone.isEmpty()) {
                        PreferenceManager preferenceManager = new PreferenceManager(requireContext());
                        int userId = Integer.parseInt(preferenceManager.getUserId());
                        UserTableHelper userTableHelper = new UserTableHelper(requireContext());

                        // Update the user data in the database
                        boolean isUpdated = userTableHelper.updateUserName(userId, name, phone);

                        // Check if the update was successful
                        if (isUpdated) {
                            Toast.makeText(requireContext(), "Thông tin đã được cập nhật!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(requireContext(), "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return view;
        }

        private void loadUserData(Users user) {


            if (user != null) {
                // Set user data to EditText fields
                if (user.getName() != null && !user.getName().isEmpty()) {
                    edtName.setText(user.getName());
                }
                if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                    edtPhoneNumber.setText(user.getPhone());
                }
            }
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            try {
                Button btUserAddressBack = view.findViewById(R.id.btUserInfoBack);
                if (btUserAddressBack != null) {
                    NavController navController = Navigation.findNavController(view);
                    btUserAddressBack.setOnClickListener(v -> navController.popBackStack());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("UserInfoFragment", "Lỗi khi thiết lập nút quay lại: " + e.getMessage());
            }
        }



}