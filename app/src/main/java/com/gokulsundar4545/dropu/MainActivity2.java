package com.gokulsundar4545.dropu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gokulsundar4545.searchallActivityFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity {

    private RelativeLayout playerView;
    LottieAnimationView lottieAnimationView;
    private MediaPlayerNotificationManager notificationManager;
    private ExoPlayer exoPlayer;

    private TextView section1Title, section2Title, section3Title, songTitleTextView, section4Title, subtitle, section5Title, section6Title, username1;
    ProgressBar progressBar2;
    private ImageView songcover, songCoverImageView;

    ConstraintLayout playercontroler;
    CardView playercontroler2;

    RelativeLayout player_view;

    FloatingActionButton fab1;
    private boolean isPlaying = false;
    CircleImageView songImage;
    ImageView playButton, pauseButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    CardView carproduct23;
    ImageView playliist1, bar;

    LinearLayout layout1,layout2,layout3,layout4;

    ImageView menu1,menu2,menu3;
    TextView text1,text2,text3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1234); // 1234 is a request code
        }




        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);

        layout1=findViewById(R.id.layout1);
        layout2=findViewById(R.id.layout2);
        layout3=findViewById(R.id.layout3);
        layout4=findViewById(R.id.layout4);

        menu1=findViewById(R.id.menu1);
        menu2=findViewById(R.id.menu2);
        menu3=findViewById(R.id.menu3);

        text1=findViewById(R.id.textView1);
        text2=findViewById(R.id.textView2);
        text3=findViewById(R.id.textView3);

        MainActivityHomeSubFragment bottomBarFragment = new MainActivityHomeSubFragment(MainActivity2.this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomBarFragment);
        fragmentTransaction.commit();

        Drawable newDrawable = ContextCompat.getDrawable(MainActivity2.this, R.drawable.home2);
        menu1.setImageDrawable(newDrawable);

        Drawable newDrawable2 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.search11);
        menu2.setImageDrawable(newDrawable2);

        Drawable newDrawable3 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.library);
        menu3.setImageDrawable(newDrawable3);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUid1 = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(currentUserUid1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String profile=dataSnapshot.child("profileImageUrl").getValue(String.class);

                    CircleImageView profileImageView=findViewById(R.id.menu4);
                    Picasso.get().load(profile).placeholder(R.drawable.picture).
                            into(profileImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(MainActivity2.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityHomeSubFragment bottomBarFragment = new MainActivityHomeSubFragment(MainActivity2.this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, bottomBarFragment);
                fragmentTransaction.commit();

                Drawable newDrawable = ContextCompat.getDrawable(MainActivity2.this, R.drawable.home2);
                menu1.setImageDrawable(newDrawable);

                Drawable newDrawable2 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.search11);
                menu2.setImageDrawable(newDrawable2);

                Drawable newDrawable3 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.library);
                menu3.setImageDrawable(newDrawable3);

            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchnewFragment bottomBarFragment = new SearchnewFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, bottomBarFragment);
                fragmentTransaction.commit();

                Drawable newDrawable = ContextCompat.getDrawable(MainActivity2.this, R.drawable.home1);
                menu1.setImageDrawable(newDrawable);

                Drawable newDrawable2 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.search2);
                menu2.setImageDrawable(newDrawable2);

                Drawable newDrawable3 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.library);
                menu3.setImageDrawable(newDrawable3);

            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchallActivityFragment bottomBarFragment = new searchallActivityFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, bottomBarFragment);
                fragmentTransaction.commit();

                Drawable newDrawable = ContextCompat.getDrawable(MainActivity2.this, R.drawable.home1);
                menu1.setImageDrawable(newDrawable);

                Drawable newDrawable2 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.search11);
                menu2.setImageDrawable(newDrawable2);

                Drawable newDrawable3 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.library2);
                menu3.setImageDrawable(newDrawable3);

            }
        });


        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileActivityFragment bottomBarFragment = new ProfileActivityFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, bottomBarFragment);
                fragmentTransaction.commit();

                Drawable newDrawable = ContextCompat.getDrawable(MainActivity2.this, R.drawable.home1);
                menu1.setImageDrawable(newDrawable);

                Drawable newDrawable2 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.search11);
                menu2.setImageDrawable(newDrawable2);

                Drawable newDrawable3 = ContextCompat.getDrawable(MainActivity2.this, R.drawable.library);
                menu3.setImageDrawable(newDrawable3);

            }
        });


        carproduct23 = findViewById(R.id.carproduct2);
        playliist1 = findViewById(R.id.imageView4);
        playercontroler = findViewById(R.id.laypro3);
        playercontroler2 = findViewById(R.id.playcontrol);

        IntentFilter filter = new IntentFilter("SONG_CHANGED");
        registerReceiver(songChangeReceiver, filter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Check if the current song is in the playlist
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (MediaPlayerManager.getCurrentSong() != null) {
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference userPlaylistRef = mDatabase.child("Users").child(userId).child("playlist");
                userPlaylistRef.orderByChild("title").equalTo(MediaPlayerManager.getCurrentSong().getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            playliist1.setImageResource(R.drawable.baseline_check_circle_24);
                        } else {
                            playliist1.setImageResource(R.drawable.addplay);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });

            }
        }


        playliist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite();
            }
        });


        checkNetworkStatus();

        songTitleTextView = findViewById(R.id.song_title_text_view);
        notificationManager = new MediaPlayerNotificationManager(this);
        player_view = findViewById(R.id.player_view);
        exoPlayer = MediaPlayerManager.getInstance();
        subtitle = findViewById(R.id.subtitle);
        songCoverImageView = findViewById(R.id.song_cover_image_view);
        fab1 = findViewById(R.id.fab);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddHighlitescover.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(findViewById(R.id.fab), "transition_login");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity2.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        progressBar2 = findViewById(R.id.progress_bar);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);

        playerView = findViewById(R.id.player_view);

        showPlayerView();

        songCoverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (playercontroler2.getVisibility() == View.VISIBLE) {
//                    playercontroler2.setVisibility(View.INVISIBLE);
//                    playercontroler2.startAnimation(AnimationUtils.loadAnimation(MainActivity2.this, R.anim.slid_to_left));
//                } else {
//                    playercontroler2.setVisibility(View.VISIBLE);
//                    playercontroler2.startAnimation(AnimationUtils.loadAnimation(MainActivity2.this, R.anim.slid_from_left));
//                }

                Intent serviceIntent = new Intent(MainActivity2.this, OverlayService.class);
                startService(serviceIntent);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showPlayerView() {






        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);

        SongModel currentSong1 = MediaPlayerManager.getCurrentSong();


        if (exoPlayer != null) {
            if (exoPlayer.isPlaying()) {
                pauseButton.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(View.VISIBLE);
                songTitleTextView.setText(currentSong1.getTitle());
                songTitleTextView.setSelected(true);

            } else {

                playButton.setVisibility(View.VISIBLE);

                lottieAnimationView.setVisibility(View.GONE);
                songTitleTextView.setText(currentSong1.getTitle());
                songTitleTextView.setSelected(false);
            }
        } else {

            playerView.setVisibility(View.GONE); // Hide the player view if the player is not ready
        }


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoPlayer != null && !exoPlayer.isPlaying()) {
                    exoPlayer.play();
                    isPlaying = true; // Update playback state
                    updateButtonVisibility(); // Update button visibility
                    lottieAnimationView.setVisibility(View.VISIBLE);

                    songTitleTextView.setText(currentSong1.getTitle());
                    songTitleTextView.setSelected(true);
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    exoPlayer.pause();
                    isPlaying = false; // Update playback state
                    updateButtonVisibility(); // Update button visibility

                    songTitleTextView.setText(currentSong1.getTitle());
                    songTitleTextView.setSelected(false);
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }
        });


        updateProgressBar2();
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, PlayerActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slid_to_top);
            }
        });


        if (MediaPlayerManager.getCurrentSong() != null) {

            Intent serviceIntent = new Intent(this, OverlayService.class);
            startService(serviceIntent);

        } else {
            cancelNotification();

        }

        SongModel currentSong = MediaPlayerManager.getCurrentSong();
        if (currentSong != null) {
            playerView.setVisibility(View.VISIBLE);

            subtitle.setText(currentSong.getSubtitle());
            Glide.with(MainActivity2.this).load(currentSong.getCoverUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(songCoverImageView);

            songImage = findViewById(R.id.songimage);
            Picasso.get().load(currentSong.getCoverUrl()).into(songImage);


            Glide.with(this).asBitmap().load(currentSong.getCoverUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    // Use Palette API to extract the dark color
                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // Get the dark vibrant color, fallback to black if not available
                            int darkColor = palette.getDarkVibrantColor(getResources().getColor(android.R.color.black));

                            // If dark vibrant is not available, fallback to dark muted
                            if (darkColor == getResources().getColor(android.R.color.black)) {
                                darkColor = palette.getDarkMutedColor(getResources().getColor(android.R.color.black));
                            }

                            // Use the extracted dark color
                            player_view.setBackgroundColor(darkColor);
                            playercontroler.setBackgroundColor(darkColor);
                        }
                    });
                    return false;
                }
            }).into(songCoverImageView);


        } else {
            playerView.setVisibility(View.GONE);
        }
    }


    private BroadcastReceiver songChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {



            if (MediaPlayerManager.getCurrentSong() != null) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference userPlaylistRef = mDatabase.child("Users").child(userId).child("playlist");
                    userPlaylistRef.orderByChild("title").equalTo(MediaPlayerManager.getCurrentSong().getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                playliist1.setImageResource(R.drawable.baseline_check_circle_24);
                            } else {
                                playliist1.setImageResource(R.drawable.addplay);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });

                }
            }



            playButton = findViewById(R.id.play_button);
            pauseButton = findViewById(R.id.pause_button);

            SongModel currentSong1 = MediaPlayerManager.getCurrentSong();


            if (exoPlayer != null) {
                if (exoPlayer.isPlaying()) {
                    pauseButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Playback paused", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    songTitleTextView.setText(currentSong1.getTitle());
                    songTitleTextView.setSelected(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Paused", Toast.LENGTH_SHORT).show();
                    playButton.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                    songTitleTextView.setText(currentSong1.getTitle());
                    songTitleTextView.setSelected(false);
                }
            } else {

                playerView.setVisibility(View.GONE); // Hide the player view if the player is not ready
            }


            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exoPlayer != null && !exoPlayer.isPlaying()) {
                        exoPlayer.play();
                        isPlaying = true; // Update playback state
                        updateButtonVisibility(); // Update button visibility
                        lottieAnimationView.setVisibility(View.VISIBLE);

                        songTitleTextView.setText(currentSong1.getTitle());
                        songTitleTextView.setSelected(true);
                    }
                }
            });

            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exoPlayer != null && exoPlayer.isPlaying()) {
                        exoPlayer.pause();
                        isPlaying = false; // Update playback state
                        updateButtonVisibility(); // Update button visibility

                        songTitleTextView.setText(currentSong1.getTitle());
                        songTitleTextView.setSelected(false);
                        lottieAnimationView.setVisibility(View.GONE);
                    }
                }
            });


            updateProgressBar2();
            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity2.this, PlayerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_bottom, R.anim.slid_to_top);
                }
            });


            if (MediaPlayerManager.getCurrentSong() != null) {
                showNotification();

            } else {
                cancelNotification();

            }

            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            if (currentSong != null) {
                playerView.setVisibility(View.VISIBLE);

                subtitle.setText(currentSong.getSubtitle());

                try {
                    Glide.with(MainActivity2.this).load(currentSong.getCoverUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(songCoverImageView);

                    Glide.with(MainActivity2.this).load(currentSong.getCoverUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(songImage);

                    Glide.with(MainActivity2.this).asBitmap().load(currentSong.getCoverUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).listener(new RequestListener<Bitmap>() {
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
                                    playercontroler.setBackgroundColor(dominantColor);

                                }
                            });
                            return false;
                        }
                    }).into(songCoverImageView);
                } catch (Exception e) {

                }


            } else {
                playerView.setVisibility(View.GONE);
            }
        }
    };

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
                    lottieAnimationView.setVisibility(View.VISIBLE);

                    playButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);
                }
                // Call the method again after a delay
                updateProgressBar2();
            }
        }, 1000); // Update progress bar every second (1000 milliseconds)


    }

    private void cancelNotification() {
        notificationManager.cancelNotification();
    }


    private void updateButtonVisibility() {
        if (isPlaying) {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        } else {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
    }


    private void checkNetworkStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        carproduct23.setVisibility(View.GONE);
                        carproduct23.startAnimation(AnimationUtils.loadAnimation(MainActivity2.this, R.anim.slid_to_bottom));

                    }
                });
            }

            @Override
            public void onLost(Network network) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        carproduct23.setVisibility(View.VISIBLE);
                        carproduct23.startAnimation(AnimationUtils.loadAnimation(MainActivity2.this, R.anim.slide_from_bottom3));
                    }
                });
            }
        });
    }

    private void toggleFavorite() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("playlist");


                userFavoritesRef.orderByChild("title").equalTo(MediaPlayerManager.getCurrentSong().getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Song is already in favorites, remove it
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                childSnapshot.getRef().removeValue();
                            }

                            playliist1.setImageResource(R.drawable.addplay);

                        } else {

                            String songUid = userFavoritesRef.push().getKey(); // Generate a unique key for the song
                            userFavoritesRef.child(songUid).child("title").setValue(MediaPlayerManager.getCurrentSong().getTitle());
                            userFavoritesRef.child(songUid).child("subtitle").setValue(MediaPlayerManager.getCurrentSong().getSubtitle());
                            userFavoritesRef.child(songUid).child("coverUrl").setValue(MediaPlayerManager.getCurrentSong().getCoverUrl());
                            userFavoritesRef.child(songUid).child("url").setValue(MediaPlayerManager.getCurrentSong().getUrl());// Set the coverUrl


                            playliist1.setImageResource(R.drawable.baseline_check_circle_24);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

        }


    }


    public void playPrevious(View view) {
        MediaPlayerManager.playPrevious(view.getContext());
        pauseButton.setVisibility(View.VISIBLE);
    }

    public void playnext(View view) {
        MediaPlayerManager.playNext(view.getContext());
        pauseButton.setVisibility(View.VISIBLE);
    }

    private void showNotification() {
        // Get the current song information
        SongModel currentSong = MediaPlayerManager.getCurrentSong();
        if (currentSong != null) {
            // Construct notification title and subtitle
            String title = "Now Playing: " + currentSong.getTitle();
            String subtitle = currentSong.getSubtitle();

            try {
                Glide.with(this)
                        .asBitmap()
                        .load(currentSong.getCoverUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                // Show notification with the song's image bitmap
                                MediaPlayerNotificationManager notificationManager = new MediaPlayerNotificationManager(MainActivity2.this);

                                // Assuming you have a large icon as well, replace R.drawable.your_large_icon with your actual large icon resource
                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.music);

                                // Show the notification with the loaded bitmap
                                notificationManager.showNotification(title, subtitle, largeIcon, resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // Handle case where the bitmap loading is cleared
                            }
                        });
            }catch (Exception e){

            }
            // Load the song's image bitmap using Glide or any other image loading library

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            if (Settings.canDrawOverlays(this)) {
                // Permission granted, start the overlay service
                Intent serviceIntent = new Intent(this, OverlayService.class);
                startService(serviceIntent);
            } else {
                // Permission not granted, show a message to the user
                Toast.makeText(this, "Overlay permission is required to show the widget", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}