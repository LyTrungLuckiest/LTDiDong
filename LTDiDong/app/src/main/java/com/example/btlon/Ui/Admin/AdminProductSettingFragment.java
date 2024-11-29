package com.example.btlon.Ui.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btlon.Adapter.ProductAdapterRecycler;
import com.example.btlon.Data.Product;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.List;

public class AdminProductSettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapterRecycler productAdapter;
    private List<Product> productList;
    private ProductTableHelper productTableHelper;
    private Button btnAddProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_product_setting_fragment, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        // Thiết lập GridLayoutManager với 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);  // 2 là số cột
        recyclerView.setLayoutManager(gridLayoutManager);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);  // Tối ưu hóa hiệu suất

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

                        Product newProduct = new Product(name,priceStr, description, stockStr);

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
                .setCancelable(false);  // Không cho phép đóng dialog bằng cách nhấn ngoài

        builder.create().show();
    }

    private void showEditProductDialog(Product product, int position) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_edit_dialog, null);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductStock = dialogView.findViewById(R.id.edtProductStock);

        edtProductName.setText(product.getName());
        edtProductPrice.setText(String.valueOf(product.getPrice()));
        edtProductDescription.setText(product.getDescription());

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
                        product.setQuality(stockStr);

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
}
