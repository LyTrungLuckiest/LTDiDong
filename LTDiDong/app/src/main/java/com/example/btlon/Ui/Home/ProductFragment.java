package com.example.btlon.Ui.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager2.widget.ViewPager2;

import com.example.btlon.Adapter.CategoryPagerAdapter;
import com.example.btlon.Data.Category;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.btlon.Data.CategoryTableHelper;
import com.example.btlon.R;

import java.util.List;

public class ProductFragment extends Fragment {

    public ProductFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_product_fragment, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);


        CategoryTableHelper categoryTableHelper = new CategoryTableHelper(getContext());
        List<Category> categoryList = categoryTableHelper.getAllCategories();


        CategoryPagerAdapter adapter = new CategoryPagerAdapter(this, categoryList);

        viewPager.setAdapter(adapter);


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(categoryList.get(position).getName());
        }).attach();

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Category category = categoryList.get(position);
                tab.setText(category.getName());


                switch (category.getName()) {
                    case "Sản phẩm mới":
                        tab.setIcon(R.drawable.star);
                        break;
                    case "sản phẩm bán chạy":
                        tab.setIcon(R.drawable.fire);
                        break;
                    default:
                        tab.setIcon(R.drawable.fruit);
                        break;
                }
            }
        }).attach();


        return view;
    }
}
