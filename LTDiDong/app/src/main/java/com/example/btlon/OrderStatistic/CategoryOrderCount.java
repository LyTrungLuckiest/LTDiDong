package com.example.btlon.OrderStatistic;

public class CategoryOrderCount {
    private int categoryId;
    private int totalOrders;

    private String mostSellingProductName;

    public CategoryOrderCount(int categoryId, int totalOrders,String mostSellingProductName) {
        this.categoryId = categoryId;
        this.totalOrders = totalOrders;
        this.mostSellingProductName=mostSellingProductName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public String getMostSellingProductName() {
        return mostSellingProductName;
    }
}
