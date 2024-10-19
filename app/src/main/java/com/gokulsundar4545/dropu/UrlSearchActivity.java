package com.gokulsundar4545.dropu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class UrlSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter6 adapter;
    private List<SongModel> songList;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_search);

        // Get URL from intent data
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            url = data.toString();
        }

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songList = new ArrayList<>();
        adapter = new SearchAdapter6(this, songList);
        recyclerView.setAdapter(adapter);

        // Set click listener for TextView to fetch data and filter

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
                                // Extract song data from Firestore document
                                String songTitle = documentSnapshot.getString("title");
                                String subtitle = documentSnapshot.getString("subtitle");
                                String coverUrl = documentSnapshot.getString("coverUrl");
                                String songUrl = documentSnapshot.getString("url");
                                String id = documentSnapshot.getString("id");
                                String lyrics = documentSnapshot.getString("lyrics");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                Long count = documentSnapshot.getLong("count");
                                String key = documentSnapshot.getId();
                                String moviename = documentSnapshot.getString("moviename");
                                // Create SongModel object
                                SongModel song = new SongModel(key, id, songTitle, subtitle, songUrl, coverUrl, lyrics,artist,name,moviename, count);
                                songList.add(song); // Add song to list
                            } else {
                                Log.d("SearchActivity", "No such document");
                            }
                        }

                        // Notify adapter after data change
                        adapter.notifyDataSetChanged();

                        // Filter songs based on the retrieved URL
                        filterSongs(url);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchActivity", "Error fetching songs", e);
                    }
                });
    }

    // Method to filter the song list based on search text (URL)
    private void filterSongs(String searchText) {
        List<SongModel> filteredList = new ArrayList<>();
        for (SongModel song : songList) {
            if (song.getUrl().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(song);
            }
        }
        adapter.filterList(filteredList); // Update adapter with filtered list
    }
}
