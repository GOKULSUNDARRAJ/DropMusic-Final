package com.gokulsundar4545.dropu;
public class User1 {
    String username;
    String email;
    String phone;

    String Email,Password,Name,Profission,Uid,token,status;

    public User1() {

    }

    public User1(String username, String email, String phone, String email1, String password, String name, String profission, String uid, String token, String status) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        Email = email1;
        Password = password;
        Name = name;
        Profission = profission;
        Uid = uid;
        this.token = token;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfission() {
        return Profission;
    }

    public void setProfission(String profission) {
        Profission = profission;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
