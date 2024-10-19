package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrendingSongplaylistActivity extends AppCompatActivity {

    private static final String TAG = "yourSongplaylistActivity";
    private RecyclerView recyclerView;
    private SongAdapterfinal songAdapter;
    private List<SongModel> songList = new ArrayList<>();
    private LottieAnimationView progressBar;
    ImageView categoryCoverUrl;
    FloatingActionButton download;

    private EditText searchEditText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsongplaylist);

        searchEditText = findViewById(R.id.searchedt);

        // Existing initialization code...

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });
        progressBar = findViewById(R.id.progressBar);


        progressBar.setVisibility(View.INVISIBLE);
        download=findViewById(R.id.fab);

        recyclerView = findViewById(R.id.suggesedrecyclerview); // Assuming you have a RecyclerView with this ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapterfinal(songList);
        recyclerView.setAdapter(songAdapter);
        categoryCoverUrl=findViewById(R.id.categoryCoverUrl);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAllSongs();
                download.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        Intent intent = getIntent();
        String itemId = intent.getStringExtra("ITEM_ID");
        String name = intent.getStringExtra("Name");
        String profile = intent.getStringExtra("profile");

        com.google.android.material.appbar.CollapsingToolbarLayout collaps;
        collaps = findViewById(R.id.collapslayput);
        collaps.setTitle(name);

        Picasso.get().load(profile).into(categoryCoverUrl);


        if (itemId != null) {
            loadItemDetails(itemId);
            Toast.makeText(this, "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No item ID found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadItemDetails(String itemId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("")
                .child("playlists")
                .child("selectedSongs")
                .child(itemId);

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot: " + dataSnapshot.toString()); // Log the entire snapshot
                songList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Check if the data is a String or a SongModel object
                    if (snapshot.getValue() instanceof String) {
                        String songString = snapshot.getValue(String.class);
                        Log.d(TAG, "Song String: " + songString); // Log the string value
                        // Handle the string appropriately if needed
                    } else {
                        SongModel song = snapshot.getValue(SongModel.class);
                        if (song != null) {
                            Log.d(TAG, "Song: " + song); // Log each song



                            songList.add(song);
                        } else {
                            Log.e(TAG, "Failed to parse SongModel");
                        }
                    }


                }
                songAdapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage()); // Log errors
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
                                    Toast.makeText(TrendingSongplaylistActivity.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    cursor.close();
                }
            }
        }).start();
    }

    private void filter(String text) {
        List<SongModel> filteredList = new ArrayList<>();
        for (SongModel song : songList) {
            if (song.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(song);
            }
        }
        songAdapter.filterList(filteredList);
    }
}
