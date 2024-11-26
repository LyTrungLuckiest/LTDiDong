package com.example.btlon.Data;

import java.io.Serializable;

public class Products implements Serializable {
    private int id;
    private String image;
    private String name;
    private String price;
    private String description;
    public Products( int id, String name, String description, String price, String image) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.id=id;
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
