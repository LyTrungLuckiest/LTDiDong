package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Model.Cart;
import com.example.btlon.Model.CartProduct;
import com.example.btlon.R;

import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> carts; // List of carts
    private Map<Integer, List<CartProduct>> cartProductMap; // cartId -> list of CartProducts
    private Context context;
    private CartUpdateListener cartUpdateListener;  // Declare CartUpdateListener

    // Modify constructor to accept CartUpdateListener
    public CartAdapter(Context context, List<Cart> carts, Map<Integer, List<CartProduct>> cartProductMap, CartUpdateListener cartUpdateListener) {
        this.context = context;
        this.carts = carts;
        this.cartProductMap = cartProductMap;
        this.cartUpdateListener = cartUpdateListener;  // Store the listener
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);
        List<CartProduct> cartProductList = cartProductMap.get(cart.getCartId());

        // Pass the CartUpdateListener to CartProductAdapter
        CartProductAdapter cartProductAdapter = new CartProductAdapter(context, cartProductList, cartUpdateListener);
        holder.recyclerViewCartProduct.setAdapter(cartProductAdapter);
        holder.recyclerViewCartProduct.setLayoutManager(new LinearLayoutManager(context));
    }


    @Override
    public int getItemCount() {
        return carts.size();
    }

    // Optional: Method to update data in CartAdapter
    public void updateData(List<Cart> carts, Map<Integer, List<CartProduct>> cartProductsMap) {
        this.carts = carts;
        this.cartProductMap = cartProductsMap;
        notifyDataSetChanged();  // Refresh RecyclerView
    }

    // ViewHolder class
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewCartProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewCartProduct = itemView.findViewById(R.id.recyclerViewCartProduct);
        }
    }

    // Interface for cart updates
    public interface CartUpdateListener {
        void onCartUpdated();  // Method to notify when the cart is updated
    }
}
