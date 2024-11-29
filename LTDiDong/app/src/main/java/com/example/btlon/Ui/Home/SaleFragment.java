package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.example.btlon.Adapter.ActionViewFlipperAdapter;
import com.example.btlon.Adapter.ProductAdapter;
import com.example.btlon.Data.AdvertisementTableHelper;
import com.example.btlon.Data.Product;
import com.example.btlon.Data.ProductTableHelper;

import com.example.btlon.R;

import java.util.List;

public class SaleFragment extends Fragment {


    ViewFlipper viewFlipper;
    private GridView gridView;


    public SaleFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_sale_fragment, container, false);

        viewFlipper = view.findViewById(R.id.viewFlipper);
        gridView = view.findViewById(R.id.gridView);

        // Lấy danh sách quảng cáo từ cơ sở dữ liệu
        AdvertisementTableHelper advertisementTableHelper = new AdvertisementTableHelper(getContext());
        List<String> advertisementList = advertisementTableHelper.getAdvertisements();

        ActionViewFlipperAdapter actionViewFlipperAdapter = new ActionViewFlipperAdapter();
        actionViewFlipperAdapter.ActionViewFlipper(getContext(), viewFlipper, advertisementList);

        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Product> productList = productTableHelper.getAllProducts();
        ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.item_product, productList);
        gridView.setAdapter(adapter);

        return view;

    }


}
