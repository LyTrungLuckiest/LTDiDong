package com.example.btlon.Data;

public class Cart {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;

    private String productName;
    private double price;
    private String image;


    public Cart(int cartId, int userId, int productId, int quantity, String productName, String price, String image) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.price = Double.parseDouble(price);
        this.image = image;

    }

    // Getters and setters

    public int getCartId() {
        return cartId;
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }



    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }



    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getImage() {
        return image;
    }
    public void setImage(){
        this.image = image;
    }
}
