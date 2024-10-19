package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    LinearLayout recentlylayout, actresslayout, trend1playlistlayout, playlist2layout, favlayout, layout3, layout4, layout5, layout6, layout7, layout8, layout9, layout10;


    private List<SongModel> songList;
    private RecyclerView recyclerView;
    private MostViewAdapter5 adapter;

    private RecyclerView recyclerView45;
    private RecentlyPlayedAdapter45 adapter45;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView recyclerView34;
    private PlaylistAdapter45 adapter34;
    private List<SongModel> favoritesList34;

    RecyclerView artistrecyclerview;
    private ArtistnewAdapter movieAdapter;
    private List<Artist> movieList;



    private RecyclerView recyclerViewplaylist;
    private PlaylistAdapter13 adapterplay;
    private List<PlaylistItem> playlistItems;


    private RecyclerView recyclerViewplaylist3;
    private PlaylistAdapter14 adapterplay3;
    private List<PlaylistItem> playlistItems3;


    private RecyclerView recyclerViewplaylist4;
    private PlaylistAdapter14 adapterplay4;
    private List<PlaylistItem> playlistItems4;


    private RecyclerView recyclerViewplaylist5;
    private PlaylistAdapter14 adapterplay5;
    private List<PlaylistItem> playlistItems5;

    private RecyclerView recyclerViewplaylist6;
    private PlaylistAdapter14 adapterplay6;
    private List<PlaylistItem> playlistItems6;


    private RecyclerView recyclerViewplaylist7;
    private PlaylistAdapter14 adapterplay7;
    private List<PlaylistItem> playlistItems7;

    private RecyclerView recyclerViewplaylist8;
    private PlaylistAdapter14 adapterplay8;
    private List<PlaylistItem> playlistItems8;

    private RecyclerView recyclerViewplaylist9;
    private PlaylistAdapter14 adapterplay9;
    private List<PlaylistItem> playlistItems9;

    private RecyclerView recyclerViewplaylist10;
    private PlaylistAdapter14 adapterplay10;
    private List<PlaylistItem> playlistItems10;

    private RecyclerView recyclerViewplaylist11;
    private PlaylistAdapter14 adapterplay11;
    private List<PlaylistItem> playlistItems11;


    TextView title1, title2, title3, title4, title5, title6, title7, title8, title9, title10;
    TextView subtitle1, subtitle2, subtitle3, subtitle4, subtitle5, subtitle6, subtitle7, subtitle8, subtitle9, subtitle10;
    ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10;

    private ShimmerFrameLayout shimmerFrameLayout;

    LinearLayout layout1,layout2,layout33;

    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        layout1=view.findViewById(R.id.layout1);
        layout2=view.findViewById(R.id.layout2);
        layout33=view.findViewById(R.id.layout3);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        if (shimmerFrameLayout != null) {
            // Start shimmer effect if it's not null
            shimmerFrameLayout.startShimmer();

            // Optional: Customize shimmer properties (speed, direction, etc.)
            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT) // Customize direction
                    .setDuration(1500) // Customize duration (in ms)
                    .build();
            shimmerFrameLayout.setShimmer(shimmer);
        } else {
            // Log an error if the shimmer layout was not found
            Log.e("ShimmerFragment", "ShimmerFrameLayout not found in layout");
        }
        recentlylayout = view.findViewById(R.id.recentlylayout);
        actresslayout = view.findViewById(R.id.actresslayout);
        trend1playlistlayout = view.findViewById(R.id.trend1playlistlayout);
        playlist2layout = view.findViewById(R.id.playlist2layout);
        favlayout = view.findViewById(R.id.favlayout);
        layout3 = view.findViewById(R.id.layout3);
        layout4 = view.findViewById(R.id.layout4);
        layout5 = view.findViewById(R.id.layout5);
        layout6 = view.findViewById(R.id.layout6);
        layout7 = view.findViewById(R.id.layout7);
        layout8 = view.findViewById(R.id.layout8);
        layout9 = view.findViewById(R.id.layout9);
        layout10 = view.findViewById(R.id.layout10);

        title1 = view.findViewById(R.id.title11);
        title2 = view.findViewById(R.id.title22);
        title3 = view.findViewById(R.id.title33);
        title4 = view.findViewById(R.id.title44);
        title5 = view.findViewById(R.id.title55);
        title6 = view.findViewById(R.id.title66);
        title7 = view.findViewById(R.id.title77);
        title8 = view.findViewById(R.id.title88);
        title9 = view.findViewById(R.id.title99);
        title10 = view.findViewById(R.id.title100);

        subtitle1 = view.findViewById(R.id.sub11);
        subtitle2 = view.findViewById(R.id.sub22);
        subtitle3 = view.findViewById(R.id.sub33);
        subtitle4 = view.findViewById(R.id.sub44);
        subtitle5 = view.findViewById(R.id.sub55);
        subtitle6 = view.findViewById(R.id.sub66);
        subtitle7 = view.findViewById(R.id.sub77);
        subtitle8 = view.findViewById(R.id.sub88);
        subtitle9 = view.findViewById(R.id.sub99);
        subtitle10 = view.findViewById(R.id.sub100);


        image1 = view.findViewById(R.id.image11);
        image2 = view.findViewById(R.id.image22);
        image3 = view.findViewById(R.id.image33);
        image4 = view.findViewById(R.id.image44);
        image5 = view.findViewById(R.id.image55);
        image6 = view.findViewById(R.id.image66);
        image7 = view.findViewById(R.id.image77);
        image8 = view.findViewById(R.id.image88);
        image9 = view.findViewById(R.id.image99);
        image10 = view.findViewById(R.id.image100);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        songList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.mostlyviewedrecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MostViewAdapter5(songList);
        recyclerView.setAdapter(adapter);


        recyclerView45 = view.findViewById(R.id.recentlypalyedrecyclerview);
        LinearLayoutManager layoutManager45 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView45.setLayoutManager(layoutManager45);


        recyclerView34 = view.findViewById(R.id.favorityrecyclerview);
        recyclerView34.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        favoritesList34 = new ArrayList<>();
        adapter34 = new PlaylistAdapter45(favoritesList34);
        recyclerView34.setAdapter(adapter34);


        artistrecyclerview = view.findViewById(R.id.Actressrecyclerview);
        artistrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        movieList = new ArrayList<>();
        movieAdapter = new ArtistnewAdapter(movieList);
        artistrecyclerview.setAdapter(movieAdapter);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesFromFirebase();
                getRecentlyPlayedSongs();
                MostViewedPlaylist();
                fetchFavorites();

                loadPlaylistData();
                loadPlaylistData2();
                loadPlaylistData3();
                loadPlaylistData4();
                loadPlaylistData5();
                loadPlaylistData6();
                loadPlaylistData7();
                loadPlaylistData8();
                loadPlaylistData9();
                loadPlaylistData10();
            }
        });


        recyclerViewplaylist = view.findViewById(R.id.trendingyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist.setLayoutManager(layoutManager);
        playlistItems = new ArrayList<>();
        adapterplay = new PlaylistAdapter13(getContext(), playlistItems);
        recyclerViewplaylist.setAdapter(adapterplay);


        recyclerViewplaylist3 = view.findViewById(R.id.trendingyclerview2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist3.setLayoutManager(layoutManager2);
        playlistItems3 = new ArrayList<>();
        adapterplay3 = new PlaylistAdapter14(getContext(), playlistItems3);
        recyclerViewplaylist3.setAdapter(adapterplay3);


        recyclerViewplaylist4 = view.findViewById(R.id.playlist3eclerview);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist4.setLayoutManager(layoutManager4);
        playlistItems4 = new ArrayList<>();
        adapterplay4 = new PlaylistAdapter14(getContext(), playlistItems4);
        recyclerViewplaylist4.setAdapter(adapterplay4);

        recyclerViewplaylist5 = view.findViewById(R.id.playlist5eclerview);
        LinearLayoutManager layoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist5.setLayoutManager(layoutManager5);
        playlistItems5 = new ArrayList<>();
        adapterplay5 = new PlaylistAdapter14(getContext(), playlistItems5);
        recyclerViewplaylist5.setAdapter(adapterplay5);

        recyclerViewplaylist6 = view.findViewById(R.id.playlist6eclerview);
        LinearLayoutManager layoutManager6 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist6.setLayoutManager(layoutManager6);
        playlistItems6 = new ArrayList<>();
        adapterplay6 = new PlaylistAdapter14(getContext(), playlistItems6);
        recyclerViewplaylist6.setAdapter(adapterplay6);

        recyclerViewplaylist7 = view.findViewById(R.id.playlist7eclerview);
        LinearLayoutManager layoutManager7 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist7.setLayoutManager(layoutManager7);
        playlistItems7 = new ArrayList<>();
        adapterplay7 = new PlaylistAdapter14(getContext(), playlistItems7);
        recyclerViewplaylist7.setAdapter(adapterplay7);

        recyclerViewplaylist8 = view.findViewById(R.id.playlist8eclerview);
        LinearLayoutManager layoutManager8 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist8.setLayoutManager(layoutManager8);
        playlistItems8 = new ArrayList<>();
        adapterplay8 = new PlaylistAdapter14(getContext(), playlistItems8);
        recyclerViewplaylist8.setAdapter(adapterplay8);


        recyclerViewplaylist9 = view.findViewById(R.id.playlist9eclerview);
        LinearLayoutManager layoutManager9 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist9.setLayoutManager(layoutManager9);
        playlistItems9 = new ArrayList<>();
        adapterplay9 = new PlaylistAdapter14(getContext(), playlistItems9);
        recyclerViewplaylist9.setAdapter(adapterplay9);


        recyclerViewplaylist10 = view.findViewById(R.id.playlist10eclerview);
        LinearLayoutManager layoutManager10 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist10.setLayoutManager(layoutManager10);
        playlistItems10 = new ArrayList<>();
        adapterplay10 = new PlaylistAdapter14(getContext(), playlistItems10);
        recyclerViewplaylist10.setAdapter(adapterplay10);


        recyclerViewplaylist11 = view.findViewById(R.id.playlist11eclerview);
        LinearLayoutManager layoutManager11 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist11.setLayoutManager(layoutManager11);
        playlistItems11 = new ArrayList<>();
        adapterplay11 = new PlaylistAdapter14(getContext(), playlistItems11);
        recyclerViewplaylist11.setAdapter(adapterplay11);

        loadPlaylistData();
        loadPlaylistData2();
        loadPlaylistData3();
        loadPlaylistData4();
        loadPlaylistData5();
        loadPlaylistData6();
        loadPlaylistData7();
        loadPlaylistData8();
        loadPlaylistData9();
        loadPlaylistData10();

        fetchMoviesFromFirebase();
        getRecentlyPlayedSongs();
        MostViewedPlaylist();
        fetchFavorites();


        gettitle1();
        gettitle2();
        gettitle3();
        gettitle4();
        gettitle5();
        gettitle6();
        gettitle7();
        gettitle8();
        gettitle9();
        gettitle10();


        getsubtext1();
        getsubtext2();
        getsubtext3();
        getsubtext4();
        getsubtext5();
        getsubtext6();
        getsubtext7();
        getsubtext8();
        getsubtext9();
        getsubtext10();




        getimage1();
        getimage2();
        getimage3();
        getimage4();
        getimage5();
        getimage6();
        getimage7();
        getimage8();
        getimage9();
        getimage10();


        return view;
    }

    private void MostViewedPlaylist() {
        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<SongModel> updatedList = new ArrayList<>();
                    int count = 0; // Counter to limit the number of items to 8
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (count >= 8) { // If we already have 8 items, break the loop
                            break;
                        }
                        // Extract data from Firestore document
                        String key = documentSnapshot.getId();
                        String songTitle = documentSnapshot.getString("title");
                        String subtitle = documentSnapshot.getString("subtitle");
                        String coverUrl = documentSnapshot.getString("coverUrl");
                        String Url = documentSnapshot.getString("url");
                        String id = documentSnapshot.getString("id");
                        String lyrics = documentSnapshot.getString("lyrics");
                        String artist = documentSnapshot.getString("artist");
                        String name = documentSnapshot.getString("name");
                        Long countValue = documentSnapshot.getLong("count");
                        String moviename = documentSnapshot.getString("moviename");
                        if (countValue != null && countValue > 5) { // Check count condition
                            SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics, artist, name, moviename, countValue);
                            updatedList.add(song);
                            count++; // Increment the counter
                        }
                    }
                    // Update adapter with filtered list
                    Collections.reverse(updatedList);
                    adapter.updateSongList(updatedList);

                    layout1.setVisibility(View.INVISIBLE);


                    if (updatedList.isEmpty()) {

                    } else {

                    }

                    swipeRefreshLayout.setRefreshing(false);

                })
                .addOnFailureListener(e -> Log.e("MostViewActivity", "Error fetching songs", e));
    }

    private void getRecentlyPlayedSongs() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        try {
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
                                // Check if a song with the same title already exists
                                boolean songExists = false;
                                for (SongModel existingSong : recentlyPlayedSongs) {

                                    try {
                                        if (existingSong.getId().equals(song.getId())) {
                                            // Update the existing song details
                                            existingSong.setCount(song.getCount());
                                            existingSong.setCoverUrl(song.getCoverUrl());
                                            existingSong.setLyrics(song.getLyrics());
                                            existingSong.setSubtitle(song.getSubtitle());
                                            existingSong.setTitle(song.getTitle());
                                            existingSong.setUrl(song.getUrl());
                                            // You can update other attributes as needed
                                            songExists = true;
                                            break;
                                        }
                                    } catch (Exception e) {

                                    }

                                }
                                if (!songExists) {
                                    recentlyPlayedSongs.add(song);
                                }
                            }
                        }
                        // Display recently played songs in RecyclerView
                        displayRecentlyPlayedSongs(recentlyPlayedSongs);
                        swipeRefreshLayout.setRefreshing(false);
                        layout2.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        } catch (Exception e) {

        }

    }

    private void displayRecentlyPlayedSongs(List<SongModel> songs) {
        // Reverse the list of songs
        Collections.reverse(songs);

        if (songs.isEmpty()) {

            recentlylayout.setVisibility(View.GONE);

        } else {
            // Create an instance of the RecentlyPlayedAdapter
            adapter45 = new RecentlyPlayedAdapter45(getContext(), songs);

            recentlylayout.setVisibility(View.VISIBLE);

            // Set the adapter to the RecyclerView
            recyclerView45.setAdapter(adapter45);
        }
    }

    private void fetchFavorites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("playlist");

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoritesList34.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SongModel song = snapshot.getValue(SongModel.class);
                    favoritesList34.add(song);
                }
                if (favoritesList34.isEmpty()) {

                    favlayout.setVisibility(View.GONE);
                } else {
                    adapter34.setFavoritesList(favoritesList34); // Update adapter data
                    swipeRefreshLayout.setRefreshing(false);
                    favlayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    private void fetchMoviesFromFirebase() {

        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtistPlaylist");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    movieList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Artist movie = snapshot.getValue(Artist.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    }
                    movieAdapter.notifyDataSetChanged();

                    if (movieList.isEmpty()) {
                        actresslayout.setVisibility(View.GONE);
                    } else {
                        actresslayout.setVisibility(View.VISIBLE);
                    }

                    layout3.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.stopShimmer();

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    try {
                        Toast.makeText(getContext(), "Failed to load movies.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }

                }
            });
        } catch (Exception e) {

        }

    }


    private void loadPlaylistData() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
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

                if (playlistItems.isEmpty()) {
                    trend1playlistlayout.setVisibility(View.GONE);
                } else {
                    trend1playlistlayout.setVisibility(View.VISIBLE);
                }
                adapterplay.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData2() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists2")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems3.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems3.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems3.isEmpty()) {
                    playlist2layout.setVisibility(View.GONE);
                } else {
                    playlist2layout.setVisibility(View.VISIBLE);
                }
                adapterplay3.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }


    private void loadPlaylistData3() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists3")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems4.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems4.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems4.isEmpty()) {
                    layout3.setVisibility(View.GONE);
                } else {
                    layout3.setVisibility(View.VISIBLE);
                }
                adapterplay4.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData4() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists4")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems5.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems5.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems5.isEmpty()) {
                    layout4.setVisibility(View.GONE);
                } else {
                    layout4.setVisibility(View.VISIBLE);
                }
                adapterplay5.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData5() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists5")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems6.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems6.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems6.isEmpty()) {
                    layout5.setVisibility(View.GONE);
                } else {
                    layout5.setVisibility(View.VISIBLE);
                }
                adapterplay6.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData6() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists6")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems7.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems7.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }
                if (playlistItems7.isEmpty()) {
                    layout6.setVisibility(View.GONE);
                } else {
                    layout6.setVisibility(View.VISIBLE);
                }
                adapterplay7.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData7() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists7")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems8.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems8.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems8.isEmpty()) {
                    layout7.setVisibility(View.GONE);
                } else {
                    layout7.setVisibility(View.VISIBLE);
                }
                adapterplay8.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData8() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists8")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems9.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems9.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems9.isEmpty()) {
                    layout8.setVisibility(View.GONE);
                } else {
                    layout8.setVisibility(View.VISIBLE);
                }
                adapterplay9.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData9() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists9")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems10.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems10.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems10.isEmpty()) {
                    layout9.setVisibility(View.GONE);
                } else {
                    layout9.setVisibility(View.VISIBLE);
                }
                adapterplay10.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadPlaylistData10() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference()
                .child("playlists10")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems11.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems11.add(item);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                if (playlistItems11.isEmpty()) {
                    layout10.setVisibility(View.GONE);
                } else {
                    layout10.setVisibility(View.VISIBLE);
                }
                adapterplay11.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }


    private void gettitle1() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title1");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title1.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle2() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title2");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title2.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle3() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title3");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title3.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle4() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title4");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title4.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle5() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title5");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title5.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle6() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title6");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title6.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle7() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title7");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title7.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle8() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title8");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title8.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle9() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title9");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title9.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void gettitle10() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagetitle").child("title10");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                title10.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }




    private void getsubtext1() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle1");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (title.isEmpty()){
                    subtitle1.setVisibility(View.GONE);
                }else {
                    subtitle1.setText(title);
                    subtitle1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext2() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle2");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle2.setVisibility(View.GONE);
                }else {
                    subtitle2.setText(title);
                    subtitle2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }


    private void getsubtext3() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle3");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle3.setVisibility(View.GONE);
                }else {
                    subtitle3.setText(title);
                    subtitle3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext4() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle4");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle4.setVisibility(View.GONE);
                }else {
                    subtitle4.setText(title);
                    subtitle4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext5() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle5");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (title.isEmpty()){
                    subtitle5.setVisibility(View.GONE);
                }else {
                    subtitle5.setText(title);
                    subtitle5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext6() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle6");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle6.setVisibility(View.GONE);
                }else {
                    subtitle6.setText(title);
                    subtitle6.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext7() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle7");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle7.setVisibility(View.GONE);
                }else {
                    subtitle7.setText(title);
                    subtitle7.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext8() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle8");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle8.setVisibility(View.GONE);
                }else {
                    subtitle8.setText(title);
                    subtitle8.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext9() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle9");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle9.setVisibility(View.GONE);
                }else {
                    subtitle9.setText(title);
                    subtitle9.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getsubtext10() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePagesubtitle").child("subtitle10");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (title.isEmpty()){
                    subtitle10.setVisibility(View.GONE);
                }else {
                    subtitle10.setText(title);
                    subtitle10.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }






    private void getimage1() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image1");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (url.isEmpty()){
                    image1.setVisibility(View.GONE);
                }else {
                    image1.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage2() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image2");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (url.isEmpty()){
                    image2.setVisibility(View.GONE);
                }else {
                    image2.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage3() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image3");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (url.isEmpty()){
                    image3.setVisibility(View.GONE);
                }else {
                    image3.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage4() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image4");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (url.isEmpty()){
                    image4.setVisibility(View.GONE);
                }else {
                    image4.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image4);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage5() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image5");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (url.isEmpty()){
                    image5.setVisibility(View.GONE);
                }else {
                    image5.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image5);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage6() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image6");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (url.isEmpty()){
                    image6.setVisibility(View.GONE);
                }else {
                    image6.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image6);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage7() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image7");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value
                if (url.isEmpty()){
                    image7.setVisibility(View.GONE);
                }else {
                    image7.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image7);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage8() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image8");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (url.isEmpty()){
                    image8.setVisibility(View.GONE);
                }else {
                    image8.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image8);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage9() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image9");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value

                if (url.isEmpty()){
                    image9.setVisibility(View.GONE);
                }else {
                    image9.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image9);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }

    private void getimage10() {
        DatabaseReference databaseReference;


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("HomePageimage").child("image10");

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);  // Get the title1 value


                if (url.isEmpty()){
                    image10.setVisibility(View.GONE);
                }else {
                    image10.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(image10);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any error here
            }
        });


    }


}