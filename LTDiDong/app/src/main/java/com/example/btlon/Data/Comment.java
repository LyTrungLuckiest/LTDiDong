package com.example.btlon.Data;

public class Comment {
    private int id;
    private int userId;
    private int productId;
    private String content;

    private String created_at;

    // Constructor, getter, và setter
    public Comment(int userId, int productId, String content) {
        this.userId = userId;
        this.productId = productId;
        this.content = content;

    }
    public Comment(int id,int userId, int productId, String content,String created_at) {
        this.id=id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.created_at=created_at;

    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

