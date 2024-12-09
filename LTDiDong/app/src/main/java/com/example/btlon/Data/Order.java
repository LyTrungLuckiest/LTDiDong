package com.example.btlon.Data;

import java.util.List;

public class Order {
    private int orderId;
    private Users user;
    private String orderDate;
    private double totalAmount;
    private boolean status;  // 0 là chưa thanh toán 1 là đã thanh toán r
    private List<OrderDetail> orderDetails;

    public Order( Users user, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.user = user;
        this.orderDate = orderDate;
        this.orderDetails = orderDetails;
    }
    public Order( int orderId,Users user, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.user = user;
        this.orderDate = orderDate;
        this.orderDetails = orderDetails;
    }
    public Order(int orderId, Users user, String orderDate, double totalAmount, List<OrderDetail> orderDetails, boolean status) {
        this.orderId = orderId;
        this.user = user;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
