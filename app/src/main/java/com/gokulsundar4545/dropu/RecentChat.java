package com.gokulsundar4545.dropu;

public class RecentChat {
    private String uid;
    private String mesage; // Keep this as mesage
    private String time;
    private String name;

    public RecentChat() {
        // Default constructor
    }

    public RecentChat(String uid, String mesage, String time, String name) {
        this.uid = uid;
        this.mesage = mesage;
        this.time = time;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
