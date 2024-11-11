package com.example.btlon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    private ViewFlipper viewFlipper;
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    SqliteHelper sqliteHelper;
    Button btnDeleteAll;
    Button btnOut;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
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
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        recyclerView = view.findViewById(R.id.recyclerview);
        frameLayout = view.findViewById(R.id.frameLayout);
        sqliteHelper = new SqliteHelper(getContext());

        btnDeleteAll = view.findViewById(R.id.buttonXoa);
        btnOut = view.findViewById(R.id.buttonOut);

        // Set onClickListeners
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelper.deleteAllData(); // Gọi hàm xóa tất cả dữ liệu
                Toast.makeText(getActivity(), "Đã xóa tất cả dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        ActionViewFlipper();
        return view;
    }

    private void ActionViewFlipper() {
        // Create a list of image URLs for the view flipper
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://www.bigc.vn/files/a-31-08-2023-11-41-07/21-31-01-si-u-h-i-tr-i-c-y-1080go.jpg");
        mangquangcao.add("https://www.bigc.vn/files/banners/2022/feb/tra-i-ca-y-giao-mu-a-1080x540-bigc.jpg");
        mangquangcao.add("https://www.bigc.vn/files/banners/new-node-31-05-2023-11-41-17/mega-mid-june-15-06-2023-16-56-56/1080-g-15-28-06-fruit-festival.jpg");

        // Add images to the ViewFlipper
        for (String url : mangquangcao) {
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        // Set ViewFlipper properties
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        // Add animation to the ViewFlipper
        Animation slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
}
