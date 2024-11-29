package com.example.btlon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.Product;
import com.example.btlon.R;
import com.example.btlon.Utils.ChiTietSanPhamActivity;
import com.example.btlon.Utils.PreferenceManager;

import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

public class ProductAdapterRecycler extends RecyclerView.Adapter<ProductAdapterRecycler.ViewHolder> {
    private final Context context;
    private final List<Product> productList;
    private OnProductActionListener onProductActionListener;  // Listener đã được khai báo đúng

    public ProductAdapterRecycler(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Thiết lập dữ liệu cho item
        holder.txtProductName.setText(product.getName());
        holder.txtPrice.setText(product.getPrice() + " VNĐ/1kg");
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.traicay)
                .error(R.drawable.error_image)
                .into(holder.imgProduct);

        // Xử lý sự kiện nhấn vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
                    Log.d("ProductAdapterRecycler", "Clicked product: " + product.getName());
                    if (onProductActionListener != null) {
                        onProductActionListener.onProductClick(product, position);  // Gọi sự kiện nếu listener được gán
                    }
                    PreferenceManager preferenceManager = new PreferenceManager(context);
                    if (Objects.equals(preferenceManager.getUserRole(), "User")) {
                        // Chuyển sang Activity ChiTiếtSanPhamActivity
                        Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                        intent.putExtra("product", product);  // Chuyển đối tượng sản phẩm vào intent
                        context.startActivity(intent);

                    }
                });



        // Long click để xử lý sự kiện xóa hoặc sửa
        holder.itemView.setOnLongClickListener(v -> {
            if (onProductActionListener != null) {
                onProductActionListener.onProductLongClick(product, position);  // Gọi sự kiện long click
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Set listener để xử lý hành động click và long click
    public void setOnProductActionListener(OnProductActionListener listener) {
        this.onProductActionListener = listener;
    }

    // Interface cho các sự kiện click
    public interface OnProductActionListener {
        void onProductClick(Product product, int position);
        void onProductLongClick(Product product, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtPrice;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtTenSp);
            txtPrice = itemView.findViewById(R.id.txtGiaSp);
            imgProduct = itemView.findViewById(R.id.img_product);
        }
    }
}
