package com.example.btlon.Data;

import java.util.List;

public class Cart {
    private int cartId;
    private Users user;
    private String cartDate;  // Thời gian tạo giỏ hàng (có thể là thời gian thêm sản phẩm vào giỏ)
    private double totalAmount; // Tổng giá trị các sản phẩm trong giỏ
    private boolean status;  // 0 là chưa thanh toán, 1 là đã thanh toán (nếu bạn cần chức năng này)
    private List<CartProduct> cartProducts; // Danh sách các sản phẩm trong giỏ hàng

    // Constructor khi không có tổng giá trị
    public Cart(int cartId, Users user, List<CartProduct> cartProducts) {
        this.cartId = cartId;
        this.user = user;
        this.cartDate = cartDate;
        this.cartProducts = cartProducts;
    }

    // Constructor khi có tổng giá trị và trạng thái thanh toán
    public Cart(int cartId, Users user, String cartDate, double totalAmount, List<CartProduct> cartProducts, boolean status) {
        this.cartId = cartId;
        this.user = user;
        this.cartDate = cartDate;
        this.totalAmount = totalAmount;
        this.cartProducts = cartProducts;
        this.status = status;
    }

    // Getter và Setter
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getCartDate() {
        return cartDate;
    }

    public void setCartDate(String cartDate) {
        this.cartDate = cartDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }
}
