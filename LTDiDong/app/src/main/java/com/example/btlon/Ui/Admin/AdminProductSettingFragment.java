package com.example.btlon.Ui.Admin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private static final int PICK_IMAGE_REQUEST = 1;
    private String selectedImagePath = "";

    // Declare the ActivityResultLauncher
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_product_setting_fragment, container, false);

        // Initialize ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        selectedImagePath = imageUri.toString();
                    }
                });

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

    private void showAddProductDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_edit_dialog, null);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductStock = dialogView.findViewById(R.id.edtProductStock);
        ImageView imgProduct = dialogView.findViewById(R.id.img_product);
        Button btn_select_image = dialogView.findViewById(R.id.btn_select_image);

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

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
                        Toast.makeText(getContext(), "Tên và giá sản phẩm không thể để trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Product newProduct = new Product(name, priceStr, description, selectedImagePath, Integer.parseInt(stockStr));

                        if (productTableHelper.addProduct(newProduct)) {
                            productList.add(newProduct);
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

    private void showEditProductDialog(Product product, int position) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_edit_dialog, null);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductStock = dialogView.findViewById(R.id.edtProductStock);
        ImageView imgProduct = dialogView.findViewById(R.id.img_product);
        Button btn_select_image = dialogView.findViewById(R.id.btn_select_image);

        btn_select_image.setOnClickListener(v -> openImagePicker());

        edtProductName.setText(product.getName());
        edtProductPrice.setText(String.valueOf(product.getPrice()));
        edtProductDescription.setText(product.getDescription());
        edtProductStock.setText(String.valueOf(product.getQuantity()));

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

                    try {
                        double price = Double.parseDouble(priceStr);
                        product.setName(name);
                        product.setPrice(String.valueOf(price));
                        product.setDescription(description);
                        product.setQuantity(Integer.parseInt(stockStr));
                        product.setImage(selectedImagePath);

                        if (productTableHelper.updateProduct(product)) {
                            productList.set(position, product);
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

    private void showDeleteProductDialog(Product product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (productTableHelper.deleteProduct(product.getId())) {
                        productList.remove(position);
                        productAdapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .setCancelable(false);

        builder.create().show();
    }

    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);  // Use imagePickerLauncher instead of startActivityForResult
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();  // If permission granted, open image picker
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
