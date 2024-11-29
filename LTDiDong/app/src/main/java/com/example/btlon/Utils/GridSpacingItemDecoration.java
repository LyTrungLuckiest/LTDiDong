package com.example.btlon.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount; // Số cột của GridLayout
    private int spacing; // Khoảng cách giữa các item
    private boolean includeEdge; // Bao gồm khoảng cách ở các cạnh

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // Vị trí của item trong RecyclerView
        int column = position % spanCount; // Cột của item

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // Khoảng cách trái
            outRect.right = (column + 1) * spacing / spanCount; // Khoảng cách phải

            if (position < spanCount) {
                outRect.top = spacing; // Khoảng cách trên cho dòng đầu
            }
            outRect.bottom = spacing; // Khoảng cách dưới cho mỗi item
        } else {
            outRect.left = column * spacing / spanCount; // Khoảng cách trái
            outRect.right = spacing - (column + 1) * spacing / spanCount; // Khoảng cách phải

            if (position >= spanCount) {
                outRect.top = spacing; // Khoảng cách trên cho các item không phải dòng đầu
            }
        }
    }
}

