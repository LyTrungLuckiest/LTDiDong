package com.example.btlon.Ui.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Adapter.CategoryAdapter;
import com.example.btlon.Data.Category;
import com.example.btlon.Data.CategoryTableHelper;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.List;

public class AdminCategorySettingFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private CategoryTableHelper categoryTableHelper;
    private Button btnAddCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_category_setting_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddCategory = view.findViewById(R.id.btnAddCategory);

        // Khởi tạo CategoryTableHelper
        categoryTableHelper = new CategoryTableHelper(getContext());

        // Lấy tất cả danh mục
        categoryList = categoryTableHelper.getAllCategories();

        // Gán adapter
        categoryAdapter = new CategoryAdapter(getContext(), (ArrayList<Category>) categoryList);
        recyclerView.setAdapter(categoryAdapter);

        // Sự kiện khi nhấn nút thêm danh mục
        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        // Gắn sự kiện chỉnh sửa hoặc xóa danh mục
        categoryAdapter.setOnCategoryActionListener((category, position) -> {
            if (position >= 0 && position < categoryList.size()) {
                showActionDialog(category, position);
            } else {
                Toast.makeText(getContext(), "Vị trí không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện khi nhấn nút quay lại
        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);
        btBackToSettings.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        return view;
    }

    private void showActionDialog(Category category, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn hành động");

        builder.setItems(new CharSequence[]{"Chỉnh sửa", "Xóa"}, (dialog, which) -> {
            if (which == 0) {
                showEditCategoryDialog(category, position);
            } else if (which == 1) {
                deleteCategory(category, position);
            }
        });

        builder.create().show();
    }

    private void showAddCategoryDialog() {
        // Tạo giao diện cho hộp thoại từ layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.category_edit_dialog, null);

        EditText edtCategoryName = dialogView.findViewById(R.id.edtCategoryName);

        // Tạo hộp thoại
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm danh mục mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String categoryName = edtCategoryName.getText().toString().trim();
                    if (TextUtils.isEmpty(categoryName)) {
                        Toast.makeText(getContext(), "Danh mục không thể để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Thêm danh mục mới
                        if (categoryTableHelper.addCategory(categoryName)) {
                            Category newCategory = new Category(categoryName);
                            categoryList.add(newCategory);
                            categoryAdapter.notifyItemInserted(categoryList.size() - 1);
                            Toast.makeText(getContext(), "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thêm danh mục thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showEditCategoryDialog(Category category, int position) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.category_edit_dialog, null);

        EditText edtCategoryName = dialogView.findViewById(R.id.edtCategoryName);
        edtCategoryName.setText(category.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa danh mục")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String updatedCategoryName = edtCategoryName.getText().toString().trim();
                    if (TextUtils.isEmpty(updatedCategoryName)) {
                        Toast.makeText(getContext(), "Danh mục không thể để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (categoryTableHelper.updateCategory(category.getCategory_id(), updatedCategoryName)) {
                            category.setName(updatedCategoryName);
                            categoryList.set(position, category);
                            categoryAdapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "Cập nhật danh mục thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật danh mục thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void deleteCategory(Category category, int position) {
        if (categoryTableHelper.deleteCategory(category.getCategory_id())) {
            categoryList.remove(position);
            categoryAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Xóa danh mục thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
