package com.example.btlon.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.Product;
import com.example.btlon.R;
import com.example.btlon.Utils.ChiTietSanPhamActivity;
import com.example.btlon.Utils.PreferenceManager;

import java.util.List;
import java.util.Objects;

public class ProductAdapter extends BaseAdapter {
    private final Activity context;
    private final List<Product> productList;
    private final int layoutId;

    public ProductAdapter(Activity context, int layoutId, List<Product> productList) {
        this.context = context;
        this.layoutId = layoutId;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutId, null);
        }

        Product product = productList.get(position);

        // Ánh xạ các thành phần giao diện
        ImageView imgProduct = convertView.findViewById(R.id.img_product);
        TextView txtProductName = convertView.findViewById(R.id.txtTenSp);
        TextView txtPrice = convertView.findViewById(R.id.txtGiaSp);
        Button btnMuaHang = convertView.findViewById(R.id.btnMuaHang);

        // Thiết lập dữ liệu
        txtProductName.setText(product.getName());
        txtPrice.setText(product.getPrice() + " VNĐ/1kg");
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.traicay)
                .error(R.drawable.error_image)
                .into(imgProduct);

        // Xử lý sự kiện nhấn vào sản phẩm
        convertView.setOnClickListener(v -> {
            Log.d("ProductAdapter", "Clicked product: " + product.getName());
            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
        PreferenceManager preferenceManager = new PreferenceManager(context);
        String role = preferenceManager.getUserRole();
        Log.d("ProductAdapter", "User role: " + role); // Kiểm tra giá trị quyền


        // Xử lý sự kiện nhấn nút Mua hàng
        btnMuaHang.setOnClickListener(v -> {
            preferenceManager.checkLoginStatus(context);
        });

        return convertView;
    }

    public void updateData(List<Product> newProducts) {
        productList.clear();
        productList.addAll(newProducts);
        notifyDataSetChanged();
    }

    public static void loadProductsFromDatabaseToGridView(Context context, GridView gridView, List<Product> productList) {
        ProductAdapter adapter = new ProductAdapter((Activity) context, R.layout.item_product, productList);
        gridView.setAdapter(adapter);
    }
}
