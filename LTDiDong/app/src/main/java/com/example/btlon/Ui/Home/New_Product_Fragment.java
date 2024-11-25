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
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.Data.Products;
import com.example.btlon.R;

import java.util.ArrayList;
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

        ActionViewFlipper();
        loadProductsToGridView();


        return view;
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://www.bigc.vn/files/a-31-08-2023-11-41-07/21-31-01-si-u-h-i-tr-i-c-y-1080go.jpg");
        mangquangcao.add("https://www.bigc.vn/files/banners/2022/feb/tra-i-ca-y-giao-mu-a-1080x540-bigc.jpg");
        mangquangcao.add("https://www.bigc.vn/files/banners/new-node-31-05-2023-11-41-17/mega-mid-june-15-06-2023-16-56-56/1080-g-15-28-06-fruit-festival.jpg");

        for (String url : mangquangcao) {
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        Animation slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void loadProductsToGridView() {
        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Products> productsList = productTableHelper.getNewProducts();
        MyArrayAdapter adapter = new MyArrayAdapter((Activity) getActivity(), R.layout.item_product, (ArrayList<Products>) productsList);
        gridView.setAdapter(adapter);
    }


}
