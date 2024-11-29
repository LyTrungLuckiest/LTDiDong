package com.example.btlon.Data;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String image;
    private String name;
    private String price;
    private String description;
    private String quality;
    public Product(String name, String description, String price, String image, String quality) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.quality=quality;
    }

    public Product(int id, String name, String description, String price, String image) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.id=id;
    }
    public Product(int id, String name, String price) {
        this.image = image;
        this.name = name;
        this.price = price;

        this.id=id;
    }

    public Product(String productName, double price, int quantity) {
        this.name = productName;
        this.price = String.valueOf(price);
        this.quality = String.valueOf(quantity);

    }

    public Product(String productName, double price, int quantity, String imageUrl) {
        this.name = productName;
        this.price = String.valueOf(price);
        this.quality = String.valueOf(quantity);
        this.image = imageUrl;
    }
    public Product( String name, String price, String description) {
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public Product(String name, String priceStr, String description, String stockStr) {
        this.name = name;
        this.price = priceStr;
        this.description = description;
        this.quality = stockStr;
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
