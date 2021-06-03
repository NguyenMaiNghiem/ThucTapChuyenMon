package com.example.trasua1.models;

public class NavCategoryModel {
    public String name,img_url,category;

    public NavCategoryModel() {
    }

    public NavCategoryModel(String name, String img_url, String category) {
        this.name = name;
        this.img_url = img_url;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
