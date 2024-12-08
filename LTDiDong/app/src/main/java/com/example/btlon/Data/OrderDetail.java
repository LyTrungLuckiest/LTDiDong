package com.example.btlon.Data;

public class OrderDetail {
    private int orderDetailId;
    private Product product; // Mỗi dòng chi tiết liên kết đến 1 Product
    private int quantity;

    private double totalPrice;


    public OrderDetail(int orderDetailId, Product product, int quantity) {
        this.orderDetailId = orderDetailId;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = Double.parseDouble(product.getPrice())* quantity;// Calculate total price here

    }


    public  OrderDetail(int orderDetailId){
        this.orderDetailId=orderDetailId;
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
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
    }


}
