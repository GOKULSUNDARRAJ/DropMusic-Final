package com.gokulsundar4545.dropu;
public class Playlist {
    private String id;

    public Playlist() {
        // Default constructor required for calls to DataSnapshot.getValue(Playlist.class)
    }

    public Playlist(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
