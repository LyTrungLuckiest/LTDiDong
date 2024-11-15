package com.example.btlon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;  // Thêm import Glide

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<Product> {
    Activity context;
    int IdLayout;
    ArrayList<Product> mylist;

    // Constructor
    public MyArrayAdapter(Activity context, int idLayout, ArrayList<Product> mylist) {
        super(context, idLayout, mylist);
        this.context = context;
        IdLayout = idLayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Kiểm tra nếu convertView là null thì mới tạo mới View
        if (convertView == null) {
            // Tạo đế chứa Layout
            LayoutInflater myInflater = LayoutInflater.from(context);
            // Đặt Layout lên đế để tạo thành View
            convertView = myInflater.inflate(IdLayout, parent, false);
        }

        // Lấy 1 phần từ trong mảng ra
        Product myProduct = mylist.get(position);


        // Khai báo và hiển thị hình ảnh
        ImageView img_product = convertView.findViewById(R.id.img_product);

        // Sử dụng Glide để tải hình ảnh từ URL
        Glide.with(context)
                .load(myProduct.getImageUrl()) // URL của hình ảnh
                .placeholder(R.drawable.traicay) // Ảnh placeholder (hình ảnh mặc định khi tải)
                .error(R.drawable.error_image) // Ảnh hiển thị khi lỗi tải
                .into(img_product); // Đặt vào ImageView

        // Khai báo và hiển thị tên sản phẩm
        TextView txt_product = convertView.findViewById(R.id.txtTenSp);
        txt_product.setText(myProduct.getName());

        // Khai báo và hiển thị giá sản phẩm
        TextView txt_price = convertView.findViewById(R.id.txtGiaSp);
        txt_price.setText(myProduct.getPrice() + " VNĐ/1kg");

        return convertView;
    }

}
