package com.example.trasuaserver1.models;

public class VoucherModel {
    public String img_url;
    public int price;

    public VoucherModel() {
    }

    public VoucherModel(String img_url, int price) {
        this.img_url = img_url;
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
