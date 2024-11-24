package com.example.btlon.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.btlon.Data.Category;
import com.example.btlon.R;
import com.example.btlon.Ui.Home.Best_Selling_Product_Fragment;
import com.example.btlon.Ui.Home.FruitFragment;
import com.example.btlon.Ui.Home.New_Product_Fragment;
import com.example.btlon.Ui.Home.ProductFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CategoryPagerAdapter extends FragmentStateAdapter {

    private List<Category> categoryList;

    public CategoryPagerAdapter(@NonNull ProductFragment fragmentActivity, List<Category> categoryList) {
        super(fragmentActivity);
        this.categoryList = categoryList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Category category = categoryList.get(position);


        switch (category.getName()) {
            case "Sản phẩm mới":

                return new New_Product_Fragment();
            case "sản phẩm bán chạy":
                return new Best_Selling_Product_Fragment();
            default:
                return FruitFragment.newInstance(category.getCategory_id());
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

}
