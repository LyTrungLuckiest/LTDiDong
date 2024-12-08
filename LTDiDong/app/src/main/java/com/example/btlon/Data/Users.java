package com.example.btlon.Data;

public class Users {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;
    private int UserId;



    public Users( String username, String password, String role) {

        this.username = username;
        this.password = password;
        this.role = role;
    }
    public Users( int userId) {

       this.userId=userId;
    }
    public Users(int Userid, String username, String password, String role) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.UserId = Userid;
    }


    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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

    public  String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
