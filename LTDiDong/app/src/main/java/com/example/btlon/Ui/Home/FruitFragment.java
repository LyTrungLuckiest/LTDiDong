package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.btlon.Adapter.ProductAdapter;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.Data.Product;
import com.example.btlon.R;

import java.util.List;

public class FruitFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "category_id";
    private int categoryId;

    public static FruitFragment newInstance(int categoryId) {
        FruitFragment fragment = new FruitFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fruit_fragment, container, false);

        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID, -1);
        }

        GridView gridView = view.findViewById(R.id.gridView3);
        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Product> productList = productTableHelper.getProductsByCategory(categoryId);
        ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.item_product, productList);
        gridView.setAdapter(adapter);
        return view;
    }
}
