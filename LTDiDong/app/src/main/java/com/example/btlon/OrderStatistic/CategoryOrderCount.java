package com.example.btlon.OrderStatistic;

public class CategoryOrderCount {
    private int categoryId;
    private int totalOrders;

    public CategoryOrderCount(int categoryId, int totalOrders) {
        this.categoryId = categoryId;
        this.totalOrders = totalOrders;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getTotalOrders() {
        return totalOrders;
    }


}
