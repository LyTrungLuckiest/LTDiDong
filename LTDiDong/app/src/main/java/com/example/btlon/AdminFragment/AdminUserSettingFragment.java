package com.example.btlon.AdminFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        userAdapter.setOnUserActionListener((user, position) -> showActionDialog(user, position));

        return view;
    }

    private void showActionDialog(User user, int position) {
        // Create dialog with two options: Edit and Delete
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Action");

        // Set options
        builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                // Edit action
                showEditDialog(user, position);
            } else if (which == 1) {
                // Delete action
                deleteUser(user, position);
            }
        });

        // Show dialog
        builder.create().show();
    }

    private void showEditDialog(User user, int position) {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_user, null);

        // Initialize EditTexts
        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);

        // Pre-fill data
        edtUsername.setText(user.getUsername());
        edtPassword.setText(user.getPassword());

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit User")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newUsername = edtUsername.getText().toString().trim();
                    String newPassword = edtPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newPassword)) {
                        Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Update user in database
                        user.setUsername(newUsername);
                        user.setPassword(newPassword);
                        sqliteHelper.updateUser(user);

                        // Notify adapter and refresh RecyclerView
                        userList.set(position, user);
                        userAdapter.notifyItemChanged(position);

                        Toast.makeText(getContext(), "User updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null);

        // Show dialog
        builder.create().show();
    }

    private void deleteUser(User user, int position) {
        // Delete user from database
        sqliteHelper.deleteUser(user.getUserId());

        // Remove from list and notify adapter
        userList.remove(position);
        userAdapter.notifyItemRemoved(position);

        Toast.makeText(getContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show();
    }
}
