package com.example.trasuaserver1.models;

public class UserModel {
    public String phone;
    public String name;
    public String email;
    public String password;
    public String address;
    public String profileImg;
    public String isAdmin;

    public UserModel() {
    }

    public UserModel(String phone, String name, String email, String address, String password) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.profileImg = "https://firebasestorage.googleapis.com/v0/b/trasua1.appspot.com/o/anhdaidien.jpg?alt=media&token=3fe54faa-5d8c-4d32-ab45-ddb6a5c4361d";
        this.isAdmin = "true";
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
