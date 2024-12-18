package com.example.btlon.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Model.Address;
import com.example.btlon.R;

import java.util.List;

public class AddressCartAdapter extends RecyclerView.Adapter<AddressCartAdapter.AddressCartViewHolder> {
    private List<Address> addressList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Interface to handle clicks
    public interface OnItemClickListener {
        void onItemClick(Address address);
    }

    public AddressCartAdapter(List<Address> addressList, Context context, OnItemClickListener onItemClickListener) {
        this.addressList = addressList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AddressCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_address, parent, false);
        return new AddressCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressCartViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.txtAddress.setText(address.getAddress());

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(address);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressCartViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAddress;

        public AddressCartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.txtCartAddress);
        }
    }
}
