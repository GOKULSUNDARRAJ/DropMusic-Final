package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainHomeFragment extends Fragment {


    RelativeLayout recentltpayedlayout,favaritylayout;




    private RecyclerView recyclerView5;
    private SearchAdapter5 adapter5;
    private List<SongModel> songList5;


    ImageView play;



    private RecyclerView recyclerView34;
    private PlaylistAdapter45 adapter34;
    private List<SongModel> favoritesList34;




    TextView texttitle,texttitle2;

    private RecyclerView recyclerView45;
    private RecentlyPlayedAdapter45 adapter45;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;




    private List<SongModel> songList;
    private RecyclerView recyclerView;
    private MostViewAdapter5 adapter;


    ConstraintLayout const12;




    private TextView username1;
    private RecyclerView categoriesRecyclerView;

    CircleImageView image4recyclerview;

    LinearLayout linearLayout16,linearLayout1654;
    TextView menu;

    static  final  float Endscale=0.7f;


    LottieAnimationView lottieAnimationView;

    DrawerLayout drawerlayout;

    TextView seeall;

    RelativeLayout contentView;



    TextView mosttxt,mosttxt2;


    TextView textView3;


    private ProgressBar volumeProgressBar;


    SwipeRefreshLayout swipeRefreshLayout;

    private ExoPlayer exoPlayer;


    private Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onIsPlayingChanged(boolean isPlaying) {

        }
    };


    ProgressBar progressBar2;

    CardView carproduct;
    LinearLayout linearLayoutPodcast;
    RelativeLayout toolbar;

    TextView imageView2;
    Button gotoplaylist,add;

    CardView carproduct23;
    ProgressBar progressbar56;
    TextView artisttext;
    RelativeLayout player_view;

    ImageView chatactivity;

    RecyclerView artistrecyclerview;

    TextView section_5_title00;
    private ArtistnewAdapter movieAdapter;
    private List<Artist> movieList;
    private ShimmerFrameLayout shimmerFrameLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mainactiviyfragmant, container, false);



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



        section_5_title00=view.findViewById(R.id.section_5_title00);
        recentltpayedlayout=view.findViewById(R.id.most4);
        favaritylayout=view.findViewById(R.id.most45);



        Recyclerview_visibility();
        Recyclerview_visibility_mostlyvied();
        Recyclerview_visibility_Recyctlyplayed();
        Recyclerview_visibility_favlayout();
        Recyclerview_visibility_artist();





        artistrecyclerview=view.findViewById(R.id.artistrecyclerview);

        artistrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        movieList = new ArrayList<>();
        movieAdapter = new ArtistnewAdapter(movieList);
        artistrecyclerview.setAdapter(movieAdapter);

        fetchMoviesFromFirebase();














        drawerlayout=view.findViewById(R.id.drawer_layout);
        contentView=view.findViewById(R.id.content);

        player_view=view.findViewById(R.id.player_view);










        image4recyclerview=view.findViewById(R.id.profile);



        carproduct23=view.findViewById(R.id.carproduct2);

        carproduct=view.findViewById(R.id.carproduct);

        toolbar=view.findViewById(R.id.toolbar);
        imageView2=view.findViewById(R.id.imageView2);


        const12=view.findViewById(R.id.const12);
        play=view.findViewById(R.id.play);


        toolbar.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));


        recyclerView5 = view.findViewById(R.id.section_5_recycler_view00);
        recyclerView5.setLayoutManager(new LinearLayoutManager(getContext()));

        songList5 = new ArrayList<>();
        adapter5 = new SearchAdapter5(getContext(), songList5);
        recyclerView5.setAdapter(adapter5);

        fetchData();
        textView3=view.findViewById(R.id.textView3);
        texttitle=view.findViewById(R.id.texttitle);
        texttitle2=view.findViewById(R.id.texttitle2);

        recyclerView34 =view. findViewById(R.id.mostrecyclerview54);
        recyclerView34.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritesList34 = new ArrayList<>();
        adapter34 = new PlaylistAdapter45(favoritesList34);
        recyclerView34.setAdapter(adapter34);

        // Fetch the user's favorites from Firebase
        fetchFavorites();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Initialize RecyclerView
        recyclerView45 = view.findViewById(R.id.mostrecyclerview5);
        LinearLayoutManager layoutManager45 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView45.setLayoutManager(layoutManager45);

        // Fetch recently played songs
        getRecentlyPlayedSongs();

        linearLayoutPodcast = view.findViewById(R.id.linearLayout165);

        linearLayoutPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MovieDetailActivity.class);
                startActivity(intent);

                SearchnewFragment fragmentB = new SearchnewFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentB);
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                fragmentTransaction.commit();

            }
        });
        mosttxt=view.findViewById(R.id.mosttxt);
        mosttxt.setText("Trending Songs .");

        mosttxt2=view.findViewById(R.id.mosttxt2);

        mosttxt2.setText("See all");

        mosttxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),MostViewActivity.class));

            }
        });

        songList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.mostrecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MostViewAdapter5(songList);
        recyclerView.setAdapter(adapter);

// Fetch data from Firestore
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
                            SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name,moviename, countValue);
                            updatedList.add(song);
                            count++; // Increment the counter
                        }
                    }
                    // Update adapter with filtered list
                    Collections.reverse(updatedList);
                    adapter.updateSongList(updatedList);

                    if (updatedList.isEmpty()){
                        mosttxt.setVisibility(View.GONE);
                    }else {
                        mosttxt.setVisibility(View.VISIBLE);
                    }
                    // Check and display toast
                    checkAndDisplayToast(updatedList);
                })
                .addOnFailureListener(e -> Log.e("MostViewActivity", "Error fetching songs", e));


        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();




            exoPlayer = MediaPlayerManager.getInstance();
            exoPlayer.setShuffleModeEnabled(true);

            exoPlayer.addListener(playerListener);







        }




        progressBar2=view.findViewById(R.id.progress_bar);




        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                fetchData();
                fetchFavorites();
                getRecentlyPlayedSongs();

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
                                    SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name,moviename, countValue);
                                    updatedList.add(song);
                                    count++; // Increment the counter
                                }
                            }
                            // Update adapter with filtered list
                            Collections.reverse(updatedList);
                            adapter.updateSongList(updatedList);

                            if (updatedList.isEmpty()){
                                mosttxt.setVisibility(View.GONE);
                            }else {
                                mosttxt.setVisibility(View.VISIBLE);
                            }
                            // Check and display toast
                            checkAndDisplayToast(updatedList);
                        })
                        .addOnFailureListener(e -> Log.e("MostViewActivity", "Error fetching songs", e));



            }
        });







        lottieAnimationView=view.findViewById(R.id.lottieAnimationView);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUid = firebaseAuth.getCurrentUser().getUid();

// Retrieve the user data from the Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);
                    // Set the username in the TextView


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error

            }
        });

        DrawerLayout drawerLayout =view.findViewById(R.id.drawer_layout);
        menu = view.findViewById(R.id.imageView2);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationdrawer();
        // Initialize notification manager



        linearLayout1654=view.findViewById(R.id.linearLayout1654);
        linearLayout1654.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),songallActivity.class));
            }
        });

        linearLayout16 =view. findViewById(R.id.linearLayout16);
        username1 = view.findViewById(R.id.imageView2);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        String currentUserUid1 = firebaseAuth.getCurrentUser().getUid();

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
                CustomDialogClassfoeaddlogin cdd = new CustomDialogClassfoeaddlogin(getContext());
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });




        linearLayout16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MostViewActivity.class));

            }
        });
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);









        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();
                if (id == R.id.nav_home) {

                    Intent intent = new Intent(getContext(), MainActivity2.class);
                    startActivity(intent);

                    return true;
                }
                else if (id==R.id.nav_settings) {

                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);

                    return true;
                }
                else if (id==R.id.history) {
                    Intent intent = new Intent(getContext(), RecentlyPlayedActivity.class);
                    startActivity(intent);


                    return true;
                }
                else if (id==R.id.new12) {
                    Intent intent = new Intent(getContext(), playbackActivity.class);
                    startActivity(intent);


                    return true;
                }
                // Add other menu item click handling here if needed
                return false;
            }
        });

        animateNavigationdrawer();


        return view;
    }


    private void animateNavigationdrawer() {


        drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // This method is called when the drawer is sliding

                // Scale the View based on current slide offset
                // Here, you are scaling the contentView based on the slide offset

                final float diffScaledOffset = slideOffset * (1 - Endscale);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);


                // You are translating the contentView based on the slide offset
                final float xoffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xoffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }




























    private void checkAndDisplayToast(List<SongModel> songs) {
        if (!songs.isEmpty()) {

        }
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
                                    }catch (Exception e){

                                    }

                                }
                                if (!songExists) {
                                    recentlyPlayedSongs.add(song);
                                }
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
        }catch (Exception e){

        }

    }



    private void displayRecentlyPlayedSongs(List<SongModel> songs) {
        // Reverse the list of songs
        Collections.reverse(songs);

        if (songs.isEmpty()) {
            // Show toast message if the list is empty
            texttitle2.setVisibility(View.GONE);
        } else {
            // Create an instance of the RecentlyPlayedAdapter
            adapter45 = new RecentlyPlayedAdapter45(getContext(), songs);
            texttitle2.setVisibility(View.VISIBLE);

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
                    // Show toast message if the favorites list is empty
                    texttitle.setVisibility(View.GONE);

                } else {
                    adapter34.setFavoritesList(favoritesList34); // Update adapter data
                    texttitle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }



    private void fetchData() {
        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        songList5.clear(); // Clear existing data
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

                                songList5.add(song);
                            } else {
                                Log.d("SearchActivity", "No such document");
                            }
                        }

                        // Shuffle the songList5
                        Collections.shuffle(songList5);

                        adapter5.notifyDataSetChanged(); // Notify adapter after data change
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchActivity", "Error fetching songs", e);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    try {
                        Toast.makeText(getContext(), "Failed to load movies.", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){

                    }

                }
            });
        }catch (Exception e){

        }

    }





    private void Recyclerview_visibility(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("allsongrecyclerview").getValue(Boolean.class);

               if (bigSongView){
                   recyclerView5.setVisibility(View.VISIBLE);
                   section_5_title00.setVisibility(View.VISIBLE);
               }else {
                   recyclerView5.setVisibility(View.GONE);
                   section_5_title00.setVisibility(View.GONE);
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }

    private void Recyclerview_visibility_mostlyvied(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("mostviewedrecyclerview").getValue(Boolean.class);

                if (bigSongView){
                    mosttxt.setVisibility(View.VISIBLE);
                    mosttxt2.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    mosttxt.setVisibility(View.GONE);
                    mosttxt2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }

    private void Recyclerview_visibility_Recyctlyplayed(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("recentlyplayedrecyclerview").getValue(Boolean.class);

                if (bigSongView){
                    recentltpayedlayout.setVisibility(View.VISIBLE);
                }else {
                    recentltpayedlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }

    private void Recyclerview_visibility_favlayout(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("favrecyclerview").getValue(Boolean.class);

                if (bigSongView){
                    favaritylayout.setVisibility(View.VISIBLE);
                }else {
                    favaritylayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }


    private void Recyclerview_visibility_artist(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("artistrecyclerview").getValue(Boolean.class);

                if (bigSongView){

                    artistrecyclerview.setVisibility(View.VISIBLE);
                }else {

                    artistrecyclerview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }

}