package com.example.trasua1.models;

public class VoucherModel {
    public String img_url;
    public String price;

    public VoucherModel() {
    }

    public VoucherModel(String img_url, String price) {
        this.img_url = img_url;
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
