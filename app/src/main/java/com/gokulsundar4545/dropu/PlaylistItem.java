package com.gokulsundar4545.dropu;

public class PlaylistItem {
    private String id;
    private String coverImage;
    private String coverName;

    // Default constructor required for Firebase
    public PlaylistItem() {
    }

    public PlaylistItem(String id, String coverImage, String coverName) {
        this.id = id;
        this.coverImage = coverImage;
        this.coverName = coverName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }
}
