package com.example.btlon.Data;

import java.io.Serializable;

public class Products implements Serializable {
    private int id;
    private String image;
    private String name;
    private String price;
    private String description;
    private String quality;
    public Products( String name, String description, String price, String image, String quality) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.quality=quality;
    }

    public Products( int id, String name, String description, String price, String image) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.id=id;
    }
    public Products( int id, String name,  String price) {
        this.image = image;
        this.name = name;
        this.price = price;

        this.id=id;
    }

    public Products(String productName, double price, int quantity) {
        this.name = productName;
        this.price = String.valueOf(price);
        this.quality = String.valueOf(quantity);

    }

    public Products(String productName, double price, int quantity, String imageUrl) {
        this.name = productName;
        this.price = String.valueOf(price);
        this.quality = String.valueOf(quantity);
        this.image = imageUrl;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
