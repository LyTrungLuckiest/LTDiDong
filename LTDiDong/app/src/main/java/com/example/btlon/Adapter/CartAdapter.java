package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.Cart;
import com.example.btlon.Data.CartTableHelper;
import com.example.btlon.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;
    private Context context;
    private CartTableHelper cartTableHelper;

    public CartAdapter(Context context, List<Cart> cartList, CartTableHelper cartTableHelper) {
        this.context = context;
        this.cartList = cartList;
        this.cartTableHelper = cartTableHelper;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        if (cart != null) {
            holder.productName.setText(cart.getProductName());
            holder.productPrice.setText(String.format("%s VND", cart.getPrice() * cart.getQuantity()));
            holder.productQuantity.setText(String.valueOf(cart.getQuantity()));



            // Tải ảnh bằng Glide
            Glide.with(holder.itemView.getContext())
                    .load(cart.getImage())
                    .placeholder(R.drawable.traicay)
                    .error(R.drawable.error_image)
                    .into(holder.productImage);

            // Xử lý nút tăng số lượng
            holder.btnIncrease.setOnClickListener(v -> {
                int currentQuantity = cart.getQuantity();
                cart.setQuantity(currentQuantity + 1);
                cartTableHelper.updateQuantity(cart.getCartId(), currentQuantity + 1);
                notifyItemChanged(position); // Cập nhật RecyclerView





            });

            // Xử lý nút giảm số lượng
            holder.btnDecrease.setOnClickListener(v -> {
                int currentQuantity = cart.getQuantity();
                if (currentQuantity > 1) { // Không cho giảm số lượng xuống 0
                    cart.setQuantity(currentQuantity - 1);
                    cartTableHelper.updateQuantity(cart.getCartId(), currentQuantity - 1);
                    notifyItemChanged(position); // Cập nhật RecyclerView

                } else {

                    if (position >= 0 && position < cartList.size()) {
                        cartList.remove(position);  // Xóa phần tử tại vị trí `position`
                        cartTableHelper.removeItemFromCart(cart.getCartId());  // Xóa phần tử khỏi cơ sở dữ liệu
                        notifyItemRemoved(position);  // Cập nhật RecyclerView
                        notifyItemRangeChanged(position, cartList.size());  // Cập nhật các phần tử xung quanh
                        notifyDataSetChanged();
                    }


                }
            });
            holder.btnDelete.setOnClickListener(v->{
                if (position >= 0 && position < cartList.size()) {
                    cartList.remove(position);  // Xóa phần tử tại vị trí `position`
                    cartTableHelper.removeItemFromCart(cart.getCartId());  // Xóa phần tử khỏi cơ sở dữ liệu
                    notifyItemRemoved(position);  // Cập nhật RecyclerView
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage, btnIncrease, btnDecrease;
        Button btnDelete;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);

            productImage = itemView.findViewById(R.id.productImage);
            btnIncrease = itemView.findViewById(R.id.item_giohang_cong);
            btnDecrease = itemView.findViewById(R.id.item_giohang_tru);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
