package com.example.btlon.Data;

import java.util.List;

public class Order {
    private int orderId;
    private Users user;
    private String orderDate;
    private double totalAmount;
    private List<OrderDetail> orderDetails;

    public Order(int orderId, Users user, String orderDate, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.user = user;
        this.orderDate = orderDate;
        this.orderDetails = orderDetails;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
