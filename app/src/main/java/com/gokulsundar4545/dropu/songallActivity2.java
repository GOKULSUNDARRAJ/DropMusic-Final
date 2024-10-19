package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class songallActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter3 adapter;
    private List<SongModel> songList;

    TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchall2_layout);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        String barcodeValue = getIntent().getStringExtra("BARCODE_VALUE_EXTRA");



        textView =findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSongs(barcodeValue);
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        songList = new ArrayList<>();
        adapter = new SearchAdapter3(this, songList);
        recyclerView.setAdapter(adapter);




        // Set up swipe-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform data refresh operations here
                fetchData();
            }
        });


    }

    // Method to fetch data from Firestore
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
                                String subtitle = documentSnapshot.getString("subtitle");
                                String coverUrl = documentSnapshot.getString("coverUrl");
                                String Url = documentSnapshot.getString("url");
                                String id = documentSnapshot.getString("id");
                                String lyrics = documentSnapshot.getString("lyrics");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                Long count = documentSnapshot.getLong("count");
                                String key = documentSnapshot.getId();
                                String moviename = documentSnapshot.getString("moviename");
                                SongModel song = new SongModel(key,id, songTitle, subtitle, Url, coverUrl,lyrics,artist,name,moviename, count);

                                songList.add(song);
                            } else {
                                Log.d("SearchActivity", "No such document");
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter after data change
                        swipeRefreshLayout.setRefreshing(false); // Stop refresh animation
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchActivity", "Error fetching songs", e);
                        swipeRefreshLayout.setRefreshing(false); // Stop refresh animation in case of failure
                    }
                });
    }

    // Method to filter the song list based on search text
    private void filterSongs(String searchText) {
        List<SongModel> filteredList = new ArrayList<>();
        for (SongModel song : songList) {
            if (song.getUrl().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(song);
            }
        }

        adapter.filterList(filteredList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_from_left, R.anim.slid_to_right);
    }
}
