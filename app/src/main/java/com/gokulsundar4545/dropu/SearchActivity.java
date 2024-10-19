package com.gokulsundar4545.dropu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView2;
    private RecentlyPlayedAdapter adapter2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<SongModel> songList;
    private EditText editText;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        txt=findViewById(R.id.txt);
        editText = findViewById(R.id.search);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // If EditText has focus, hide the RecyclerView
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.GONE);
                    txt.setText("Search");
                } else {
                    // If EditText loses focus, show the RecyclerView
                    recyclerView.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    txt.setText("Recently Played");
                }
            }
        });

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        recyclerView2 = findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);

        // Fetch recently played songs
        getRecentlyPlayedSongs();






        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songList = new ArrayList<>();
        adapter = new SearchAdapter(this, songList);
        recyclerView.setAdapter(adapter);
    // Initially hide the RecyclerView

        // Add a TextWatcher to the EditText for searching
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the songList based on the user input
                filterSongs(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String key = documentSnapshot.getId();
                                String songTitle = documentSnapshot.getString("title");
                                String subtitle = documentSnapshot.getString("subtitle");
                                String coverUrl = documentSnapshot.getString("coverUrl");
                                String Url = documentSnapshot.getString("url");
                                String id = documentSnapshot.getString("id");
                                String lyrics = documentSnapshot.getString("lyrics");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                Long count = documentSnapshot.getLong("count");
                                String moviename = documentSnapshot.getString("moviename");
                                SongModel song = new SongModel(key,id, songTitle, subtitle, Url, coverUrl,lyrics,artist,name,moviename, count);

                                songList.add(song);
                            } else {
                                Log.d("SearchActivity", "No such document");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchActivity", "Error fetching songs", e);
                    }
                });
    }


    // Method to filter the song list based on search text
    private void filterSongs(String searchText) {
        List<SongModel> filteredList = new ArrayList<>();
        for (SongModel song : songList) {
            if (song.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(song);
            }
        }


            adapter.filterList(filteredList);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slid_from_left,R.anim.slid_to_right);
    }




    private void getRecentlyPlayedSongs() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRecentRef = mDatabase.child("Users").child(userId).child("recently_played");

            userRecentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<SongModel> recentlyPlayedSongs = new ArrayList<>();
                    // Process the recently played songs
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Retrieve song details from the snapshot
                        SongModel song = childSnapshot.getValue(SongModel.class);
                        if (song != null) {
                            recentlyPlayedSongs.add(song);

                        }
                    }
                    // Display recently played songs in RecyclerView
                    displayRecentlyPlayedSongs(recentlyPlayedSongs);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private void displayRecentlyPlayedSongs(List<SongModel> songs) {
        // Reverse the list of songs
        Collections.reverse(songs);

        // Create an instance of the RecentlyPlayedAdapter
        adapter2 = new RecentlyPlayedAdapter(this, songs);

        // Set the adapter to the RecyclerView
        recyclerView2.setAdapter(adapter2);
    }



}
