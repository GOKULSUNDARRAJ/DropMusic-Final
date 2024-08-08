package com.gokulsundar4545.dropu;

// UserProfile.java
public class UserProfile {
    private String uid;
    private String username;
    private String email;
    private String phone;
    private boolean online;

    // Constructors, getters, setters...
    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
    }

    public UserProfile(String uid, String username, String email, String phone, boolean online) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.online = online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
