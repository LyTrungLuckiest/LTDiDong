package com.example.btlon.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.GioHang;
import com.example.btlon.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {


    Context context;
    List<GioHang> gioHangList;
    private CartUpdateListener cartUpdateListener;

    public CartAdapter(Context context, List<GioHang> gioHangList, CartUpdateListener listener) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.cartUpdateListener = listener;
    }
    // Định nghĩa interface
    public interface CartUpdateListener {
        void onCartUpdated();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent ,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang  gioHang = gioHangList.get(position);
        holder.item_giohang_tensanpham.setText(gioHang.getTenSp());
        holder.item_giohang_soluong.setText(gioHang.getSoLuong()+" ");
        Glide.with(context).load(gioHang.getHinhSp()).into(holder.item_giohang_image);

        DecimalFormat decimal = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimal.format((gioHang.getGiaSp())) + "Đ");
        long gia = gioHang.getSoLuong() * gioHang.getGiaSp();
        holder.item_giohang_giaSp2.setText(decimal.format(gia));








    }




    @Override
    public int getItemCount() {
        return gioHangList.size();
    }



    public class MyViewHolder extends  RecyclerView.ViewHolder {
        ImageView item_giohang_image;
        TextView item_giohang_tensanpham,item_giohang_gia,item_giohang_soluong,item_giohang_giaSp2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensanpham = itemView.findViewById(R.id.item_giohang_tensanpham);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giaSp2 = itemView.findViewById(R.id.item_giohang_giaSp2);






        }
    }


}
