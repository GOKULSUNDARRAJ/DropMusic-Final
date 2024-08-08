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
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView5;
    private SearchAdapter5 adapter5;
    private List<SongModel> songList5;
    private EditText editText5;
    private SwipeRefreshLayout swipeRefreshLayout5;

    ImageView back5;

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

    private View parentLayout;
    private CategoryAdapter categoryAdapter;
    private RelativeLayout section1MainLayout, section2MainLayout, section3MainLayout, section4MainLayout, playerView, section5MainLayout, section6MainLayout;
    private TextView section1Title, section2Title, section3Title, songTitleTextView, section4Title, subtitle, section5Title, section6Title, username1;
    private RecyclerView categoriesRecyclerView, section1RecyclerView, section2RecyclerView, section3RecyclerView, section4RecyclerView, section5RecyclerView, section6RecyclerView;
    private ImageView songcover, songCoverImageView;
    CircleImageView image4recyclerview;

    LinearLayout linearLayout16,linearLayout1654;
    TextView menu;

    static  final  float Endscale=0.7f;

    private MediaPlayerNotificationManager notificationManager;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    LottieAnimationView lottieAnimationView;

    DrawerLayout drawerlayout;

    TextView seeall;

    RelativeLayout contentView;

    NavigationView navigation_view;

    TextView mosttxt,mosttxt2;

    private AudioManager audioManager;
    TextView textView3;


    private ProgressBar volumeProgressBar;
    private boolean isProgressBarVisible = false;
    private static final int PROGRESS_BAR_DURATION = 3000; // 3 seconds
    private Handler handler = new Handler();
    private Runnable hideProgressBarRunnable = new Runnable() {
        @Override
        public void run() {
            hideProgressBar();
        }
    };

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

    CardView carproduct23;
    ProgressBar progressbar56;
    TextView artisttext;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        progressbar56=findViewById(R.id.progressbar56);



        image4recyclerview=findViewById(R.id.profile);



        carproduct23=findViewById(R.id.carproduct2);

        carproduct=findViewById(R.id.carproduct);

        toolbar=findViewById(R.id.toolbar);
        imageView2=findViewById(R.id.imageView2);


        const12=findViewById(R.id.const12);
        play=findViewById(R.id.play);


        toolbar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.recycler2));


        recyclerView5 = findViewById(R.id.section_5_recycler_view00);
        recyclerView5.setLayoutManager(new LinearLayoutManager(this));

        songList5 = new ArrayList<>();
        adapter5 = new SearchAdapter5(this, songList5);
        recyclerView5.setAdapter(adapter5);

        fetchData();
        textView3=findViewById(R.id.textView3);
        texttitle=findViewById(R.id.texttitle);
        texttitle2=findViewById(R.id.texttitle2);

        recyclerView34 = findViewById(R.id.mostrecyclerview54);
        recyclerView34.setLayoutManager(new LinearLayoutManager(this));

        favoritesList34 = new ArrayList<>();
        adapter34 = new PlaylistAdapter45(favoritesList34);
        recyclerView34.setAdapter(adapter34);

        // Fetch the user's favorites from Firebase
        fetchFavorites();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Initialize RecyclerView
        recyclerView45 = findViewById(R.id.mostrecyclerview5);
        LinearLayoutManager layoutManager45 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView45.setLayoutManager(layoutManager45);

        // Fetch recently played songs
        getRecentlyPlayedSongs();

        linearLayoutPodcast = findViewById(R.id.linearLayout165);

        linearLayoutPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ProdcastActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mosttxt=findViewById(R.id.mosttxt);
        mosttxt.setText("Trending Songs .");

        mosttxt2=findViewById(R.id.mosttxt2);

        mosttxt2.setText("See all");

        mosttxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MostViewActivity.class));
                finish();
            }
        });

        songList = new ArrayList<>();
        recyclerView = findViewById(R.id.mostrecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
                        if (countValue != null && countValue > 5) { // Check count condition
                            SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name, countValue);
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
            setTitle(currentSong.getTitle());



            exoPlayer = MediaPlayerManager.getInstance();
            exoPlayer.setShuffleModeEnabled(true);

            exoPlayer.addListener(playerListener);







        }




        progressBar2=findViewById(R.id.progress_bar);




        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              getCategories();


                setupSection("section_1", section3MainLayout, section3Title, section3RecyclerView);
                setupSection("section_2", section2MainLayout, section2Title, section2RecyclerView);

                setupSection2("section_3", section1MainLayout, section1Title, section1RecyclerView);

                setupSection3("section5", section4MainLayout, section4Title, section4RecyclerView,image4recyclerview);

                setupSection4("section4", section5MainLayout, section5Title, section5RecyclerView);
                setupSection("section6", section6MainLayout, section6Title, section6RecyclerView);
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
                                if (countValue != null && countValue > 5) { // Check count condition
                                    SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name, countValue);
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

        volumeProgressBar = findViewById(R.id.volumeProgressBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        seeall=findViewById(R.id.textView3);
        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FullArtistActicity.class));
                finish();
            }
        });

        artisttext=findViewById(R.id.artisttext);
        artisttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FullArtistActicity.class));
                finish();
            }
        });

        navigation_view=findViewById(R.id.navigation_view);

        navigation_view.bringToFront();
        navigation_view.setNavigationItemSelectedListener(this);

        contentView=findViewById(R.id.content);

        drawerlayout=findViewById(R.id.drawer_layout);
        navigation_view.setCheckedItem(R.id.nav_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item selection
                if (item.getItemId() == R.id.nav_home) {
                    return true;
                } else if (item.getItemId() == R.id.nav_settings) {
                    // Handle menu item 2 click


                    Intent intent = new Intent(MainActivity.this, songallActivity.class);
                    startActivity(intent);
                    finish();


                    return true;
                } else if (item.getItemId() == R.id.nav_logout) {
                    Intent intent = new Intent(MainActivity.this, searchallActivity.class);
                    startActivity(intent);
                    finish();



                    return true;
                } else if (item.getItemId() == R.id.settings123) {

                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
                else {
                    return false;
                }
            }
        });


        parentLayout = findViewById(android.R.id.content);
        checkNetworkStatus();


        lottieAnimationView=findViewById(R.id.lottieAnimationView);

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
                    TextView navHeaderUsername = findViewById(R.id.nav_header_username);
                    TextView navHeaderUsername2 = findViewById(R.id.nav_header_username2);
                    navHeaderUsername.setText(username);
                    navHeaderUsername2.setText(username);

                    navHeaderUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                    });

                    navHeaderUsername2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error

            }
        });


        menu = findViewById(R.id.imageView2);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationdrawer();
        // Initialize notification manager
        notificationManager = new MediaPlayerNotificationManager(this);


        linearLayout1654=findViewById(R.id.linearLayout1654);
        linearLayout1654.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });

        linearLayout16 = findViewById(R.id.linearLayout16);
        username1 = findViewById(R.id.imageView2);

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
                CustomDialogClassfoeaddlogin cdd = new CustomDialogClassfoeaddlogin(MainActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });




        linearLayout16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MostViewActivity.class));
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slid_to_top);
                finish();
            }
        });
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);

        playerView = findViewById(R.id.player_view);
        songCoverImageView = findViewById(R.id.song_cover_image_view);
        songTitleTextView = findViewById(R.id.song_title_text_view);
        subtitle = findViewById(R.id.subtitle);

        section3MainLayout = findViewById(R.id.section_3_main_layout);
        section3Title = findViewById(R.id.section_3_title);
        section3RecyclerView = findViewById(R.id.section_3_recycler_view);

        section2MainLayout = findViewById(R.id.section_2_main_layout);
        section2Title = findViewById(R.id.section_2_title);
        section2RecyclerView = findViewById(R.id.section_2_recycler_view);


        section1MainLayout = findViewById(R.id.section_1_main_layout);
        section1Title = findViewById(R.id.section_1_title);
        section1RecyclerView = findViewById(R.id.section_1_recycler_view);

        section4MainLayout = findViewById(R.id.section_4_main_layout);
        section4Title = findViewById(R.id.section_4_title);
        section4RecyclerView = findViewById(R.id.section_4_recycler_view);

        section5MainLayout = findViewById(R.id.section_5_main_layout);
        section5Title = findViewById(R.id.section_5_title);
        section5RecyclerView = findViewById(R.id.section_5_recycler_view);

        section6MainLayout = findViewById(R.id.section_6_main_layout);
        section6Title = findViewById(R.id.section_6_title);
        section6RecyclerView = findViewById(R.id.section_6_recycler_view);

        getCategories();

        songcover = findViewById(R.id.song_cover_image_view);

        setupSection("section_1", section3MainLayout, section3Title, section3RecyclerView);
        setupSection("section_2", section2MainLayout, section2Title, section2RecyclerView);

        setupSection2("section_3", section1MainLayout, section1Title, section1RecyclerView);

        setupSection3("section5", section4MainLayout, section4Title, section4RecyclerView,image4recyclerview);

        setupSection4("section4", section5MainLayout, section5Title, section5RecyclerView);
        setupSection("section6", section6MainLayout, section6Title, section6RecyclerView);


        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();
                if (id == R.id.nav_home) {

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (id==R.id.nav_settings) {

                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (id==R.id.history) {
                    Intent intent = new Intent(MainActivity.this, RecentlyPlayedActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
                else if (id==R.id.new12) {
                    Intent intent = new Intent(MainActivity.this, playbackActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
                // Add other menu item click handling here if needed
                return false;
            }
        });
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


    @Override
    protected void onResume() {
        super.onResume();
        showPlayerView();


    }

    private void showPlayerView() {
        updateProgressBar2();
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        play.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_from_bottom3));

        carproduct.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_from_bottom3));

        if (MediaPlayerManager.getCurrentSong() != null) {


            // Show LottieAnimationView
            lottieAnimationView.setVisibility(View.VISIBLE);
        } else {
            cancelNotification();
            // Hide LottieAnimationView
            lottieAnimationView.setVisibility(View.GONE);
        }

        SongModel currentSong = MediaPlayerManager.getCurrentSong();
        if (currentSong != null) {
            playerView.setVisibility(View.VISIBLE);
            songTitleTextView.setText(currentSong.getTitle());
            songTitleTextView.setSelected(true);
            subtitle.setText(currentSong.getSubtitle());
            Glide.with(MainActivity.this)
                    .load(currentSong.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(songCoverImageView);


            RelativeLayout player_view=findViewById(R.id.player_view);


            Glide.with(this)
                    .asBitmap()
                    .load(currentSong.getCoverUrl())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            // Use Palette API to extract dominant color
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Get the dominant color
                                    int dominantColor = palette.getDominantColor(getResources().getColor(android.R.color.black));
                                    // Now you have the dominant color, you can use it as needed
                                    // For example, set the background color of a view
                                    player_view.setBackgroundColor(dominantColor);
                                }
                            });
                            return false;
                        }
                    })
                    .into(songCoverImageView);
        } else {
            playerView.setVisibility(View.GONE);
        }
    }




    private void cancelNotification() {
        notificationManager.cancelNotification();
    }

    // Categories
    public void getCategories() {

        FirebaseFirestore.getInstance().collection("Category")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel> categoryList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                    setupCategoryRecyclerView(categoryList);
                    swipeRefreshLayout.setRefreshing(false);
                    progressbar56.setVisibility(View.GONE);
                });
    }

    public void setupCategoryRecyclerView(List<CategoryModel> categoryList) {
        categoryAdapter = new CategoryAdapter(categoryList, this);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoryAdapter);

    }


    private void setupSection(String id, RelativeLayout mainLayout, TextView titleView, RecyclerView recyclerView) {
        FirebaseFirestore.getInstance().collection("sections")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                        if (section != null) {

                            titleView.setText(section.getName());
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(new SectionSongListAdapter(section));

                            mainLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(MainActivity.this, SongsListActivity.class);
                                    intent.putExtra("category", (Serializable) section);
                                    startActivity(intent);

                                }
                            });




                        }
                    }
                });
    }


    private void setupSection2(String id, RelativeLayout mainLayout, TextView titleView, RecyclerView recyclerView) {
        FirebaseFirestore.getInstance().collection("sections")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                        if (section != null) {

                            titleView.setText(section.getName());
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                            recyclerView.setAdapter(new SectionSongListAdapter2(section));
                            mainLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(MainActivity.this, SongsListActivity.class);
                                    intent.putExtra("category", (Serializable) section);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }


    private void setupSection3(String id, RelativeLayout mainLayout, TextView titleView, RecyclerView recyclerView,ImageView profile) {
        FirebaseFirestore.getInstance().collection("sections")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                        if (section != null) {

                            titleView.setText(section.getName());
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(new SectionSongListAdapter3(section));
                            Picasso.get().load(section.getCoverUrl()).into(profile);
                            mainLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(MainActivity.this, SongsListActivity.class);
                                    intent.putExtra("category", (Serializable) section);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }


    private void setupSection4(String id, RelativeLayout mainLayout, TextView titleView, RecyclerView recyclerView) {
        FirebaseFirestore.getInstance().collection("sections")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                        if (section != null) {

                            titleView.setText(section.getName());
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(new SectionSongListAdapter4(section));
                            mainLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(MainActivity.this, SongsListActivity.class);
                                    intent.putExtra("category", (Serializable) section);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });


    }


    public void setupMostlyPlayed(String id, RelativeLayout mainLayout, TextView titleView, RecyclerView recyclerView, Context context) {
        FirebaseFirestore.getInstance().collection("sections")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        FirebaseFirestore.getInstance().collection("songs")
                                .orderBy("count", Query.Direction.DESCENDING)
                                .limit(5)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot songListSnapshot) {
                                        List<SongModel> songsModelList = songListSnapshot.toObjects(SongModel.class);
                                        List<String> songsIdList = new ArrayList<>();
                                        for (SongModel songModel : songsModelList) {
                                            songsIdList.add(songModel.getId());
                                        }
                                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                                        if (section != null) {
                                            section.setSongs(songsIdList);
                                            mainLayout.setVisibility(View.VISIBLE);
                                            titleView.setText(section.getName());
                                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                            recyclerView.setAdapter(new SectionSongListAdapter(section));
                                            mainLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    startActivity(new Intent(MainActivity.this, SongsListActivity.class));
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (drawerlayout.isDrawerVisible(GravityCompat.START)){
            drawerlayout.closeDrawer(GravityCompat.START);
            navigation_view.setCheckedItem(R.id.nav_home);
        }else {
            super.onBackPressed();
            finish();
        }


    }
    private void checkNetworkStatus() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                carproduct23.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onLost(Network network) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        carproduct23.setVisibility(View.VISIBLE);
                        carproduct23.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_from_bottom3));
                    }
                });
            }

        });
    }

    private void showSnackbar(String message, boolean isOnline) {
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text); // Get the TextView from the Snackbar layout

        // Set text color
        if (isOnline) {
            textView.setTextColor(Color.GREEN); // Change text color to white when online
        } else {
            textView.setTextColor(Color.RED); // Change text color to red when offline
        }



        snackbar.show();
    }


    public void gotocantact(View view) {

        Intent intent=new Intent(MainActivity.this,ContactActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    increaseVolume();
                    showProgressBar();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    decreaseVolume();
                    showProgressBar();
                    return true;
                }
                break;
        }
        return super.dispatchKeyEvent(event);
    }


    private void increaseVolume() {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int newVolume = Math.min(maxVolume, currentVolume + 1);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
        updateProgressBar();
        resetHideTimer();
    }

    private void decreaseVolume() {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int newVolume = Math.max(0, currentVolume - 1);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
        updateProgressBar();
        resetHideTimer();
    }

    private void updateProgressBar() {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeProgressBar.setProgress((int) ((float) currentVolume / maxVolume * 100));
    }

    private void showProgressBar() {
        // Make progress bar visible and animate its alpha property from 0 to 1
        volumeProgressBar.setVisibility(View.VISIBLE);
        volumeProgressBar.animate()
                .alpha(1.0f) // Fully opaque
                .setDuration(300) // Animation duration in milliseconds
                .start();

        isProgressBarVisible = true;

        // Schedule hiding of the progress bar after a delay
        resetHideTimer();
        volumeProgressBar.setAlpha(0f);
    }

    private void hideProgressBar() {
        // Animate the alpha property of the progress bar from 1 to 0 and then make it gone
        volumeProgressBar.animate()
                .alpha(0.0f) // Fully transparent
                .setDuration(300) // Animation duration in milliseconds
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the progress bar after animation completes
                        volumeProgressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .start();

        isProgressBarVisible = false;
    }


    private void resetHideTimer() {
        handler.removeCallbacks(hideProgressBarRunnable);
        handler.postDelayed(hideProgressBarRunnable, PROGRESS_BAR_DURATION);
    }

    private void updateProgressBar2() {
        // Update progress bar every second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    long currentPosition = exoPlayer.getCurrentPosition(); // Get current playback position
                    long totalDuration = exoPlayer.getDuration(); // Get total duration of the song
                    // Calculate progress percentage
                    int progress = (int) (currentPosition * 100 / totalDuration);
                    // Update progress bar
                    progressBar2.setProgress(progress);
                }
                // Call the method again after a delay
                updateProgressBar2();
            }
        }, 1000); // Update progress bar every second (1000 milliseconds)
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
            adapter45 = new RecentlyPlayedAdapter45(this, songs);
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
                                SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics,artist,name, count);

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

    private void checkForUpdates() {
        if (UpdateChecker.isUpdateAvailable(this)) {
            showUpdateDialog();
        } else {
            Toast.makeText(this, "App is up to date", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Available");
        builder.setMessage("A new version of the app is available. Please update to continue.");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                redirectToPlayStore();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void redirectToPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            // If Play Store app is not available on the device, open the Play Store website
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void gotoshare(View view) {
        startActivity(new Intent(view.getContext(), ShareSongActivity.class));
    }
}

