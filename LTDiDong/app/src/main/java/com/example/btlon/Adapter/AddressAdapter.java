package com.example.btlon.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.Address;
import com.example.btlon.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private ArrayList<Address> addressList;
    private Context context;

    private OnEditAddressCallback editAddressCallback;
    private OnDeleteAddressCallback deleteAddressCallback;

    public AddressAdapter(ArrayList<Address> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);

        if (address != null) {
            holder.addressTextView.setText(address.getAddress());
        } else {
            Log.e("AddressAdapter", "Address at position " + position + " is null.");

            holder.addressTextView.setText("Address not available");
        }

        // Handle Edit button click
        holder.btEditAddress.setOnClickListener(v -> {
            if (editAddressCallback != null) {
                editAddressCallback.onEditAddress(address);
            }
        });

        // Handle Delete button click
        holder.btDeleteAddress.setOnClickListener(v -> {
            if (deleteAddressCallback != null) {
                deleteAddressCallback.onDeleteAddress(address, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void setOnEditAddressCallback(OnEditAddressCallback callback) {
        this.editAddressCallback = callback;
    }

    public void setOnDeleteAddressCallback(OnDeleteAddressCallback callback) {
        this.deleteAddressCallback = callback;
    }

    public interface OnEditAddressCallback {
        void onEditAddress(Address address);
    }

    public interface OnDeleteAddressCallback {
        void onDeleteAddress(Address address, int position);
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        private TextView addressTextView;
        private Button btEditAddress;
        private Button btDeleteAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            btEditAddress = itemView.findViewById(R.id.btEditAddress);
            btDeleteAddress = itemView.findViewById(R.id.btDeleteAddress);
        }
    }
}
