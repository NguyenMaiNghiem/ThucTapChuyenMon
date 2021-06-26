package com.example.trasuaserver1.models;

public class TheLoai {

    String name;
    String category;
    String img_url;

    public TheLoai() {
    }

    public TheLoai(String name, String category, String img_url) {
        this.name = name;
        this.category = category;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
