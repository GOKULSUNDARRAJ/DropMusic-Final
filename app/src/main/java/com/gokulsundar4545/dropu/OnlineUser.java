package com.gokulsundar4545.dropu;
public class OnlineUser {
    private String uid;
    private String email;
    private String username;
    private boolean online;
    public OnlineUser() {
        // Default constructor required for calls to DataSnapshot.getValue(OnlineUser.class)
    }
    public OnlineUser(String uid, String email, String username, boolean online) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.online = online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
