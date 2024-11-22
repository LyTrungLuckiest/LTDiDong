package com.example.btlon.Ui.Admin;

import android.app.AlertDialog;
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
import com.example.btlon.Data.UserTableHelper;
import com.example.btlon.R;
import com.example.btlon.Data.Users;
import com.example.btlon.Adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminUserSettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> usersList;
    private UserTableHelper userTableHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_user_setting, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize UserTableHelper and fetch user data
        userTableHelper = new UserTableHelper(getContext());
        usersList = userTableHelper.getAllUsers();

        // Initialize Adapter
        userAdapter = new UserAdapter(getContext(), (ArrayList<Users>) usersList);
        recyclerView.setAdapter(userAdapter);

        // Set OnClickListener for the Back Button
        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);
        btBackToSettings.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(); // Navigate back
        });

        // Handle user action button click
        userAdapter.setOnUserActionListener((users, position) -> showActionDialog(users, position));

        return view;
    }

    private void showActionDialog(Users users, int position) {
        // Create dialog with two options: Edit and Delete
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Action");

        // Set options
        builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                // Edit action
                showEditDialog(users, position);
            } else if (which == 1) {
                // Delete action
                deleteUser(users, position);
            }
        });

        // Show dialog
        builder.create().show();
    }

    private void showEditDialog(Users users, int position) {
        // Inflate the custom layout for editing
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_user, null);

        // Initialize EditTexts
        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);

        // Pre-fill data
        edtUsername.setText(users.getUsername());
        edtPassword.setText(users.getPassword());

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
                        users.setUsername(newUsername);
                        users.setPassword(newPassword);
                        boolean isUpdated = userTableHelper.updateUser(users.getUserId(), newUsername, newPassword, users.getRole());

                        // Notify adapter and refresh RecyclerView
                        if (isUpdated) {
                            usersList.set(position, users);
                            userAdapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "User updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to update user!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        // Show dialog
        builder.create().show();
    }

    private void deleteUser(Users users, int position) {
        // Delete user from database
        boolean isDeleted = userTableHelper.deleteUser(users.getUserId());

        if (isDeleted) {
            // Remove from list and notify adapter
            usersList.remove(position);
            userAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to delete user!", Toast.LENGTH_SHORT).show();
        }
    }
}
