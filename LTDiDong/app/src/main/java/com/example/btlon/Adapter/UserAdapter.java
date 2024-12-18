package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Model.Users;
import com.example.btlon.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<Users> usersList;
    private OnUserActionListener onUserActionListener;

    public UserAdapter(Context context, ArrayList<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    public void setOnUserActionListener(OnUserActionListener listener) {
        this.onUserActionListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users users = usersList.get(position);
        holder.txtId.setText(String.valueOf(users.getUserId()));
        holder.txtName.setText(users.getUsername());
        holder.txtPassword.setText(users.getPassword());
        holder.txtRole.setText(users.getRole());

        holder.btAction.setOnClickListener(v -> {
            if (onUserActionListener != null) {
                onUserActionListener.onAction(users, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtPassword, txtRole;
        Button btAction;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtName = itemView.findViewById(R.id.txtName);
            txtPassword = itemView.findViewById(R.id.txtPassword);
            txtRole = itemView.findViewById(R.id.txtRole);
            btAction = itemView.findViewById(R.id.btn);
        }
    }

    public interface OnUserActionListener {
        void onAction(Users users, int position);
    }
}
