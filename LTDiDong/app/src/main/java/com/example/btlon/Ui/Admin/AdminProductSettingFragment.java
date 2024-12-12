package com.example.btlon.Ui.Admin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.btlon.Adapter.ProductAdapterRecycler;
import com.example.btlon.Data.Category;
import com.example.btlon.Data.CategoryTableHelper;
import com.example.btlon.Data.Product;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class AdminProductSettingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapterRecycler productAdapter;
    private List<Product> productList;
    private ProductTableHelper productTableHelper;
    private Button btnAddProduct;
    private String selectedImagePath;

    private Spinner spinnerCategory;

    // Declare the ActivityResultLauncher
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int STORAGE_PERMISSION_CODE = 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_product_setting_fragment, container, false);
// Initialize ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            handleImageUri(imageUri); // Xử lý ảnh được chọn
                        }
                    }
                }
        );


        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        boolean includeEdge = true;
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(2, spacing, includeEdge);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setHasFixedSize(true);

        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        productTableHelper = new ProductTableHelper(getContext());
        productList = productTableHelper.getAllProducts();

        productAdapter = new ProductAdapterRecycler(getContext(), (ArrayList<Product>) productList);
        recyclerView.setAdapter(productAdapter);

        btnAddProduct.setOnClickListener(v -> showAddProductDialog());

        productAdapter.setOnProductActionListener(new ProductAdapterRecycler.OnProductActionListener() {
            @Override
            public void onProductClick(Product product, int position) {
                showEditProductDialog(product, position);
            }

            @Override
            public void onProductLongClick(Product product, int position) {
                showDeleteProductDialog(product, position);
            }
        });

        Button btBackToSettings = view.findViewById(R.id.btBackToSettings);
        btBackToSettings.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        return view;
    }

    private void openImagePicker() {
        selectedImagePath = ""; // Đường dẫn ảnh được chọn
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            // Android 10 trở xuống: Kiểm tra quyền
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            } else {
                launchImagePicker();
            }
        } else {
            // Android 11 trở lên: Không cần quyền
            launchImagePicker();
        }
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void handleImageUri(Uri imageUri) {
        selectedImagePath = imageUri.toString();
        // Hiển thị ảnh trong ImageView
        ImageView imageView = getView().findViewById(R.id.img_product);
        imageView.setImageURI(imageUri);

        // Lưu URI để sử dụng sau

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker(); // Quyền được cấp, mở picker
            } else {
                Toast.makeText(getContext(), "Cần cấp quyền để chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to show Add Product Dialog
    private void showAddProductDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_edit_dialog, null);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductStock = dialogView.findViewById(R.id.edtProductStock);
        ImageView imgProduct = dialogView.findViewById(R.id.img_product);
        Button btn_select_image = dialogView.findViewById(R.id.btn_select_image);

        spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        List<Category> categories = CategoryTableHelper.getAllCategories();
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        btn_select_image.setOnClickListener(v -> openImagePicker());
        imgProduct.setOnClickListener(v -> openImagePicker());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm sản phẩm mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtProductName.getText().toString().trim();
                    String priceStr = edtProductPrice.getText().toString().trim();
                    String description = edtProductDescription.getText().toString().trim();
                    String stockStr = edtProductStock.getText().toString().trim();
                    Category selectedCategory = (Category) spinnerCategory.getSelectedItem();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
                        Toast.makeText(getContext(), "Tên và giá sản phẩm không thể để trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Product newProduct = new Product(name, priceStr, description, selectedImagePath, Integer.parseInt(stockStr), selectedCategory.getCategory_id());

                        if (productTableHelper.addProduct(newProduct)) {
                            productList.add(newProduct);
                            reloadCategories(); // Reload categories after adding product
                            productAdapter.notifyItemInserted(productList.size() - 1);
                            Toast.makeText(getContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Giá sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .setCancelable(false);

        builder.create().show();
    }

    // Method to show Edit Product Dialog
    private void showEditProductDialog(Product product, int position) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_edit_dialog, null);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductStock = dialogView.findViewById(R.id.edtProductStock);
        ImageView imgProduct = dialogView.findViewById(R.id.img_product);
        Button btn_select_image = dialogView.findViewById(R.id.btn_select_image);

        spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        List<Category> categories = CategoryTableHelper.getAllCategories();
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Set current product details
        edtProductName.setText(product.getName());
        edtProductPrice.setText(String.valueOf(product.getPrice()));
        edtProductDescription.setText(product.getDescription());
        edtProductStock.setText(String.valueOf(product.getQuantity()));
        if (!TextUtils.isEmpty(product.getImage())) {
            imgProduct.setImageURI(Uri.parse(product.getImage()));
        }
        // Chọn danh mục đúng cho sản phẩm
        int categoryIndex = getCategoryIndex(product.getCategory_id());
        if (categoryIndex != -1) {
            spinnerCategory.setSelection(categoryIndex);
        }


        // Image selection
        btn_select_image.setOnClickListener(v -> openImagePicker());
        imgProduct.setOnClickListener(v -> openImagePicker());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa sản phẩm")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String name = edtProductName.getText().toString().trim();
                    String priceStr = edtProductPrice.getText().toString().trim();
                    String description = edtProductDescription.getText().toString().trim();
                    String stockStr = edtProductStock.getText().toString().trim();


                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
                        Toast.makeText(getContext(), "Tên và giá sản phẩm không thể để trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // If no new image selected, keep the old one
                    String updatedImagePath = TextUtils.isEmpty(selectedImagePath) ? product.getImage() : selectedImagePath;


                    try {
                        product.setName(name);
                        product.setPrice(priceStr);
                        product.setDescription(description);
                        product.setQuantity(Integer.parseInt(stockStr));
                        product.setImage(updatedImagePath);
                        product.setCategory_id(((Category) spinnerCategory.getSelectedItem()).getCategory_id());

                        if (productTableHelper.updateProduct(product)) {
                            productList.set(position, product);
                            reloadCategories(); // Reload categories after updating product
                            productAdapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Giá sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .setCancelable(false);

        builder.create().show();
    }

    // Method to reload categories in spinner
    private void reloadCategories() {
        List<Category> updatedCategories = CategoryTableHelper.getAllCategories();
        if (spinnerCategory != null) {
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, updatedCategories);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);
        } else {
            Log.e("AdminProductSetting", "spinnerCategory is null in reloadCategories.");
        }
    }

    // Method to get category index for the current product
    private int getCategoryIndex(int categoryId) {
        List<Category> categories = CategoryTableHelper.getAllCategories();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategory_id() == categoryId) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }


    // Method to show Delete Product Dialog
    private void showDeleteProductDialog(Product product, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (productTableHelper.deleteProduct(product.getId())) {
                        productList.remove(position);
                        productAdapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "Sản phẩm đã bị xóa", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
