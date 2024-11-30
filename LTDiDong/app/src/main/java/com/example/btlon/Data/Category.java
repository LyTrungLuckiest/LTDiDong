package com.example.btlon.Data;

import androidx.annotation.NonNull;

public class Category {

    private String name;
    private int category_id;


    public Category(int category_id, String name) {
        this.name = name;
        this.category_id = category_id;

    }

    public Category(String name) {
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;  // Trả về tên danh mục khi hiển thị trong Spinner
    }
}
