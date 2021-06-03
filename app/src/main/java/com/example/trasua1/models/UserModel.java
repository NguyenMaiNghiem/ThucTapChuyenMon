package com.example.trasua1.models;

public class UserModel {
    public String phone;
    public String name;
    public String email;
    public String password;

    public UserModel() {
    }

    public UserModel(String phone, String name, String email, String password) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
