package com.example.trasua1.models;

import java.io.Serializable;

public class ViewAllModel implements Serializable {
    public String name,category,rating,img_url;
    public int price;


    public ViewAllModel() {
    }

    public ViewAllModel(String name, int price, String category, String rating, String img_url) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.rating = rating;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
