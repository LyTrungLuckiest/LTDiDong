package com.example.btlon.OrderStatistic;

public class UserOrderCount {
    private int userId;
    private int totalOrders;

    public UserOrderCount(int userId, int totalOrders) {
        this.userId = userId;
        this.totalOrders = totalOrders;
    }

    public int getUserId() {
        return userId;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Total Orders: " + totalOrders;
    }
}

