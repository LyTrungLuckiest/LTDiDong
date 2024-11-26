package com.example.btlon.Data;

public class Address {
    private int id;
    private int userId;
    private String address;
    private boolean isDefault;


    public Address(int id, int userId, String address, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.isDefault = isDefault;
    }


    public Address(String address, boolean isDefault) {
        this.address = address;
        this.isDefault = isDefault;
    }


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
