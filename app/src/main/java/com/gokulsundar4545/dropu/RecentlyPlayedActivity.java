package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentlyPlayedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecentlyPlayedAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ImageView imageView3;
    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_played);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Fetch recently played songs
        getRecentlyPlayedSongs();
        imageView3=findViewById(R.id.imageView3);
        textView=findViewById(R.id.textView);



        textView.startAnimation(AnimationUtils.loadAnimation(RecentlyPlayedActivity.this, R.anim.recycler2));
        imageView3.startAnimation(AnimationUtils.loadAnimation(RecentlyPlayedActivity.this, R.anim.recycler4));
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
        adapter = new RecentlyPlayedAdapter(this, songs);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    public void gotomain(View view) {
        Intent intent=new Intent(RecentlyPlayedActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
