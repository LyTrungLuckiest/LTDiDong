package com.example.btlon;

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

public class CartFragment extends Fragment {

    // Parameters for fragment initialization
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Spinner spinnerPaymentMethod;
    private String selectedPaymentMethod = "Tiền mặt"; // Default value

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        // Initialize Spinner and Button
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);
        Button buttonCheckout = view.findViewById(R.id.buttonCheckout);

        // Set up payment methods for Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Tiền mặt", "MoMo", "ZaloPay", "Ngân hàng"}
        );
        spinnerPaymentMethod.setAdapter(adapter);

        // Handle payment method selection
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Keep the default payment method
            }
        });

        // Handle checkout button click
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
        // Add MoMo SDK intent here
    }

    private void processZaloPayPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến ZaloPay...", Toast.LENGTH_SHORT).show();
        // Add ZaloPay SDK intent here
    }

    private void processBankPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến thanh toán ngân hàng...", Toast.LENGTH_SHORT).show();
        // Add banking SDK or intent here
    }
}
