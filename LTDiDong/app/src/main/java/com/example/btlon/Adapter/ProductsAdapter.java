package com.example.btlon.Adapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.btlon.Data.Products;
import com.example.btlon.R;
import java.util.List;
public class ProductsAdapter extends BaseAdapter {
    private Activity context;
    private List<Products> productsList;
    private int layoutId;


    public ProductsAdapter(Activity context, int layoutId, List<Products> productsList) {
        this.context = context;
        this.layoutId = layoutId;
        this.productsList = productsList;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, null);
        }


        Products product = productsList.get(position);


        ImageView imgProduct = convertView.findViewById(R.id.img_product);
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.traicay)
                .error(R.drawable.error_image)
                .into(imgProduct);

        TextView txtProductName = convertView.findViewById(R.id.txtTenSp);
        txtProductName.setText(product.getName());

        TextView txtPrice = convertView.findViewById(R.id.txtGiaSp);
        txtPrice.setText(product.getPrice() + " VNĐ/1kg");

        return convertView;
    }


    public static void loadProductsFromDatabaseToGridView(Context context, GridView gridView, List<Products> productsList) {
        ProductsAdapter adapter = new ProductsAdapter((Activity) context, R.layout.item_product, productsList);
        gridView.setAdapter(adapter);
    }
    public void updateData(List<Products> newProducts) {
        productsList.clear(); // Xóa dữ liệu cũ
        productsList.addAll(newProducts); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Thông báo Adapter cập nhật
    }
}
