package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ViewFlipper viewFlipper;
    private GridView gridView;
    private SqliteHelper sqliteHelper;

    Toolbar toolbar;
    RecyclerView navigationView;
    ListView listViewManHinhChinh;


    public SaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleFragment newInstance(String param1, String param2) {
        SaleFragment fragment = new SaleFragment();
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
        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        // Ánh xạ viewFlipper
        viewFlipper = view.findViewById(R.id.viewFlipper);
        gridView = view.findViewById(R.id.gridView);
        sqliteHelper = new SqliteHelper(getContext());

        // Thực hiện hành động ViewFlipper
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

        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.setAutoStart(true);

        Animation slide_in_right_sale = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right_sale);
        Animation slide_out_right_sale = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right_sale);
        viewFlipper.setInAnimation(slide_in_right_sale);
        viewFlipper.setOutAnimation(slide_out_right_sale);
    }

    private void loadProductsToGridView() {
        // Lấy danh sách sản phẩm từ SQLite
        ArrayList<Product> productList = sqliteHelper.getAllProducts();
        MyArrayAdapter adapter = new MyArrayAdapter(getActivity(), R.layout.item_product, productList);
        gridView.setAdapter(adapter);
    }





}