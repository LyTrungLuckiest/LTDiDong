package com.example.btlon.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlon.Adapter.ProductCommentAdapter;
import com.example.btlon.Model.CartProduct;
import com.example.btlon.Model.CartProductTableHelper;
import com.example.btlon.Model.Comment;
import com.example.btlon.Model.CommentTableHelper;
import com.example.btlon.Model.Product;
import com.example.btlon.Model.Rating;
import com.example.btlon.Model.RatingTableHelper;
import com.example.btlon.R;
import com.example.btlon.Ui.Home.HomeActivity;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private TextView tensp, giaSp, mota, Soluong, tongDanhGia;
    private EditText edtComment;
    private Button btnSendComment;
    private ImageView imgHinhanh, Tru, Cong;
    private Product sanPhamMoi;
    private RecyclerView recyclerViewComment;
    private ProductCommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();
    private RatingBar ratingBar;
    private NotificationBadge badge;
    int productId;
    String userId;

    RatingTableHelper ratingTableHelper;

    PreferenceManager preferenceManager;
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        preferenceManager = new PreferenceManager(this);
        isLogin = preferenceManager.isLoggedIn();
        userId = preferenceManager.getUserId();

        // Kiểm tra userId
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return; // Ngừng thực thi nếu không có userId hợp lệ
        }

        initView();
        setupToolbar();
        loadProductData();
        setupRecyclerView();
        setupAddToCartButton();
        setupCommentButton();
        setupCartButton();

        // Kiểm tra nếu sanPhamMoi không phải null
        if (sanPhamMoi != null) {
            productId = sanPhamMoi.getId();
            tongDanhGia.setText("Tổng đánh giá: " + calculateAverageRating(productId));
            if (ratingTableHelper.getRatingForUserAndProduct(Integer.parseInt(userId), productId) == 0.0) {

                float ratingValue = calculateAverageRating(productId);
                ratingBar.setRating(ratingValue);

            } else
                ratingBar.setRating(ratingTableHelper.getRatingForUserAndProduct(Integer.parseInt(userId), productId));
        }

        // Lắng nghe sự kiện khi giá trị của RatingBar thay đổi
        if (isLogin) {
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (sanPhamMoi != null) {
                        ratingTableHelper.addOrUpdateRating(Integer.parseInt(userId), sanPhamMoi.getId(), rating);
                        ratingBar.setRating(ratingTableHelper.getRatingForUserAndProduct(Integer.parseInt(userId), sanPhamMoi.getId()));
                        tongDanhGia.setText("Tổng đánh giá: " + calculateAverageRating(productId));
                    }
                }
            });
        } else Toast.makeText(this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCommentsFromDatabase();
        // Cập nhật badge sau khi quay lại màn hình sản phẩm
        updateCartBadge();
    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giaSp = findViewById(R.id.txtgia);
        mota = findViewById(R.id.txtmotachitiet);
        imgHinhanh = findViewById(R.id.imageChiTiet);
        edtComment = findViewById(R.id.edtcomment);
        btnSendComment = findViewById(R.id.btSendComment);
        recyclerViewComment = findViewById(R.id.recyclerViewComment);
        Tru = findViewById(R.id.Tru);
        Cong = findViewById(R.id.Cong);
        Soluong = findViewById(R.id.Soluong);
        ratingBar = findViewById(R.id.ratingBar);
        tongDanhGia = findViewById(R.id.txtAverageRating);
        ratingTableHelper = new RatingTableHelper(this);
        badge = findViewById(R.id.menu_sl);



    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadProductData() {

        sanPhamMoi = (Product) getIntent().getSerializableExtra("product");
        if (sanPhamMoi != null) {
            tensp.setText(sanPhamMoi.getName() != null ? sanPhamMoi.getName() : "Tên sản phẩm không có");
            mota.setText(sanPhamMoi.getDescription() != null ? sanPhamMoi.getDescription() : "Mô tả không có");
            giaSp.setText(formatPrice(sanPhamMoi.getPrice()));
            Glide.with(this).load(sanPhamMoi.getImage()).into(imgHinhanh);
        } else {
            tensp.setText("Không có dữ liệu sản phẩm");
            mota.setText("");
            giaSp.setText("");
            imgHinhanh.setImageResource(R.drawable.error_image);
        }

    }

    private String formatPrice(String price) {
        if (price != null && !price.isEmpty()) {
            try {
                DecimalFormat decimal = new DecimalFormat("###,###,###");
                return "Giá: " + decimal.format(Double.parseDouble(price.trim())) + "Đ";
            } catch (NumberFormatException e) {
                return "Giá: Không hợp lệ";
            }
        } else {
            return "Giá: Không xác định";
        }
    }

    private void setupRecyclerView() {
        commentAdapter = new ProductCommentAdapter(this, commentList);
        recyclerViewComment.setAdapter(commentAdapter);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));
    }




    private void setupAddToCartButton() {
        Button addToCartButton = findViewById(R.id.btnthemvaogiohang);
        if (isLogin) {
            addToCartButton.setOnClickListener(v -> {
                if (sanPhamMoi != null) {
                    int quantity = Integer.parseInt(Soluong.getText().toString());
                    CartProductTableHelper cartProductTableHelper = new CartProductTableHelper(this);
                    CartProduct cartProduct = new CartProduct(sanPhamMoi, quantity);

                    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                    CartProduct existingProduct = cartProductTableHelper.getCartProductByProductId(Integer.parseInt(userId), sanPhamMoi.getId());

                    if (existingProduct != null) {
                        // Nếu sản phẩm đã có trong giỏ hàng, chỉ cần cập nhật số lượng
                        int newQuantity = existingProduct.getQuantity() + quantity;
                        cartProduct.setQuantity(newQuantity);
                        cartProductTableHelper.addOrUpdateCartProduct(Integer.parseInt(userId), cartProduct);
                    } else {
                        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
                        cartProductTableHelper.addOrUpdateCartProduct(Integer.parseInt(userId), cartProduct);
                    }

                    // Cập nhật badge hiển thị số lượng trong giỏ hàng
                    updateCartBadge();

                    Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
        }

        Tru.setOnClickListener(v -> updateQuantity(-1));
        Cong.setOnClickListener(v -> updateQuantity(1));
    }

    // Phương thức cập nhật badge với tổng số lượng sản phẩm trong giỏ
    private void updateCartBadge() {
        CartProductTableHelper cartProductTableHelper = new CartProductTableHelper(this);
        List<CartProduct> cartProducts = cartProductTableHelper.getCartProductsByCartId(Integer.parseInt(userId));

        int totalQuantity = 0;
        for (CartProduct product : cartProducts) {
            totalQuantity += product.getQuantity();  // Tính tổng số lượng sản phẩm trong giỏ
        }

        badge.setText(String.valueOf(totalQuantity));  // Cập nhật badge với tổng số lượng sản phẩm trong giỏ

        // Debugging: Kiểm tra số lượng sản phẩm trong giỏ
        Log.d("Cart", "Total quantity in cart: " + totalQuantity);
    }


    private void setupCommentButton() {
        if (isLogin) {
            btnSendComment.setOnClickListener(v -> {
                String commentText = edtComment.getText().toString().trim();
                if (!commentText.isEmpty() && sanPhamMoi != null) {
                    int productId = sanPhamMoi.getId();
                    if (new CommentTableHelper(this).insertComment(Integer.parseInt(userId), productId, commentText)) {
                        Toast.makeText(this, "Bình luận đã được thêm", Toast.LENGTH_SHORT).show();
                        loadCommentsFromDatabase();
                        edtComment.setText("");
                    } else {
                        Toast.makeText(this, "Không thể thêm bình luận", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
    }


    private void setupCartButton() {
        findViewById(R.id.framegiohang).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("isCartTransition", true); // Truyền biến
            Log.d("HomeActivity", "Starting HomeActivity with isCartTransition = true");
            startActivity(intent);
        });

    }


    private void loadCommentsFromDatabase() {
        if (sanPhamMoi != null) {
            List<Comment> comments = new CommentTableHelper(this).getCommentsByProductId(sanPhamMoi.getId());
            commentList.clear();
            commentList.addAll(comments);
            commentAdapter.notifyDataSetChanged();
        }
    }

    private void updateQuantity(int delta) {
        int currentQuantity = Integer.parseInt(Soluong.getText().toString());
        if (currentQuantity + delta > 0) {
            Soluong.setText(String.valueOf(currentQuantity + delta));

        }
    }

    public float calculateAverageRating(int productId) {
        RatingTableHelper ratingTableHelper = new RatingTableHelper(this);
        ArrayList<Rating> ratings = ratingTableHelper.getRatingsForProduct(productId);

        if (ratings.isEmpty()) {
            return 0; // Trả về 0 nếu không có đánh giá
        }

        float totalRating = 0;
        for (Rating rating : ratings) {
            totalRating += rating.getRating(); // Thêm giá trị rating của mỗi đánh giá
        }

        return totalRating / ratings.size(); // Tính trung bình
    }


}
