package com.example.btlon.Ui.Home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.btlon.Adapter.MyArrayAdapter;
import com.example.btlon.Data.Products;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.Data.SqliteHelper;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private ViewFlipper viewFlipper;
    private GridView gridView;
    private FrameLayout frameLayout;
    private SqliteHelper sqliteHelper;
    private Button btnDeleteAll;
    private Button btnOut;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Ánh xạ View
        viewFlipper = view.findViewById(R.id.viewFlipper);
        gridView = view.findViewById(R.id.gridView);
        frameLayout = view.findViewById(R.id.frameLayout);
        sqliteHelper = new SqliteHelper(getContext());


        // Gọi các hàm cần thiết
        ActionViewFlipper();
        loadProductsToGridView();

        return view;
    }

    private void ActionViewFlipper() {
        // Danh sách URL hình ảnh quảng cáo
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://www.bigc.vn/files/a-31-08-2023-11-41-07/21-31-01-si-u-h-i-tr-i-c-y-1080go.jpg");
        mangquangcao.add("https://www.bigc.vn/files/banners/2022/feb/tra-i-ca-y-giao-mu-a-1080x540-bigc.jpg");
        mangquangcao.add("https://www.bigc.vn/files/banners/new-node-31-05-2023-11-41-17/mega-mid-june-15-06-2023-16-56-56/1080-g-15-28-06-fruit-festival.jpg");

        // Thêm hình ảnh vào ViewFlipper
        for (String url : mangquangcao) {
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        // Cài đặt ViewFlipper
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        // Thêm hiệu ứng cho ViewFlipper
        Animation slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void loadProductsToGridView() {
        // Lấy danh sách sản phẩm từ SQLite
        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());
        List<Products> productsList = productTableHelper.getAllProducts();
        MyArrayAdapter adapter = new MyArrayAdapter((Activity) getActivity(), R.layout.item_product, (ArrayList<Products>) productsList);
        gridView.setAdapter(adapter);
    }
}
