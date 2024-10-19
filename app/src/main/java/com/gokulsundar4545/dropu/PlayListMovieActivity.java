package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlayListMovieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter8 adapter;
    private List<SongModel> songList;
    private String movieName;
    private String movieCoverUrl;
    private String movieTitle;
    private String subTitle;
    private ImageView progressBar;
    private FloatingActionButton download;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlistmovie);

        download = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);

        // Initially, set the progress bar as indeterminate and hide it

        progressBar.setVisibility(View.INVISIBLE);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAllSongs();
                download.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE); // Show the progress bar when downloading starts
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieName = extras.getString("MOVIE_NAME");
            movieCoverUrl = extras.getString("MOVIE_COVER_URL");
            movieTitle = extras.getString("MOVIE_TITLE");

        }

        ImageView categoryCoverUrl = findViewById(R.id.categoryCoverUrl);
        Picasso.get().load(movieCoverUrl).into(categoryCoverUrl);
        CollapsingToolbarLayout collaps=findViewById(R.id.collapslayput);

        androidx.coordinatorlayout.widget.CoordinatorLayout card;
        card=findViewById(R.id.card);
        Glide.with(this)
                .asBitmap()
                .load(movieCoverUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        // Handle the error by setting a default color (dark)
                        int fallbackColor = getResources().getColor(android.R.color.black);
                        recyclerView.setBackgroundColor(fallbackColor);
                        card.setBackgroundColor(fallbackColor);
                        collaps.setBackgroundColor(fallbackColor);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        // Use Palette API to extract dark color from the loaded bitmap
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Extract the dark vibrant color or fallback to a dark muted color
                                int darkColor = palette.getDarkVibrantColor(
                                        palette.getDarkMutedColor(getResources().getColor(android.R.color.black))
                                );

                                // Apply the dark color to the views
                                recyclerView.setBackgroundColor(darkColor);
                                card.setBackgroundColor(darkColor);
                                collaps.setBackgroundColor(darkColor);
                            }
                        });
                        return false;
                    }
                })
                .into(categoryCoverUrl);




        collaps.setTitle(movieTitle);

        recyclerView = findViewById(R.id.suggesedrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        songList = new ArrayList<>();
        adapter = new SearchAdapter8(this, songList);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        songList.clear(); // Clear existing data
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String songTitle = documentSnapshot.getString("title");
                                String moviename = documentSnapshot.getString("moviename");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                // Only process songs with the given movie name
                                if (movieName.equals(moviename)) {
                                    String subtitle = documentSnapshot.getString("subtitle");
                                    String coverUrl = documentSnapshot.getString("coverUrl");
                                    String url = documentSnapshot.getString("url");
                                    String id = documentSnapshot.getString("id");
                                    String lyrics = documentSnapshot.getString("lyrics");
                                    Long count = documentSnapshot.getLong("count");
                                    String key = documentSnapshot.getId();
                                    SongModel song = new SongModel(key, id, songTitle, subtitle, url, coverUrl, lyrics, artist, name, moviename, count);

                                    songList.add(song);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter after data change
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("PlayListMovieActivity", "Error fetching songs", e);
                    }
                });
    }

    private void downloadAllSongs() {
        for (SongModel song : songList) {
            downloadSong(song.getUrl(), song.getTitle());
        }
    }

    private void downloadSong(String songUrl, String songTitle) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(songUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, songTitle + ".mp3");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(songTitle);

        long downloadId = downloadManager.enqueue(request);

        trackDownloadProgress(downloadId);
    }

    private void trackDownloadProgress(final long downloadId) {
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;

                while (downloading) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    download.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE); // Hide the progress bar when download finishes
                                    Toast.makeText(PlayListMovieActivity.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    cursor.close();
                }
            }
        }).start();
    }


}
