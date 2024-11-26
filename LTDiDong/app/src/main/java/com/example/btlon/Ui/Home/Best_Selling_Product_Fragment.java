package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.example.btlon.Adapter.ActionViewFlipperAdapter;
import com.example.btlon.Adapter.ProductsAdapter;
import com.example.btlon.Data.AdvertisementTableHelper;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.Data.Products;
import com.example.btlon.R;

import java.util.List;

public class Best_Selling_Product_Fragment extends Fragment {
    private ViewFlipper viewFlipper;
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_bestselling_product_fragment, container, false);


        viewFlipper = view.findViewById(R.id.viewFlipper2);
        gridView = view.findViewById(R.id.gridView2);


        AdvertisementTableHelper advertisementTableHelper = new AdvertisementTableHelper(getContext());
        List<String> advertisementList = advertisementTableHelper.getAdvertisements();

        ActionViewFlipperAdapter actionViewFlipperAdapter = new ActionViewFlipperAdapter();
        actionViewFlipperAdapter.ActionViewFlipper(getContext(), viewFlipper, advertisementList);


        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Products> productsList = productTableHelper.getBestSellingProducts();
        ProductsAdapter adapter = new ProductsAdapter(getActivity(), R.layout.item_product, productsList);
        gridView.setAdapter(adapter);
        return view;
    }
}
