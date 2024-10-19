package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class playlistfavActivity extends AppCompatActivity {


    ImageView imageView3;

    private RecyclerView recyclerViewplaylist;
    private PlaylistAdapter24 adapterplay;
    private List<PlaylistItem> playlistItems;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        imageView3=findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), searchallActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RecyclerView recyclerViewplaylist = findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 is the number of columns
        recyclerViewplaylist.setLayoutManager(gridLayoutManager);

// Initialize playlistItems list
        playlistItems = new ArrayList<>();

// Initialize the adapter with the playlistItems list
        adapterplay = new PlaylistAdapter24(playlistfavActivity.this, playlistItems);

// Set the adapter to the RecyclerView
        recyclerViewplaylist.setAdapter(adapterplay);

        loadPlaylistData();


    }

    private void loadPlaylistData() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("playlists")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }
                adapterplay.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                Toast.makeText(playlistfavActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
