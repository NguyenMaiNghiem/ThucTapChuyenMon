package com.example.trasua1.models;

import java.io.Serializable;

public class SanPhamNoiBat implements Serializable {
    public String name,rating,discount,category,img_url;
    public int price;

    public SanPhamNoiBat() {
    }

    public SanPhamNoiBat(String name, String rating, String discount, String category, String img_url, int price) {
        this.name = name;
        this.rating = rating;
        this.discount = discount;
        this.category = category;
        this.img_url = img_url;
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg_url() {
        return img_url;
    }



    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
