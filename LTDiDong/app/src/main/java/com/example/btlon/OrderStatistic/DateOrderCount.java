package com.example.btlon.OrderStatistic;

public class DateOrderCount {
    private String date;
    private int totalOrders;

    public DateOrderCount(String date, int totalOrders) {
        this.date = date;
        this.totalOrders = totalOrders;
    }


    public int getTotalOrders() {
        return totalOrders;
    }

    public String getDate() {
        return date;
    }
}
