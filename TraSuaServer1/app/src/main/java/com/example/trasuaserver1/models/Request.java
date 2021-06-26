package com.example.trasuaserver1.models;

import java.util.List;

public class Request {
    public String name,phone,address,total,status;
    public List<OrdersModel> ordersModelList;  //list of item order

    public Request() {
    }

    public Request(String name, String phone, String address, String total, String status, List<OrdersModel> ordersModelList) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.status = status;
        this.ordersModelList = ordersModelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrdersModel> getOrdersModelList() {
        return ordersModelList;
    }

    public void setOrdersModelList(List<OrdersModel> ordersModelList) {
        this.ordersModelList = ordersModelList;
    }
}
