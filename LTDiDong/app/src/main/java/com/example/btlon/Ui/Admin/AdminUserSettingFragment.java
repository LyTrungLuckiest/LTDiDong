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
        View view = inflater.inflate(R.layout.admin_user_setting_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userTableHelper = new UserTableHelper(getContext());
        usersList = userTableHelper.getAllUsers();

        userAdapter = new UserAdapter(getContext(), (ArrayList<Users>) usersList);
        recyclerView.setAdapter(userAdapter);

        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);
        btBackToSettings.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        userAdapter.setOnUserActionListener((users, position) -> showActionDialog(users, position));

        return view;
    }

    private void showActionDialog(Users users, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn hành động");

        builder.setItems(new CharSequence[]{"Chỉnh sửa", "Xóa"}, (dialog, which) -> {
            if (which == 0) {
                showEditDialog(users, position);
            } else if (which == 1) {
                deleteUser(users, position);
            }
        });

        builder.create().show();
    }

    private void showEditDialog(Users users, int position) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.user_edit_dialog, null);

        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);

        edtUsername.setText(users.getUsername());
        edtPassword.setText(users.getPassword());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa người dùng")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newUsername = edtUsername.getText().toString().trim();
                    String newPassword = edtPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newPassword)) {
                        Toast.makeText(getContext(), "Các trường không thể để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        users.setUsername(newUsername);
                        users.setPassword(newPassword);
                        boolean isUpdated = userTableHelper.updateUser(users.getUserId(), newUsername, newPassword, users.getRole());

                        if (isUpdated) {
                            usersList.set(position, users);
                            userAdapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "Cập nhật người dùng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật người dùng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void deleteUser(Users users, int position) {
        boolean isDeleted = userTableHelper.deleteUser(users.getUserId());

        if (isDeleted) {
            usersList.remove(position);
            userAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Xóa người dùng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Xóa người dùng thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
