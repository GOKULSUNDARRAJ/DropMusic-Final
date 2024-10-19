package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PlayListAritistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter8 adapter;
    private List<SongModel> songList;
    private List<SongModel> filteredSongList;
    private EditText searchEditText;
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
        searchEditText = findViewById(R.id.searchedt);

        progressBar.setVisibility(View.INVISIBLE);

        Glide.with(this)
                .asGif()
                .load(R.drawable.downloadgif) // Replace with your GIF resource
                .into(progressBar);


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadFilteredSongs();
                download.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieName = extras.getString("MOVIE_NAME");
            movieCoverUrl = extras.getString("MOVIE_COVER_URL");
            movieTitle = extras.getString("MOVIE_TITLE");
            subTitle = extras.getString("SUB_TITLE");
        }

        ImageView categoryCoverUrl = findViewById(R.id.categoryCoverUrl);
        Picasso.get().load(movieCoverUrl).into(categoryCoverUrl);
        CollapsingToolbarLayout collaps = findViewById(R.id.collapslayput);

        androidx.coordinatorlayout.widget.CoordinatorLayout card = findViewById(R.id.card);
        Glide.with(this)
                .asBitmap()
                .load(movieCoverUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int dominantColor = palette.getDominantColor(getResources().getColor(android.R.color.black));
                                recyclerView.setBackgroundColor(dominantColor);
                                card.setBackgroundColor(dominantColor);
                                collaps.setBackgroundColor(dominantColor);
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
        filteredSongList = new ArrayList<>();
        adapter = new SearchAdapter8(this, filteredSongList);
        recyclerView.setAdapter(adapter);

        fetchData();

        // Setup search EditText to filter results
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void fetchData() {
        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        songList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String songTitle = documentSnapshot.getString("title");
                                String moviename = documentSnapshot.getString("moviename");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                if (movieName.equals(name)) {
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
                        filter(searchEditText.getText().toString()); // Initial filter
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlayListAritistActivity.this, "Failed to load songs", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filter(String text) {
        filteredSongList.clear();
        if (text.isEmpty()) {
            filteredSongList.addAll(songList);
        } else {
            for (SongModel song : songList) {
                if (song.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredSongList.add(song);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void downloadFilteredSongs() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        for (SongModel song : filteredSongList) {  // Use filteredSongList instead of songList
            Uri uri = Uri.parse(song.getUrl());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(song.getTitle());
            request.setDescription("Downloading song");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, song.getTitle() + ".mp3");

            long downloadId = downloadManager.enqueue(request);

            trackDownloadProgress(downloadId);
        }
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
                                    Toast.makeText(PlayListAritistActivity.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
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
