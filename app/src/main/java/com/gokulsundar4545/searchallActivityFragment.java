package com.gokulsundar4545;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gokulsundar4545.dropu.AllSearch;
import com.gokulsundar4545.dropu.ArtistFullActivity;
import com.gokulsundar4545.dropu.ArtistLibararyActivity;
import com.gokulsundar4545.dropu.BarcodeScannerActivity;
import com.gokulsundar4545.dropu.FavoriteActivity;
import com.gokulsundar4545.dropu.MainActivity;
import com.gokulsundar4545.dropu.MainActivity2;
import com.gokulsundar4545.dropu.MainActivityHomeSubFragment;
import com.gokulsundar4545.dropu.MostViewActivity;
import com.gokulsundar4545.dropu.MostViewAdapter2;
import com.gokulsundar4545.dropu.Movie;
import com.gokulsundar4545.dropu.MovieAdapter;
import com.gokulsundar4545.dropu.MovieDetailActivity;
import com.gokulsundar4545.dropu.MusicsActivity;
import com.gokulsundar4545.dropu.ProfileActivity;
import com.gokulsundar4545.dropu.ProfileActivityFragment;
import com.gokulsundar4545.dropu.R;
import com.gokulsundar4545.dropu.SearchActivity;
import com.gokulsundar4545.dropu.SongModel;
import com.gokulsundar4545.dropu.TrendingActivity;
import com.gokulsundar4545.dropu.Video;
import com.gokulsundar4545.dropu.VideoAdapter;
import com.gokulsundar4545.dropu.playbackActivity;
import com.gokulsundar4545.dropu.playlistfavActivity;
import com.gokulsundar4545.dropu.searchallActivity;
import com.gokulsundar4545.dropu.songallActivity;
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

public class searchallActivityFragment extends Fragment {
    LinearLayout linearLayout2;
    private List<SongModel> songList;
    private RecyclerView recyclerView;
    private MostViewAdapter2 adapter;

    ImageView camera,back;
    CardView carproduct,carproduct2,carproduct66,carproduct266,carproduct67,carproduct26,carproduct673,carproduct263;



    RecyclerView videorecyclerView;

    private VideoAdapter movieAdapter;
    private List<Video> movieList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_searchall_activity, container, false);

        videorecyclerView=view.findViewById(R.id.videorecyclerView);
        videorecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        movieList = new ArrayList<>();
        movieAdapter = new VideoAdapter(movieList);
        videorecyclerView.setAdapter(movieAdapter);

        fetchMoviesFromFirebase();

        Recyclerview_visibility();






        carproduct=view.findViewById(R.id.carproduct);
        carproduct2=view.findViewById(R.id.carproduct2);
        carproduct66=view.findViewById(R.id.carproduct66);
        carproduct266=view.findViewById(R.id.carproduct266);
        carproduct67=view.findViewById(R.id.carproduct67);
        carproduct26=view.findViewById(R.id.carproduct26);
        carproduct673=view.findViewById(R.id.carproduct673);
        carproduct263=view.findViewById(R.id.carproduct263);

        carproduct.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct2.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct66.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct266.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct67.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct26.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct673.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
        carproduct263.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));

        carproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MovieDetailActivity.class);
                startActivity(intent);
            }
        });

        carproduct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MostViewActivity.class);
                startActivity(intent);
            }
        });
        carproduct66.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), songallActivity.class);
                startActivity(intent);
            }
        });

        carproduct266.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ArtistLibararyActivity.class);
                startActivity(intent);
            }
        });

        carproduct67.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), playbackActivity.class);
                startActivity(intent);


            }
        });

        carproduct26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), TrendingActivity.class);
                startActivity(intent);

            }
        });

        carproduct673.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), FavoriteActivity.class);
                startActivity(intent);


            }
        });

        carproduct263.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), playlistfavActivity.class);
                startActivity(intent);


            }
        });



        back=view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivityHomeSubFragment fragmentB = new MainActivityHomeSubFragment((MainActivity2) getActivity());
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentB);
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                fragmentTransaction.commit();

            }
        });



        camera=view.findViewById(R.id.imageView25);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(), BarcodeScannerActivity.class);
                startActivity(intent);

            }
        });

        linearLayout2 =view. findViewById(R.id.linearLayout2);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(), AllSearch.class);
                startActivity(intent);

            }
        });

        linearLayout2.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler));

        songList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
                                String moviename = documentSnapshot.getString("moviename");
                                SongModel song = new SongModel(key,id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name,moviename, count);

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

        TextView username1 =view.findViewById(R.id.imageView2);


        username1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivityFragment fragmentB = new ProfileActivityFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentB);
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                fragmentTransaction.commit();
            }
        });

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
                Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void checkAndDisplayToast(List<SongModel> songs) {
        if (!songs.isEmpty()) {

        }
    }






    private void fetchMoviesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("VideosongPlaylist");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Video movie = snapshot.getValue(Video.class);
                    if (movie != null) {
                        movieList.add(movie);
                    }
                }
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                try {
                    Toast.makeText(getContext(), "Failed to load movies.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }

            }
        });
    }

    private void Recyclerview_visibility(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("videorecyclerview").getValue(Boolean.class);

                if (bigSongView){
                    videorecyclerView.setVisibility(View.VISIBLE);

                }else {
                    videorecyclerView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }

}