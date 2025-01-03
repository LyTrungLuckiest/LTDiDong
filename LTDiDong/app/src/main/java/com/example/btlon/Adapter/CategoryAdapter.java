package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Model.Category;
import com.example.btlon.R;

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


}
