package com.gokulsundar4545.dropu;

import java.util.List;
public class PlaylistModel {
    private String coverImage;
    private String coverName;
    private List<SongModel> songs;

    public PlaylistModel() {
        // Default constructor required for calls to DataSnapshot.getValue(PlaylistModel.class)
    }

    public PlaylistModel(String coverImage, String coverName, List<SongModel> songs) {
        this.coverImage = coverImage;
        this.coverName = coverName;
        this.songs = songs;
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

    public List<SongModel> getSongs() {
        return songs;
    }

    public void setSongs(List<SongModel> songs) {
        this.songs = songs;
    }
}
