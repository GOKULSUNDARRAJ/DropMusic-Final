package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectPlayListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchAdapter25 adapter;
    private List<SongModel> songList;
    private EditText editText;
    private SwipeRefreshLayout swipeRefreshLayout;

    ImageView back;


    TextView textView;

ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainselectplaylist);


        String imageUriString = getIntent().getStringExtra("imageUri");
        String imageText = getIntent().getStringExtra("imageText");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);

           // Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
        }

        progressBar=findViewById(R.id.progressbar56);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        textView=findViewById(R.id.Next);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCurrentUserToFirebase();

                textView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        editText = findViewById(R.id.search);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        songList = new ArrayList<>();
        adapter = new SearchAdapter25(this, songList);
        recyclerView.setAdapter(adapter);

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

        // Set up swipe-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform data refresh operations here
                fetchData();
            }
        });

        // Fetch data initially
        fetchData();

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
                                SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name,moviename, count);

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
            if (song.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(song);
            }
        }

        adapter.filterList(filteredList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SelectPlayListActivity.this, MainActivity2.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void sendCurrentUserToFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users");
        DatabaseReference playlistRef = userRef.child(currentUserId).child("playlists").child("selectedSongs");

        String imageUriString = getIntent().getStringExtra("imageUri");
        String imageText = getIntent().getStringExtra("imageText");

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("playlist_covers/" + System.currentTimeMillis() + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            Map<String, Object> playlistData = new HashMap<>();
                            playlistData.put("coverImage", downloadUrl);
                            playlistData.put("coverName", imageText != null ? imageText : "");

                            for (int selectedItem : adapter.getSelectedItems()) {
                                SongModel song = songList.get(selectedItem);
                                String randomKey = playlistRef.push().getKey();
                                playlistData.put(randomKey, song);
                            }

                            savePlaylistToFirebase(playlistRef, playlistData);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(SelectPlayListActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SelectPlayListActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Map<String, Object> playlistData = new HashMap<>();
            playlistData.put("coverImage", ""); // Empty string if no image
            playlistData.put("coverName", imageText != null ? imageText : "");

            for (int selectedItem : adapter.getSelectedItems()) {
                SongModel song = songList.get(selectedItem);
                String randomKey = playlistRef.push().getKey();
                playlistData.put(randomKey, song);
            }

            savePlaylistToFirebase(playlistRef, playlistData);
        }
    }


    private void savePlaylistToFirebase(DatabaseReference playlistRef, Map<String, Object> selectedSongs) {
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nextIndex = 1;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        try {
                            int index = Integer.parseInt(key);
                            if (index >= nextIndex) {
                                nextIndex = index + 1;
                            }
                        } catch (NumberFormatException e) {
                            // Handle cases where the key is not an integer
                        }
                    }
                }

                DatabaseReference newPlaylistRef = playlistRef.child(String.valueOf(nextIndex));
                newPlaylistRef.setValue(selectedSongs)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(SelectPlayListActivity.this, "Songs added playlist successfully", Toast.LENGTH_SHORT).show();
                            textView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(SelectPlayListActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                            textView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SelectPlayListActivity.this, "Failed to check existing data", Toast.LENGTH_SHORT).show();
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



}