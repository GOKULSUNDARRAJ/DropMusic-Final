package com.gokulsundar4545.dropu;

public class Movie {
    private String movieName;
    private String movieCoverUrl;
    private String movieTitle;
    private String subTitle;

    public Movie() {
        // Default constructor required for calls to DataSnapshot.getValue(Movie.class)
    }

    public Movie(String movieName, String movieCoverUrl, String movieTitle, String subTitle) {
        this.movieName = movieName;
        this.movieCoverUrl = movieCoverUrl;
        this.movieTitle = movieTitle;
        this.subTitle = subTitle;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieCoverUrl() {
        return movieCoverUrl;
    }

    public void setMovieCoverUrl(String movieCoverUrl) {
        this.movieCoverUrl = movieCoverUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
