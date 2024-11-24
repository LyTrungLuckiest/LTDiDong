package com.example.btlon.Data;

public class Address {
    private int id;           // ID địa chỉ (Primary Key)
    private int userId;       // ID người dùng liên kết (Foreign Key)
    private String address;   // Địa chỉ cụ thể
    private boolean isDefault; // Đánh dấu địa chỉ mặc định

    // Constructor đầy đủ 4 tham số
    public Address(int id, int userId, String address, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.isDefault = isDefault;
    }

    // Constructor đơn giản với 2 tham số (nếu cần)
    public Address(String address, boolean isDefault) {
        this.address = address;
        this.isDefault = isDefault;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
