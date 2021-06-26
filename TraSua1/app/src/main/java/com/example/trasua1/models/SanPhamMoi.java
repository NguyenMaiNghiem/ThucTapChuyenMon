package com.example.trasua1.models;

public class SanPhamMoi {
    public String name,price,category,rating,img_url;

    public SanPhamMoi() {
    }

    public SanPhamMoi(String name, String price, String category, String rating, String img_url) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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
