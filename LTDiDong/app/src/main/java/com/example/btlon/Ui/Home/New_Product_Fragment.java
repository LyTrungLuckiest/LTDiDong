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
import com.example.btlon.Model.AdvertisementTableHelper;
import com.example.btlon.Model.ProductTableHelper;
import com.example.btlon.Model.Product;
import com.example.btlon.R;

import java.util.List;

public class New_Product_Fragment extends Fragment {
    private ViewFlipper viewFlipper;
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_newproduct_fragment, container, false);

        viewFlipper = view.findViewById(R.id.viewFlipper1);
        gridView = view.findViewById(R.id.gridView1);
        AdvertisementTableHelper advertisementTableHelper = new AdvertisementTableHelper(getContext());
        List<String> advertisementList = advertisementTableHelper.getAdvertisements();

        ActionViewFlipperAdapter actionViewFlipperAdapter = new ActionViewFlipperAdapter();
        actionViewFlipperAdapter.ActionViewFlipper(getContext(), viewFlipper, advertisementList);

        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Product> productList = productTableHelper.getNewProducts();
        ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.item_product, productList);
        gridView.setAdapter(adapter);

        return view;
    }



}
