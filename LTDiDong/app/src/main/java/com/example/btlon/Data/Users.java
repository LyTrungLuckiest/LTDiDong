package com.example.btlon.Data;

public class Users {
    private int user_id;
    private String username;
    private String password;
    private String role;

    public Users(int userId, String username, String password, String role) {
        this.user_id = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
