package com.example.btlon.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.Products;
import com.example.btlon.R;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<Products> {
    Activity context;
    int IdLayout;
    ArrayList<Products> mylist;

    public MyArrayAdapter(Activity context, int idLayout, ArrayList<Products> mylist) {
        super(context, idLayout, mylist);
        this.context = context;
        IdLayout = idLayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater myInflater = LayoutInflater.from(context);
            convertView = myInflater.inflate(IdLayout, parent, false);
        }

        Products myProducts = mylist.get(position);

        ImageView img_product = convertView.findViewById(R.id.img_product);
        Glide.with(context)
                .load(myProducts.getImageUrl())
                .placeholder(R.drawable.traicay)
                .error(R.drawable.error_image)
                .into(img_product);

        TextView txt_product = convertView.findViewById(R.id.txtTenSp);
        txt_product.setText(myProducts.getName());

        TextView txt_price = convertView.findViewById(R.id.txtGiaSp);
        txt_price.setText(myProducts.getPrice() + " VNĐ/1kg");

        return convertView;
    }
}
