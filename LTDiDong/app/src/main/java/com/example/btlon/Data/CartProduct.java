package com.example.btlon.Data;

public class CartProduct {
    private int cartProductId; // ID của sản phẩm trong giỏ hàng
    private Product product; // Sản phẩm trong giỏ
    private int quantity; // Số lượng của sản phẩm trong giỏ
    private double totalPrice; // Tổng giá của sản phẩm (số lượng * giá)

    public CartProduct( Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = Double.parseDouble(product.getPrice()) * quantity; // Tính tổng giá tại đây
    }

    // Getter và Setter
    public int getCartProductId() {
        return cartProductId;
    }

    public void setCartProductId(int cartProductId) {
        this.cartProductId = cartProductId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = Double.parseDouble(product.getPrice()) * quantity; // Cập nhật lại tổng giá khi thay đổi số lượng
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
