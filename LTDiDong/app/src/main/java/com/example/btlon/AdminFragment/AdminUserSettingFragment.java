package com.example.btlon.AdminFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.btlon.R;
import com.example.btlon.SqliteHelper;
import com.example.btlon.User;
import com.example.btlon.UserAdapter;

import java.util.ArrayList;

public class AdminUserSettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> userList;
    private SqliteHelper sqliteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_user_setting, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize SQLiteHelper and get user data
        sqliteHelper = new SqliteHelper(getContext());
        userList = sqliteHelper.getAllUsers();

        // Initialize Adapter
        userAdapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(userAdapter);

        // Set OnClickListener for the Back Button
        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);
        btBackToSettings.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(); // Navigate back
        });

        // Handle user action button click
        userAdapter.setOnUserActionListener((user, position) -> {
            // Perform action with the clicked user's data
            Toast.makeText(getContext(), "Clicked user: " + user.getUsername(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
