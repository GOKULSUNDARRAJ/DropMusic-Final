package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class yourSongplaylistActivityFragment extends Fragment {
    private static final String TAG = "yourSongplaylistActivity";
    private RecyclerView recyclerView;
    private SongAdapterfinal songAdapter;
    private List<SongModel> songList = new ArrayList<>();
    private ProgressBar progressBar;
    ImageView categoryCoverUrl, download;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_songplaylist_activity, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        // Initially, set the progress bar as indeterminate and hide it
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

        download = view.findViewById(R.id.imageView5d);
        recyclerView = view.findViewById(R.id.suggesedrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songAdapter = new SongAdapterfinal(songList);
        recyclerView.setAdapter(songAdapter);
        categoryCoverUrl = view.findViewById(R.id.categoryCoverUrl);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAllSongs();
            }
        });

        // Get data from the host Activity
        Intent intent = getActivity().getIntent();
        String itemId = intent.getStringExtra("ITEM_ID");
        String name = intent.getStringExtra("Name");
        String profile = intent.getStringExtra("profile");

        com.google.android.material.appbar.CollapsingToolbarLayout collaps;
        collaps = view.findViewById(R.id.collapslayput);
        collaps.setTitle(name);

        Picasso.get().load(profile).into(categoryCoverUrl);

        if (itemId != null) {
            loadItemDetails(itemId);
            Toast.makeText(getContext(), "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "No item ID found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadItemDetails(String itemId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("playlists")
                .child("selectedSongs")
                .child(itemId);

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot: " + dataSnapshot.toString());
                songList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() instanceof String) {
                        String songString = snapshot.getValue(String.class);
                        Log.d(TAG, "Song String: " + songString);
                        // Handle the string appropriately if needed
                    } else {
                        SongModel song = snapshot.getValue(SongModel.class);
                        if (song != null) {
                            Log.d(TAG, "Song: " + song);
                            songList.add(song);
                        } else {
                            Log.e(TAG, "Failed to parse SongModel");
                        }
                    }
                }
                songAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void downloadAllSongs() {
        for (SongModel song : songList) {
            downloadSong(song.getUrl(), song.getTitle());
        }
    }

    private void downloadSong(String songUrl, String songTitle) {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(getContext().DOWNLOAD_SERVICE);
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
        final DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(getContext().DOWNLOAD_SERVICE);

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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    download.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Downloaded Successfully", Toast.LENGTH_SHORT).show();
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
