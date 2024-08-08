package com.gokulsundar4545.dropu;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LyricsApiService {

    @GET("/v1/{artist}/{title}")
    Call<LyricsResponse> getLyrics(
            @Path("artist") String artist,
            @Path("title") String title
    );
}
