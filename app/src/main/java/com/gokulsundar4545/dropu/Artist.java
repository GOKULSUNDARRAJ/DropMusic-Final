package com.gokulsundar4545.dropu;

public class Artist {
    private String artistName;
    private String artistCoverUrl;
    private String Title;
    private String artistTitle;

    public Artist() {

    }

    public Artist(String artistName, String artistCoverUrl, String title, String artistTitle) {
        this.artistName = artistName;
        this.artistCoverUrl = artistCoverUrl;
        Title = title;
        this.artistTitle = artistTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtistCoverUrl() {
        return artistCoverUrl;
    }

    public void setArtistCoverUrl(String artistCoverUrl) {
        this.artistCoverUrl = artistCoverUrl;
    }

    public String getArtistTitle() {
        return artistTitle;
    }

    public void setArtistTitle(String artistTitle) {
        this.artistTitle = artistTitle;
    }
}
