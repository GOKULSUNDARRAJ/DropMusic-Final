package com.gokulsundar4545;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.palette.graphics.Palette;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gokulsundar4545.dropu.MainActivity2;
import com.gokulsundar4545.dropu.MainHomeFragment;
import com.gokulsundar4545.dropu.MediaPlayerManager;
import com.gokulsundar4545.dropu.MediaPlayerNotificationManager;
import com.gokulsundar4545.dropu.PlayerActivity;
import com.gokulsundar4545.dropu.R;
import com.gokulsundar4545.dropu.SongModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivitynew extends AppCompatActivity {
    private RelativeLayout  playerView;
    LottieAnimationView lottieAnimationView;
    private MediaPlayerNotificationManager notificationManager;
    private ExoPlayer exoPlayer;

    private TextView section1Title, section2Title, section3Title, songTitleTextView, section4Title, subtitle, section5Title, section6Title, username1;
    ProgressBar progressBar2;
    private ImageView songcover, songCoverImageView;

    RelativeLayout player_view;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnew);
        showPlayerView();

        songTitleTextView =findViewById(R.id.song_title_text_view);
        notificationManager = new MediaPlayerNotificationManager(this);
        player_view=findViewById(R.id.player_view);
            exoPlayer = MediaPlayerManager.getInstance();
            exoPlayer.setShuffleModeEnabled(true);
        songCoverImageView = findViewById(R.id.song_cover_image_view);

        progressBar2=findViewById(R.id.progress_bar);
        lottieAnimationView=findViewById(R.id.lottieAnimationView);

        playerView = findViewById(R.id.player_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new MainHomeFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }
        });

        // Set default selection
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Default fragment to show
        }


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
                Intent intent=new Intent(MainActivitynew.this, PlayerActivity.class);
                startActivity(intent);

            }
        });



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
            Glide.with(MainActivitynew.this)
                    .load(currentSong.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(songCoverImageView);





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

    private void cancelNotification() {
        notificationManager.cancelNotification();
    }
}