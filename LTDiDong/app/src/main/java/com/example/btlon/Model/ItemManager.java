package com.example.btlon.Model;

public class ItemManager {
    private String title;
    private int imageResId;

    public ItemManager(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}
