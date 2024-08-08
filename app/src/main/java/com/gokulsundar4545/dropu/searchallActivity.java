package com.gokulsundar4545.dropu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class searchallActivity extends AppCompatActivity {

    LinearLayout linearLayout2;
    private List<SongModel> songList;
    private RecyclerView recyclerView;
    private MostViewAdapter2 adapter;

    ImageView camera,back;
    CardView carproduct,carproduct2,carproduct66,carproduct266,carproduct67,carproduct26,carproduct673,carproduct263;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchall);

        carproduct=findViewById(R.id.carproduct);
        carproduct2=findViewById(R.id.carproduct2);
        carproduct66=findViewById(R.id.carproduct66);
        carproduct266=findViewById(R.id.carproduct266);
        carproduct67=findViewById(R.id.carproduct67);
        carproduct26=findViewById(R.id.carproduct26);
        carproduct673=findViewById(R.id.carproduct673);
        carproduct263=findViewById(R.id.carproduct263);

        carproduct.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct2.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct66.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct266.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct67.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct26.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct673.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));
        carproduct263.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler2));

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        camera=findViewById(R.id.imageView25);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(), BarcodeScannerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(), SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayout2.startAnimation(AnimationUtils.loadAnimation(searchallActivity.this,R.anim.recycler));

        songList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(searchallActivity.this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MostViewAdapter2(songList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<SongModel> updatedList = new ArrayList<>();
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
                                SongModel song = new SongModel(key,id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name, count);

                                if (count != null && count > 5) {
                                    updatedList.add(song);
                                }
                            } else {
                                Log.d("MostViewActivity", "No such document");
                            }
                        }
                        adapter.updateSongList(updatedList);
                        checkAndDisplayToast(updatedList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MostViewActivity", "Error fetching songs", e);
                    }
                });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        TextView username1 = findViewById(R.id.imageView2);

        firebaseAuth = FirebaseAuth.getInstance();

        String currentUserUid1 = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        // Retrieve the user data from the Realtime Database
        databaseReference.child(currentUserUid1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);
                    // Set the username in the TextView
                    username1.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(searchallActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndDisplayToast(List<SongModel> songs) {
        if (!songs.isEmpty()) {

        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent=new Intent(searchallActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoTrending(View view) {
        Intent intent=new Intent(view.getContext(), TrendingActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotomusics(View view) {
        Intent intent=new Intent(view.getContext(), MusicsActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotomost(View view) {
        Intent intent=new Intent(view.getContext(), MostViewActivity.class);
        startActivity(intent);
        finish();


    }

    public void gotoartist2(View view) {

        Intent intent=new Intent(view.getContext(), ArtistFullActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotosongs(View view) {

        Intent intent=new Intent(view.getContext(), songallActivity.class);
        startActivity(intent);
        finish();

    }

    public void gotoplayback(View view) {
        Intent intent=new Intent(view.getContext(), playbackActivity.class);
        startActivity(intent);
        finish();


    }

    public void gotofav(View view) {
        Intent intent=new Intent(view.getContext(), FavoriteActivity.class);
        startActivity(intent);
        finish();


    }

    public void gotoplaylist(View view) {
        Intent intent=new Intent(view.getContext(), playlistfavActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoprofile(View view) {

        Intent intent=new Intent(view.getContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }



}


