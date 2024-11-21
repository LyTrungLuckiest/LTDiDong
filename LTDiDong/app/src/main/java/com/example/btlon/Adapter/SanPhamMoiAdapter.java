package com.example.btlon.Adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.btlon.Data.SanPhamMoi;

import java.util.List;

public class SanPhamMoiAdapter extends ArrayAdapter<SanPhamMoi> {
    Activity context;
    int IdLayout;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(@NonNull Context context, int resource, int idLayout, Activity context1, List<SanPhamMoi> array) {
        super(context, resource);
        IdLayout = idLayout;
        this.context = context1;
        this.array = array;
    }

    public SanPhamMoiAdapter(@NonNull Context context, int resource, int textViewResourceId, int idLayout, Activity context1, List<SanPhamMoi> array) {
        super(context, resource, textViewResourceId);
        IdLayout = idLayout;
        this.context = context1;
        this.array = array;
    }

    public SanPhamMoiAdapter(@NonNull Context context, int resource, @NonNull SanPhamMoi[] objects, int idLayout, Activity context1, List<SanPhamMoi> array) {
        super(context, resource, objects);
        IdLayout = idLayout;
        this.context = context1;
        this.array = array;
    }

    public SanPhamMoiAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull SanPhamMoi[] objects, int idLayout, Activity context1, List<SanPhamMoi> array) {
        super(context, resource, textViewResourceId, objects);
        IdLayout = idLayout;
        this.context = context1;
        this.array = array;
    }

    public SanPhamMoiAdapter(@NonNull Context context, int resource, @NonNull List<SanPhamMoi> objects, int idLayout, Activity context1, List<SanPhamMoi> array) {
        super(context, resource, objects);
        IdLayout = idLayout;
        this.context = context1;
        this.array = array;
    }

    public SanPhamMoiAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<SanPhamMoi> objects, int idLayout, Activity context1, List<SanPhamMoi> array) {
        super(context, resource, textViewResourceId, objects);
        IdLayout = idLayout;
        this.context = context1;
        this.array = array;
    }
}
