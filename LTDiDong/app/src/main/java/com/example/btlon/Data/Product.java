package com.example.btlon.Data;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String image;
    private String name;
    private String price;
    private String description;
    private int quantity;
    private int category_id;
    private  String CategoryName;
    public  Product(String name, String price, String description, String image, int quantity, int category_id){
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.quantity = quantity;
        this.category_id=category_id;
    }

    public Product(String name, String description, String price, String image, int quantity) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description=description;
        this.quantity = quantity;
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
        this.quantity = quantity;

    }

    public Product(String productName, double price, int quantity, String imageUrl) {
        this.name = productName;
        this.price = String.valueOf(price);
        this.quantity =quantity;
        this.image = imageUrl;
    }
    public Product( String name, String price, String description) {
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public Product(String name, String priceStr, String description, int quantity) {
        this.name = name;
        this.price = priceStr;
        this.description = description;
        this.quantity = quantity;
    }

    public Product(int id, String name, String description, String price, String image, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = stock;
    }

    public Product(int id, String name, String description, String price, String image, int stock, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = stock;
        this.category_id = categoryId;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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


    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
