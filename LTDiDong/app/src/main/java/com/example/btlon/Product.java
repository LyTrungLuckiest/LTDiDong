package com.example.btlon;

public class Product {
    private String image;
    private String name;
    private String price;
    public Product(String image, String name, String price) {
        this.image = image;
        this.name = name;
        this.price = price;
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
}
