package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.btlon.R;

public class CartFragment extends Fragment {


    private Spinner spinnerPaymentMethod;
    private String selectedPaymentMethod = "Tiền mặt";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_cart_fragment, container, false);

        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);
        Button buttonCheckout = view.findViewById(R.id.buttonCheckout);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Tiền mặt", "MoMo", "ZaloPay", "Ngân hàng"}
        );
        spinnerPaymentMethod.setAdapter(adapter);

        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonCheckout.setOnClickListener(v -> handleCheckout());

        return view;
    }

    private void handleCheckout() {
        switch (selectedPaymentMethod) {
            case "Tiền mặt":
                processCashPayment();
                break;
            case "MoMo":
                processMoMoPayment();
                break;
            case "ZaloPay":
                processZaloPayPayment();
                break;
            case "Ngân hàng":
                processBankPayment();
                break;
            default:
                Toast.makeText(requireContext(), "Vui lòng chọn phương thức thanh toán hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void processCashPayment() {
        Toast.makeText(requireContext(), "Thanh toán bằng tiền mặt thành công!", Toast.LENGTH_SHORT).show();
    }

    private void processMoMoPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến MoMo...", Toast.LENGTH_SHORT).show();
    }

    private void processZaloPayPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến ZaloPay...", Toast.LENGTH_SHORT).show();
    }

    private void processBankPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến thanh toán ngân hàng...", Toast.LENGTH_SHORT).show();
    }
}
