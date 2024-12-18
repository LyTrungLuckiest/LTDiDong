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
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.btlon.Model.UserTableHelper;
import com.example.btlon.R;
import com.example.btlon.Model.Users;
import com.example.btlon.Adapter.UserAdapter;
import com.example.btlon.Utils.PasswordToggleHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminUserSettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> usersList;
    private UserTableHelper userTableHelper;
    private Button btnThem;
    private EditText edtPassword;
    private ToggleButton toggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_user_setting_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnThem = view.findViewById(R.id.btnThem);



        userTableHelper = new UserTableHelper(getContext());
        usersList = userTableHelper.getAllUsers();

        userAdapter = new UserAdapter(getContext(), (ArrayList<Users>) usersList);
        recyclerView.setAdapter(userAdapter);
        btnThem.setOnClickListener(v -> {

        });

        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);
        btBackToSettings.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });
        btnThem.setOnClickListener(v -> showAddUserDialog());

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
    private void showAddUserDialog() {
        // Tạo View cho dialog từ layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.user_edit_dialog, null);

        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        RadioButton rdAdmin = dialogView.findViewById(R.id.rdAdmin);
        RadioButton rdUser = dialogView.findViewById(R.id.rdUser);
        RadioButton rdStaff = dialogView.findViewById(R.id.rdStaff);
        rdUser.setChecked(true);
        toggle = dialogView.findViewById(R.id.btnXem);
        new PasswordToggleHelper(edtPassword, toggle);


        // Xây dựng dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm người dùng mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    // Lấy thông tin từ các trường nhập liệu
                    String newUsername = edtUsername.getText().toString().trim();
                    String newPassword = edtPassword.getText().toString().trim();
                    String newRole = rdAdmin.isChecked() ? "Admin" : rdStaff.isChecked() ? "Staff" : "User";


                    // Kiểm tra hợp lệ dữ liệu
                    if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newPassword)) {
                        Toast.makeText(getContext(), "Các trường không thể để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userTableHelper.checkUsernameExists(newUsername)) {
                            Toast.makeText(getContext(), "Tên người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
                            return;

                        } else {
                            // Tạo đối tượng Users mới
                            Users newUser = new Users(newUsername, newPassword, newRole);

                            // Thêm vào cơ sở dữ liệu
                            boolean isAdded = userTableHelper.addUser(newUsername, newPassword);


                            if (isAdded) {
                                // Cập nhật danh sách và thông báo adapter
                                usersList.add(newUser);
                                userAdapter.notifyItemInserted(usersList.size() - 1);
                                Toast.makeText(getContext(), "Thêm người dùng thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Thêm người dùng thất bại! Tên người dùng đã tồn tại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }


    private void showEditDialog(Users users, int position) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.user_edit_dialog, null);

        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        RadioButton rdAdmin = dialogView.findViewById(R.id.rdAdmin);
        RadioButton rdUser = dialogView.findViewById(R.id.rdUser);
        RadioButton rdStaff = dialogView.findViewById(R.id.rdStaff);

        toggle = dialogView.findViewById(R.id.btnXem);
        new PasswordToggleHelper(edtPassword, toggle);



        edtUsername.setText(users.getUsername());
        edtPassword.setText(users.getPassword());
        rdAdmin.setChecked(users.getRole().equals("Admin"));
        rdUser.setChecked(users.getRole().equals("User"));
        rdStaff.setChecked(users.getRole().equals("Staff"));


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa người dùng")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newUsername = edtUsername.getText().toString().trim();
                    String newPassword = edtPassword.getText().toString().trim();
                    String newRole = rdAdmin.isChecked() ? "Admin" : rdStaff.isChecked() ? "Staff" : "User";



                    if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newPassword)) {
                        Toast.makeText(getContext(), "Các trường không thể để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        users.setUsername(newUsername);
                        users.setPassword(newPassword);
                        users.setRole(newRole);


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
