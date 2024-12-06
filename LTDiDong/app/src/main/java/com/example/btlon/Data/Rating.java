package com.example.btlon.Data;

public class Rating {
    private int ratingId;
    private int productId;
    private int userId;
    private float rating;


    // Constructor
    public Rating(int ratingId, int productId, int userId, float rating) {
        this.ratingId = ratingId;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;

    }

    // Getters and Setters
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


}
