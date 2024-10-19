package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.Manifest;

import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView currentTime, totalTime;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    BroadcastReceiver mediaReceiver;
    private ExoPlayer exoPlayer;
    TextView title, subtitle, nowplaying, lyrics, textView2;
    ImageView songCoverImageView, songGifImageView, favButton, back, menu;

    PlayerView playerView;
    private MediaPlayerNotificationManager notificationManager;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private long downloadId; // Store the download ID to track the download progress

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            showGif(isPlaying);
            showpausebutton();

        }
    };
    private String coverUrl;

    ImageView playButton, pauseButton;
    ProgressBar progressBar2;

    LinearLayout alert;
    ImageView song_cover_image_view78;
    TextView song_title_text_view42, song_title_text_view442, song_cover_image_view782;
    private boolean isPlaying = false;


    private RecyclerView recyclerView;
    private SearchAdapter8 adapter;
    private List<SongModel> songList;


    TextView Suggestedtxt;

    ImageView repeatButton;
    private ImageView shuffleButton;

    ImageView sent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        showNotification();
        sent = findViewById(R.id.sent);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomSheetFragmentforsent bottomSheetFragment = new MyBottomSheetFragmentforsent();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });


        Suggestedtxt = findViewById(R.id.Suggestedtxt);
        repeatButton = findViewById(R.id.repeatButton);
        if (MediaPlayerManager.isRepeatModeOn()) {

            repeatButton.setImageResource(R.drawable.repeaton); // Use your "Repeat Off" icon here
        } else {

            repeatButton.setImageResource(R.drawable.repeatoff); // Use your "Repeat On" icon here
        }


        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaPlayerManager.isRepeatModeOn()) {
                    // Disable repeat mode
                    MediaPlayerManager.disableRepeatMode();
                    // Update the button to show "Repeat Off" icon
                    repeatButton.setImageResource(R.drawable.repeatoff); // Use your "Repeat Off" icon here
                } else {
                    // Enable repeat mode
                    MediaPlayerManager.enableRepeatMode();
                    // Update the button to show "Repeat On" icon
                    repeatButton.setImageResource(R.drawable.repeaton); // Use your "Repeat On" icon here
                }
            }
        });

        // Initialize the shuffle button

        shuffleButton = findViewById(R.id.shuffleButton);

        if (MediaPlayerManager.isShuffleModeOn()) {

            shuffleButton.setImageResource(R.drawable.shuffleon); // Use your "Shuffle Off" icon here
        } else {

            shuffleButton.setImageResource(R.drawable.shuffleoff); // Use your "Shuffle On" icon here
        }


        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaPlayerManager.isShuffleModeOn()) {
                    // Disable shuffle mode
                    MediaPlayerManager.disableShuffleMode();
                    // Update the button to show "Shuffle Off" icon
                    shuffleButton.setImageResource(R.drawable.shuffleoff); // Use your "Shuffle Off" icon here
                } else {
                    // Enable shuffle mode
                    MediaPlayerManager.enableShuffleMode();
                    // Update the button to show "Shuffle On" icon
                    shuffleButton.setImageResource(R.drawable.shuffleon); // Use your "Shuffle On" icon here
                }
            }
        });


        // Fetch data initially


        seekBar = findViewById(R.id.seek_bar);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);

        IntentFilter filter = new IntentFilter("SONG_CHANGED");
        registerReceiver(songChangeReceiver, filter);

        textView2 = findViewById(R.id.textView2);

        song_cover_image_view78 = findViewById(R.id.song_cover_image_view78);

        alert = findViewById(R.id.alert);
        title = findViewById(R.id.song_title_text_view);
        subtitle = findViewById(R.id.song_subtitle_text_view);
        songCoverImageView = findViewById(R.id.song_cover_image_view);
        songGifImageView = findViewById(R.id.song_gif_image_view);
        playerView = findViewById(R.id.player_view);
        nowplaying = findViewById(R.id.nowplaying);
        lyrics = findViewById(R.id.lyrics);
        favButton = findViewById(R.id.fav);
        menu = findViewById(R.id.menu);
        back = findViewById(R.id.back);
        updateProgressBar();


        song_title_text_view42 = findViewById(R.id.song_title_text_view42);
        song_title_text_view442 = findViewById(R.id.song_title_text_view442);
        song_cover_image_view782 = findViewById(R.id.song_cover_image_view782);

        back.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler4));
        favButton.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler4));
        menu.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler4));
        nowplaying.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler3));
        title.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler3));
        subtitle.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler3));
        lyrics.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler3));
        playerView.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.slide_from_bottom2));
        songCoverImageView.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.recycler3));


        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            coverUrl = currentSong.getCoverUrl(); // Assuming getCoverUrl() returns the cover URL
            // Other initialization code...

        } else {
            //Toast.makeText(this, "Snog ", Toast.LENGTH_SHORT).show();
            title.setVisibility(View.INVISIBLE);
            subtitle.setVisibility(View.INVISIBLE);
            nowplaying.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            menu.setVisibility(View.INVISIBLE);
            lyrics.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            favButton.setVisibility(View.INVISIBLE);
            alert.setVisibility(View.VISIBLE);
        }

        progressBar2 = findViewById(R.id.progress_bar);

        setRecentlyPlayed(MediaPlayerManager.getCurrentSong());
        // Inside your onCreate() method of PlayerActivity
        ImageView downloadButton2 = findViewById(R.id.download);
        downloadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the permission is granted
                if (ContextCompat.checkSelfPermission(PlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, proceed with download
                    downloadCurrentSong();
                } else {
                    // Permission is denied, display a message or request the permission again
                    ActivityCompat.requestPermissions(PlayerActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        notificationManager = new MediaPlayerNotificationManager(this);



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareCurrentMusic();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayerActivity.this, MainActivity2.class));
                finish();
            }
        });

        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayerActivity.this, LyricsActivity.class));
                overridePendingTransition(R.anim.slid_from_right, R.anim.slid_to_left);
            }
        });
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        if (MediaPlayerManager.getCurrentSong() != null) {

            TextView song_title_text_view78 = findViewById(R.id.song_title_text_view78);
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            setTitle(currentSong.getTitle());
            title.setText(currentSong.getTitle());
            title.setSelected(true);
            subtitle.setText(currentSong.getSubtitle());
            nowplaying.setText(currentSong.getTitle());
            lyrics.setText(currentSong.getLyrics());
            lyrics.setSelected(true);


            recyclerView = findViewById(R.id.suggesedrecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            songList = new ArrayList<>();
            adapter = new SearchAdapter8(this, songList);
            recyclerView.setAdapter(adapter);

            ImageView more = findViewById(R.id.lyrics2445);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fetchData();
                }
            });


            Suggestedtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fetchData();

                }
            });

            fetchData();
            song_title_text_view42.setText(currentSong.getTitle());
            song_title_text_view442.setText(currentSong.getSubtitle());

            if (currentSong.getLyrics() != null) {
                song_cover_image_view782.setText(currentSong.getLyrics());
            } else {
                song_cover_image_view782.setText("Lyrics Couldn't Load");
            }

            Glide.with(this)
                    .load(currentSong.getCoverUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(songCoverImageView);
            Glide.with(this)
                    .load(R.drawable.media_playing)
                    .apply(RequestOptions.circleCropTransform())
                    .into(songGifImageView);

            Glide.with(this)
                    .load(currentSong.getArtist())
                    .placeholder(R.drawable.picture)
                    .into(song_cover_image_view78);

            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PlayListAritistActivity.class);
                    intent.putExtra("MOVIE_NAME", currentSong.getName());
                    intent.putExtra("MOVIE_COVER_URL", currentSong.getArtist());
                    intent.putExtra("MOVIE_TITLE", currentSong.getName());
                    intent.putExtra("SUB_TITLE", currentSong.getName());
                    view.getContext().startActivity(intent);
                }
            });


            if (currentSong.getName() != null) {

                song_title_text_view78.setText(currentSong.getName());
            } else {
                song_title_text_view78.setText("Artist Name");
            }


            exoPlayer = MediaPlayerManager.getInstance();
            exoPlayer.setShuffleModeEnabled(true);
            playerView.setPlayer(exoPlayer);
            playerView.showController();
            exoPlayer.addListener(playerListener);


            TextView song_title_text_view4 = findViewById(R.id.song_title_text_view4);

            initSeekBar();

            playButton = findViewById(R.id.play_button);
            pauseButton = findViewById(R.id.pause_button);

            ImageView rewindButton = findViewById(R.id.rewindButton);
            ImageView fastForwardButton = findViewById(R.id.fastForwardButton);

            rewindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long currentPosition = exoPlayer.getCurrentPosition();
                    long rewindPosition = currentPosition - 10000; // Rewind 10 seconds
                    exoPlayer.seekTo(Math.max(rewindPosition, 0));
                }
            });

            // Implement fast forward button functionality
            fastForwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long currentPosition = exoPlayer.getCurrentPosition();
                    long duration = exoPlayer.getDuration();
                    long forwardPosition = currentPosition + 10000; // Fast forward 10 seconds
                    exoPlayer.seekTo(Math.min(forwardPosition, duration));
                }
            });


            if (exoPlayer.isPlaying()) {
                pauseButton.setVisibility(View.VISIBLE);
               // Toast.makeText(getApplicationContext(), "Playback paused", Toast.LENGTH_SHORT).show();
                showGif(true);
            } else {
               // Toast.makeText(getApplicationContext(), "paused", Toast.LENGTH_SHORT).show();
                playButton.setVisibility(View.VISIBLE);
            }


            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exoPlayer != null && !exoPlayer.isPlaying()) {
                        exoPlayer.play();
                        isPlaying = true; // Update playback state
                        updateButtonVisibility(); // Update button visibility
                        showGif(true); // Show GIF when playing
                        MediaPlayerManager.play();
                        showNotification();
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
                        showGif(false); // Hide GIF when paused

                        MediaPlayerManager.pause();
                        showNotification();
                    }
                }
            });


            Long count = currentSong.getCount();
            int i = 100;

            if (currentSong.getCount() != null) {
                if (count > i) {
                    song_title_text_view4.setText(String.valueOf(currentSong.getCount() + "\t" + "Mostly Popular Listeners"));
                } else {
                    song_title_text_view4.setText(String.valueOf(currentSong.getCount() + "\t" + "Mostly Listeners"));
                }
            }


            ImageView lyrics2 = findViewById(R.id.lyrics2);
            lyrics2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PlayerActivity.this, LyricsActivity.class));
                }
            });


            ImageView nowplaying = findViewById(R.id.menu);
            nowplaying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyBottomSheetFragment bottomSheetFragment = new MyBottomSheetFragment();
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                }
            });


        }


        // Check if the current song is in favorites
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("favorites");
            userFavoritesRef.orderByChild("title").equalTo(title.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Song is in favorites, set unfavorite icon
                        favButton.setImageResource(R.drawable.baseline_favorite_24un);
                    } else {
                        // Song is not in favorites, set favorite icon
                        favButton.setImageResource(R.drawable.baseline_favorite_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }


        mediaReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("MEDIA_PLAYER_UPDATE".equals(intent.getAction())) {
                    updateUI();
                   // Toast.makeText(PlayerActivity.this, "UI updated", Toast.LENGTH_SHORT).show(); // Change context
                }
            }
        };

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("MEDIA_PLAYER_UPDATE");
        registerReceiver(mediaReceiver, filter2);

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction("ACTION_PREVIOUS");
        filter3.addAction("ACTION_PLAY");
        filter3.addAction("ACTION_PAUSE");
        filter3.addAction("ACTION_NEXT");

        MediaPlayerBroadcastReceiver receiver = new MediaPlayerBroadcastReceiver();
        registerReceiver(receiver, filter3);

        Log.d("PlayerActivity", "BroadcastReceiver registered");


    }


    private void updateUI() {
        if (MediaPlayerManager.isPlaying()) {
            pauseButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.INVISIBLE);
        } else {
            pauseButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.removeListener(playerListener);
        }

        unregisterReceiver(mediaReceiver);
    }


    private void showGif(boolean show) {
        songGifImageView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showpausebutton() {
        pauseButton.setVisibility(View.VISIBLE);
        showNotification();
    }


    private void toggleFavorite() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("favorites");
            userFavoritesRef.orderByChild("title").equalTo(title.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Song is already in favorites, remove it
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            childSnapshot.getRef().removeValue();
                        }
                        // Update UI to show favorite icon
                        favButton.setImageResource(R.drawable.baseline_favorite_24);
                        showToast("Removed from favorites");
                    } else {
                        // Song is not in favorites, add it
                        String songUid = userFavoritesRef.push().getKey(); // Generate a unique key for the song
                        userFavoritesRef.child(songUid).child("title").setValue(title.getText().toString());
                        userFavoritesRef.child(songUid).child("subtitle").setValue(subtitle.getText().toString());
                        userFavoritesRef.child(songUid).child("coverUrl").setValue(coverUrl); // Set the coverUrl
                        userFavoritesRef.child(songUid).child("url").setValue(MediaPlayerManager.getCurrentSong().getUrl()); // Set the coverUrl
                        // Add other song details as needed

                        // Update UI to show unfavorite icon
                        favButton.setImageResource(R.drawable.baseline_favorite_24un);
                        showToast("Added to favorites");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlayerActivity.this, MainActivity2.class));
        overridePendingTransition(R.anim.slid_from_top, R.anim.slid_to_bottom);
        finish();
    }







    private void shareCurrentMusic() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            String songLink = currentSong.getUrl();

            // Load the song's image asynchronously using Glide or any other image loading library
            Glide.with(this)
                    .asBitmap()
                    .load(currentSong.getCoverUrl()) // Assuming you have a method getCoverUrl() in SongModel to get the URL of the cover image
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            // Share music details and image
                            shareMusicDetailsWithImage(currentSong, songLink, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Handle case where the bitmap loading is cleared
                        }
                    });
        } else {
            showToast("No music is currently playing");
        }
    }

    private void shareMusicDetailsWithImage(SongModel song, String songLink, Bitmap imageBitmap) {
        if (songLink != null && !songLink.isEmpty()) {
            String shareText = "Now playing: " + song.getTitle() + " - " + song.getSubtitle() + "\nListen here: " + songLink;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");

            // Add image and text to the share intent
            shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(imageBitmap)); // getImageUri() converts Bitmap to Uri
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Now playing");

            startActivity(Intent.createChooser(shareIntent, "Share music via"));
        } else {
            showToast("No link available for this song");
        }
    }

    private Uri getImageUri(Bitmap imageBitmap) {
        // Save the image to cache directory and get its Uri
        File cachePath = new File(getCacheDir(), "images");
        cachePath.mkdirs(); // Make sure the directory exists
        try {
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(cachePath + "/image.png"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void downloadCurrentSong() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            String songUrl = currentSong.getUrl();
            String fileName = "song_name.mp3"; // Provide a suitable file name

            // Use DownloadManager to handle the download process
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(songUrl);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(currentSong.getTitle());
            request.setDescription("Downloading " + currentSong.getTitle());
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName);

            downloadManager.enqueue(request);

          //  Toast.makeText(this, "Downloading..." + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No song is currently playing", Toast.LENGTH_SHORT).show();
        }
    }

    // Override onRequestPermissionsResult() to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with your app logic
            } else {
                // Permission is denied, handle this case (e.g., display a message)
                Toast.makeText(this, "Permission denied,give permission to download song", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setRecentlyPlayed(SongModel song) {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = Auth.getCurrentUser();
        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRecentRef = Database.child("Users").child(userId).child("recently_played");

            userRecentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get the number of recently played songs
                    long count = dataSnapshot.getChildrenCount();

                    // If there are more than 10 songs, remove the oldest ones
                    if (count >= 10) {
                        long excess = count - 10;
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        for (int i = 0; i < excess && iterator.hasNext(); i++) {
                            DataSnapshot snapshot = iterator.next();
                            snapshot.getRef().removeValue();
                        }
                    }

                    // Set the new song as the recently played song
                    String songKey = userRecentRef.push().getKey(); // Generate a unique key for the song
                    userRecentRef.child(songKey).setValue(song);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }


    }

    private void updateProgressBar() {
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
                updateProgressBar();
            }
        }, 1000); // Update progress bar every second (1000 milliseconds)
    }

    public void goback(View view) {
        startActivity(new Intent(PlayerActivity.this, MainActivity.class));
        finish();
    }


    public void playnext(View view) {
        MediaPlayerManager.playNext(view.getContext());
    }

    private BroadcastReceiver songChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract song details from the Intent


            String songTitle = intent.getStringExtra("songTitle");

            // Show a Toast with the song title
         //   Toast.makeText(PlayerActivity.this, "Now playing: " + songTitle, Toast.LENGTH_SHORT).show();

            if (MediaPlayerManager.getCurrentSong() != null) {

                TextView song_title_text_view78 = findViewById(R.id.song_title_text_view78);
                SongModel currentSong = MediaPlayerManager.getCurrentSong();
                setTitle(currentSong.getTitle());
                title.setText(currentSong.getTitle());
                title.setSelected(true);
                subtitle.setText(currentSong.getSubtitle());
                nowplaying.setText(currentSong.getTitle());
                lyrics.setText(currentSong.getLyrics());
                lyrics.setSelected(true);

                showNotification();

                song_title_text_view42.setText(currentSong.getTitle());
                song_title_text_view442.setText(currentSong.getSubtitle());

                fetchData();

                if (currentSong.getLyrics() != null) {
                    song_cover_image_view782.setText(currentSong.getLyrics());
                } else {
                    song_cover_image_view782.setText("Lyrics Couldn't Load");
                }

                try {
                    Glide.with(PlayerActivity.this)
                            .load(currentSong.getCoverUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(songCoverImageView);
                    Glide.with(PlayerActivity.this)
                            .load(R.drawable.media_playing)
                            .apply(RequestOptions.circleCropTransform())
                            .into(songGifImageView);

                    Glide.with(PlayerActivity.this)
                            .load(currentSong.getArtist())
                            .placeholder(R.drawable.picture)
                            .into(song_cover_image_view78);
                } catch (Exception e) {

                }


                song_cover_image_view78.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ArtistFullActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                if (currentSong.getName() != null) {

                    song_title_text_view78.setText(currentSong.getName());
                } else {
                    song_title_text_view78.setText("Artist Name");
                }


                exoPlayer = MediaPlayerManager.getInstance();
                exoPlayer.setShuffleModeEnabled(true);
                playerView.setPlayer(exoPlayer);
                playerView.showController();
                exoPlayer.addListener(playerListener);


                if (exoPlayer.isPlaying()) {
                    pauseButton.setVisibility(View.VISIBLE);
                   // Toast.makeText(getApplicationContext(), "Playback paused", Toast.LENGTH_SHORT).show();
                    showGif(true);
                } else {
                 //   Toast.makeText(getApplicationContext(), "paused", Toast.LENGTH_SHORT).show();
                    playButton.setVisibility(View.VISIBLE);

                }


                TextView song_title_text_view4 = findViewById(R.id.song_title_text_view4);

                Long count = currentSong.getCount();
                int i = 100;

                if (currentSong.getCount() != null) {
                    if (count > i) {
                        song_title_text_view4.setText(String.valueOf(currentSong.getCount() + "\t" + "Mostly Popular Listeners"));
                    } else {
                        song_title_text_view4.setText(String.valueOf(currentSong.getCount() + "\t" + "Mostly Listeners"));
                    }
                }


                ImageView lyrics2 = findViewById(R.id.lyrics2);
                lyrics2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(PlayerActivity.this, LyricsActivity.class));
                    }
                });


                ImageView nowplaying = findViewById(R.id.menu);
                nowplaying.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyBottomSheetFragment bottomSheetFragment = new MyBottomSheetFragment();
                        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    }
                });


            }
        }
    };

    private void updateButtonVisibility() {
        if (isPlaying) {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        } else {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        }
    }


    public void playPrevious(View view) {
        MediaPlayerManager.playPrevious(view.getContext());
    }


    private void initSeekBar() {
        if (exoPlayer == null) return;

        // Set SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && exoPlayer != null) {
                    long newPosition = (exoPlayer.getDuration() * progress) / 100;
                    exoPlayer.seekTo(newPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateSeekBar.run(); // Ensure the SeekBar is updated after tracking
                handler.postDelayed(updateSeekBar, 1000); // Resume updating after a short delay
            }
        });

        // Update SeekBar and time display
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null) {
                    long currentPosition = exoPlayer.getCurrentPosition();
                    long duration = exoPlayer.getDuration();
                    seekBar.setMax((int) (duration / 1000)); // Set max value in seconds
                    seekBar.setProgress((int) (currentPosition / 1000));

                    currentTime.setText(formatTime(currentPosition));
                    totalTime.setText(formatTime(duration));

                    handler.postDelayed(this, 1000); // Update every second
                }
            }
        };

        handler.post(updateSeekBar); // Start updating immediately
    }


    private String formatTime(long timeInMillis) {
        int minutes = (int) (timeInMillis / (1000 * 60));
        int seconds = (int) ((timeInMillis / 1000) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }


    private void fetchData() {
        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        songList.clear(); // Clear existing data
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String songTitle = documentSnapshot.getString("title");
                                String moviename = documentSnapshot.getString("moviename");
                                // Only process songs with title "beast"

                                try {
                                    if (MediaPlayerManager.getCurrentSong().getMoviename().equals(moviename)) {
                                        String subtitle = documentSnapshot.getString("subtitle");
                                        String coverUrl = documentSnapshot.getString("coverUrl");
                                        String Url = documentSnapshot.getString("url");
                                        String id = documentSnapshot.getString("id");
                                        String lyrics = documentSnapshot.getString("lyrics");
                                        String artist = documentSnapshot.getString("artist");
                                        String name = documentSnapshot.getString("name");
                                        Long count = documentSnapshot.getLong("count");
                                        String key = documentSnapshot.getId();
                                        SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics, artist, name, moviename, count);

                                        songList.add(song);
                                    }
                                } catch (Exception e) {

                                }

                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter after data change
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchActivity", "Error fetching songs", e);
                    }
                });

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
                                MediaPlayerNotificationManager notificationManager = new MediaPlayerNotificationManager(PlayerActivity.this);

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
            } catch (Exception e) {

            }
            // Load the song's image bitmap using Glide or any other image loading library

        }
    }

}
