package com.example.btlon.Ui.Home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.btlon.Adapter.MyArrayAdapter;
import com.example.btlon.Data.Products;
import com.example.btlon.Data.ProductTableHelper;

import com.example.btlon.R;

import java.util.ArrayList;
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


        ActionViewFlipper();
        loadProductsToGridView();

        return view;

    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://img.pikbest.com/templates/20240819/fruit-sale-promotion-banner-for-supermarkets_10740211.jpg!bwr800");
        mangquangcao.add("https://img.pikbest.com/templates/20240706/fruit-fruit-banner-for-supermarket-store-green-background_10654794.jpg!bwr800");
        mangquangcao.add("https://img.pikbest.com/templates/20240819/strawberry-fruit-sale-promotion-banner-for-supermarkets_10740343.jpg!bwr800");

        for (String url : mangquangcao) {
            ImageView imageView = new ImageView(requireContext());
            Glide.with(requireContext()).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        Animation slide_in_right = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right);
        Animation slide_out_right = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in_right);
        viewFlipper.setOutAnimation(slide_out_right);
    }

    private void loadProductsToGridView() {
        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Products> productsList = productTableHelper.getAllProducts();
        MyArrayAdapter adapter = new MyArrayAdapter((Activity) getActivity(), R.layout.item_product, (ArrayList<Products>) productsList);
        gridView.setAdapter(adapter);
    }

}
