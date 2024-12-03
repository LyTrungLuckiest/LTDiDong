package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContentProviderCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.CartManager;
import com.example.btlon.Data.Category;
import com.example.btlon.R;
import com.example.btlon.Utils.Utils;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> categoryList;
    private OnCategoryActionListener onCategoryActionListener;
    private RecyclerView recyclerView;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setOnCategoryActionListener(OnCategoryActionListener listener) {
        this.onCategoryActionListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtId.setText(String.valueOf(category.getCategory_id()));  // Set the category ID
        holder.txtCategoryName.setText(category.getName());  // Set the category name


        holder.btAction.setOnClickListener(v -> {
            if (onCategoryActionListener != null) {
                onCategoryActionListener.onAction(category, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName, txtId;
        Button btAction;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId_category);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            btAction = itemView.findViewById(R.id.btnA);
        }
    }

    public interface OnCategoryActionListener {
        void onAction(Category category, int position);
    }
   // Gán adapter cho RecyclerView


    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CartAdapter cartAdapter = new CartAdapter(CartManager.getCart());
        recyclerView.setAdapter(cartAdapter);
        updateCartView(); // Cập nhật giao diện giỏ hàng
    }

    private void updateCartView() {
        if (Utils.manggiohang.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }









}
