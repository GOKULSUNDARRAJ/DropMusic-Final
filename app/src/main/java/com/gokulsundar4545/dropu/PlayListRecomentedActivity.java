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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PlayListRecomentedActivity extends AppCompatActivity {

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
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        // Use Palette API to extract dominant color
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Get the dominant color
                                int dominantColor = palette.getDominantColor(getResources().getColor(android.R.color.black));
                                // Now you have the dominant color, you can use it as needed
                                // For example, set the background color of a view
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
                                String recomented = documentSnapshot.getString("recomented");

                                // Only process songs with the given movie name
                                if (movieName.equals(recomented)) {
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
                                    Toast.makeText(PlayListRecomentedActivity.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
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
