package com.gokulsundar4545.dropu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SongPlaylistActivity extends AppCompatActivity {
    String position1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songplaylist);

        Intent intent = getIntent();
        String coverName = intent.getStringExtra("coverName"); // Currently unused
        String coverImage = intent.getStringExtra("coverImage"); // Currently unused
        position1 = intent.getStringExtra("position");

        if (position1 != null) {
            // Ensure you are setting the value at the correct path if necessary
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("playlists").child("selectedSongs").setValue("1");

            loadPlaylistsFromFirebase(position1);
        } else {
            Toast.makeText(this, "Position is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPlaylistsFromFirebase(String position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users").child(currentUserId).child("playlists").child("selectedSongs").child(position);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Playlist> playlistList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String playlistId = snapshot.getKey();
                    playlistList.add(new Playlist(playlistId));
                }

                // Exclude the last two items if there are more than two items
                if (playlistList.size() > 2) {
                    playlistList = playlistList.subList(0, playlistList.size() - 2);
                }

                // Setup RecyclerView
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(SongPlaylistActivity.this));
                PlaylistAdapter26 adapter = new PlaylistAdapter26(getApplicationContext(),playlistList,position1);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SongPlaylistActivity.this, "Failed to load playlist IDs", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
