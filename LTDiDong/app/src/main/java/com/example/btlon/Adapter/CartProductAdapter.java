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
import com.example.btlon.Data.CartProduct;
import com.example.btlon.Data.CartProductTableHelper;
import com.example.btlon.Data.CartTableHelper;
import com.example.btlon.Data.Product;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.R;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {
    private List<CartProduct> cartProducts;
    private Context context;
    private CartAdapter.CartUpdateListener cartUpdateListener;  // Add listener

    public CartProductAdapter(Context context, List<CartProduct> cartProducts, CartAdapter.CartUpdateListener cartUpdateListener) {
        this.context = context;
        this.cartProducts = cartProducts;
        this.cartUpdateListener = cartUpdateListener;  // Store the listener
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        CartProduct cart = cartProducts.get(position);
        ProductTableHelper productTableHelper = new ProductTableHelper(context);
        Product product = productTableHelper.getProductById(cart.getProduct().getId());
        CartTableHelper cartTableHelper = new CartTableHelper(context);
        CartProductTableHelper cartProductTableHelper = new CartProductTableHelper(context);

        if (cart != null) {
            holder.productName.setText(product.getName());
            holder.productPrice.setText(String.format("%s VND", Double.parseDouble(product.getPrice()) * cart.getQuantity()));
            holder.productQuantity.setText(String.valueOf(cart.getQuantity()));

            // Load image using Glide
            Glide.with(holder.itemView.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.traicay)
                    .error(R.drawable.error_image)
                    .into(holder.productImage);

            // Increase quantity
            holder.btnIncrease.setOnClickListener(v -> {
                int currentQuantity = cart.getQuantity();
                cart.setQuantity(currentQuantity + 1);
                cartProductTableHelper.updateCartProductQuantity(cart.getProduct().getId(), cart.getQuantity());

                // Refresh the RecyclerView and notify CartAdapter
                notifyItemChanged(position);
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();  // Notify CartAdapter
                }
            });

            // Decrease quantity
            holder.btnDecrease.setOnClickListener(v -> {
                int currentQuantity = cart.getQuantity();
                if (currentQuantity > 1) {
                    cart.setQuantity(currentQuantity - 1);
                    cartProductTableHelper.updateCartProductQuantity(cart.getProduct().getId(), cart.getQuantity());
                    notifyItemChanged(position);

                    // Notify CartAdapter
                    if (cartUpdateListener != null) {
                        cartUpdateListener.onCartUpdated();
                    }
                }
            });

            // Delete product
            holder.btnDelete.setOnClickListener(v -> {
                // Remove product from the list
                cartProducts.remove(position);
                cartTableHelper.deleteCartProductByProductId(cart.getProduct().getId());
                notifyItemRemoved(position);  // Update RecyclerView

                // Notify CartAdapter
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartProducts == null ? 0 : cartProducts.size();  // Return size of list
    }

    public static class CartProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage, btnIncrease, btnDecrease;
        Button btnDelete;

        public CartProductViewHolder(@NonNull View itemView) {
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
