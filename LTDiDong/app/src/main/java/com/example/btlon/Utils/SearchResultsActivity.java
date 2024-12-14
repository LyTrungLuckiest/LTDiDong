package com.example.btlon.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlon.Adapter.ProductAdapter;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.Data.Product;
import com.example.btlon.R;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private GridView gridView;
    private ProductAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_activity);


        // Ánh xạ view
        // Nút thoát
        ImageButton btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> finish()); // Đóng Activity khi nhấn nút

        searchView = findViewById(R.id.searchview1);
        gridView = findViewById(R.id.gridViewSearchResults);
        // Nhận từ khóa từ Intent
        Intent intent = getIntent();
        String query = intent.getStringExtra("search_query");
        if (query != null && !query.isEmpty()) {
            Toast.makeText(this, "Tìm kiếm sản phẩm: " + query, Toast.LENGTH_SHORT).show();

            // Xử lý logic tìm kiếm sản phẩm tại đây
        } else {
            Toast.makeText(this, "Không có từ khóa tìm kiếm.", Toast.LENGTH_SHORT).show();
        }

        // Tìm kiếm và hiển thị kết quả ban đầu
        if (query != null) {
            performSearch(query);
        }

        // Thiết lập sự kiện tìm kiếm trực tiếp trên `SearchResultsActivity`
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                searchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void performSearch(String query) {
        // Thực hiện tìm kiếm từ cơ sở dữ liệu
        ProductTableHelper productTableHelper = new ProductTableHelper(this);
        List<Product> searchResults = productTableHelper.searchProducts(query);

        // Cập nhật GridView với kết quả tìm kiếm
        if (adapter == null) {
            adapter = new ProductAdapter(this, R.layout.item_product, searchResults);
            gridView.setAdapter(adapter);
        } else {
            adapter.updateData(searchResults);
        }
    }


}




