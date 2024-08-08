package com.gokulsundar4545.dropu;
import com.google.gson.annotations.SerializedName;

public class LyricsResponse {

    @SerializedName("lyrics")
    private String lyrics;

    public String getLyrics() {
        return lyrics;
    }
}
