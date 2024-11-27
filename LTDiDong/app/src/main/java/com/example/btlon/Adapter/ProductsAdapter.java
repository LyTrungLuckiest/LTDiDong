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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlon.Utils.ChiTietSanPhamActivity;
import com.example.btlon.Data.Products;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;

import java.util.List;
public class ProductsAdapter extends BaseAdapter {
    private Activity context;
    private List<Products> productsList;
    private int layoutId;


    public ProductsAdapter(Activity context, int layoutId, List<Products> productsList) {
        this.context = context;
        this.layoutId = layoutId;
        this.productsList = productsList;
    }



    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, null);
        }








        Button btnMuaHang = convertView.findViewById(R.id.btnMuaHang);


        btnMuaHang.setOnClickListener(v -> {

            PreferenceManager preferenceManager = new PreferenceManager(context);
            preferenceManager.checkLoginStatus(context);

        });


        Products product = productsList.get(position);

        // Bắt sự kiện nhấn vào toàn bộ item
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
            intent.putExtra("chi tiết", product);
            context.startActivity(intent);
        });

        convertView.setOnClickListener(v -> {
            Log.d("ProductsAdapter", "Clicked product: " + product.getName());
            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
            intent.putExtra("chi tiết", product);
            context.startActivity(intent);
        });



        ImageView imgProduct = convertView.findViewById(R.id.img_product);
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.traicay)
                .error(R.drawable.error_image)
                .into(imgProduct);

        TextView txtProductName = convertView.findViewById(R.id.txtTenSp);
        txtProductName.setText(product.getName());

        TextView txtPrice = convertView.findViewById(R.id.txtGiaSp);
        txtPrice.setText(product.getPrice() + " VNĐ/1kg");

        return convertView;
    }


    public static void loadProductsFromDatabaseToGridView(Context context, GridView gridView, List<Products> productsList) {
        ProductsAdapter adapter = new ProductsAdapter((Activity) context, R.layout.item_product, productsList);
        gridView.setAdapter(adapter);
    }
    public void updateData(List<Products> newProducts) {
        productsList.clear();
        productsList.addAll(newProducts);
        notifyDataSetChanged();
    }



    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products sanpham = productsList.get(position);

        holder.txtTenSanPham.setText(sanpham.getName());
        holder.txtGiaSanPham.setText(sanpham.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                intent.putExtra("chi tiết", sanpham);
                context.startActivity(intent);
            }
        });
    }



    public int getItemCount() {
        return productsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSanPham, txtGiaSanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSanPham = itemView.findViewById(R.id.txttensp);
            txtGiaSanPham = itemView.findViewById(R.id.txtGiaSp);
        }
    }



}


