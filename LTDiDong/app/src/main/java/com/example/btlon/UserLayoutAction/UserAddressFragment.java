package com.example.btlon.UserLayoutAction;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlon.Adapter.AddressAdapter;
import com.example.btlon.Data.Address;
import com.example.btlon.R;
import java.util.ArrayList;

public class UserAddressFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button addAddressButton;
    private LinearLayout addressInputLayout;
    private EditText addressEditText;
    private Button saveButton;

    private  Button btCancelAdd;
    private CheckBox checkboxDefault;
    private AddressAdapter adapter;

    // Danh sách địa chỉ
    private ArrayList<Address> addressList = new ArrayList<>();

    // Trạng thái form (Thêm/Sửa)
    private boolean isEditMode = false;
    private Address editingAddress = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_address, container, false);

        // Khởi tạo các view
        recyclerView = view.findViewById(R.id.recycleviewAddress);
        addAddressButton = view.findViewById(R.id.btUserAddAddress);
        addressInputLayout = view.findViewById(R.id.addressInputLayout);
        addressEditText = view.findViewById(R.id.edtAddressInfo);
        saveButton = view.findViewById(R.id.btSaveAddress);
        checkboxDefault = view.findViewById(R.id.checkboxDefaultAddress);
        btCancelAdd= view.findViewById(R.id.btCancelAddAddress);

        // RecyclerView và Adapter
        adapter = new AddressAdapter(addressList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Ẩn layout nhập địa chỉ ban đầu
        addressInputLayout.setVisibility(View.GONE);

        // Xử lý nút "Thêm địa chỉ mới"
        addAddressButton.setOnClickListener(v -> {
            resetForm();
            addressInputLayout.setVisibility(View.VISIBLE);
        });

        // Xử lý nút "Lưu"
        saveButton.setOnClickListener(v -> handleSave());

        // Set Delete Address Callback
        adapter.setOnDeleteAddressCallback((address, position) -> {
            addressList.remove(position);  // Remove address from list
            adapter.notifyItemRemoved(position);  // Notify the adapter to remove the item
            Toast.makeText(getContext(), "Địa chỉ đã được xóa", Toast.LENGTH_SHORT).show();
        });

        // Callback từ Adapter khi chỉnh sửa địa chỉ
        adapter.setOnEditAddressCallback(address -> {
            isEditMode = true;
            editingAddress = address;
            addressInputLayout.setVisibility(View.VISIBLE);

            // Đổ dữ liệu địa chỉ vào form
            addressEditText.setText(address.getAddress());
            checkboxDefault.setChecked(address.isDefault());
        });

        btCancelAdd.setOnClickListener(v->{

            resetForm();
            addressInputLayout.setVisibility(View.GONE);

        });

        return view;
    }

    private void handleSave() {
        String address = addressEditText.getText().toString();
        boolean isDefault = checkboxDefault.isChecked();

        if (address.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra các điều kiện trước khi lưu
        if (isDefault && hasDefaultAddress() && (!isEditMode || (isEditMode && !editingAddress.isDefault()))) {
            Toast.makeText(getContext(), "Chỉ được có một địa chỉ mặc định", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isDefault && !hasDefaultAddress()) {
            Toast.makeText(getContext(), "Phải chọn một địa chỉ mặc định", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode && editingAddress != null) {
            // Cập nhật địa chỉ hiện tại
            editingAddress.setAddress(address);
            editingAddress.setDefault(isDefault);

            if (isDefault) {
                clearDefaultForOthers(editingAddress);
            }
        } else {
            // Thêm địa chỉ mới
            Address newAddress = new Address(addressList.size() + 1, 1, address, isDefault);
            addressList.add(newAddress);

            if (isDefault) {
                clearDefaultForOthers(newAddress);
            }
        }

        // Làm mới Adapter và đặt lại trạng thái
        adapter.notifyDataSetChanged();
        resetForm();
    }


    private void resetForm() {
        addressEditText.setText("");
        checkboxDefault.setChecked(false);
        addressInputLayout.setVisibility(View.GONE);
        isEditMode = false;
        editingAddress = null;
    }

    private boolean hasDefaultAddress() {
        for (Address address : addressList) {
            if (address.isDefault()) {
                return true;
            }
        }
        return false;
    }

    private void clearDefaultForOthers(Address currentAddress) {
        for (Address address : addressList) {
            if (address != currentAddress) {
                address.setDefault(false);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Button btUserAddressBack = view.findViewById(R.id.btUserAddressBack);
            if (btUserAddressBack != null) {
                NavController navController = Navigation.findNavController(view);
                btUserAddressBack.setOnClickListener(v -> navController.popBackStack());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("UserAddressFragment", "Lỗi khi thiết lập nút quay lại: " + e.getMessage());
        }
    }
}
