package com.gokulsundar4545.dropu;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyBottomSheetFragment extends BottomSheetDialogFragment {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private LinearLayout downloadSection;
    private LinearLayout shareSection, lyrics, playlist, download061, download065, download006, download0650, download056;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ImageView songImage1;
    TextView title1, subtitle1;

    SongModel currentSong;
    TextView notification5705;
    ImageView playliist1, bar;

    private static final int REQUEST_WRITE_SETTINGS = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        downloadSection = view.findViewById(R.id.download);
        shareSection = view.findViewById(R.id.share);
        download065 = view.findViewById(R.id.download054);
        download056 = view.findViewById(R.id.download056);

        download056.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactActivity.class);
                startActivity(intent);
            }
        });

        bar = view.findViewById(R.id.bar);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        download065.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogforoR cdd = new CustomDialogforoR(getContext());
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });

        download0650 = view.findViewById(R.id.download0650);
        download0650.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(requireContext())) {
                        requestWriteSettingsPermission();
                    } else {
                        // Permission has already been granted
                        // Perform the action that requires this permission

                        if (MediaPlayerManager.getCurrentSong() != null) {
                            // Get the current song details
                            SongModel currentSong = MediaPlayerManager.getCurrentSong();
                            String songUrl = currentSong.getUrl();
                            String songTitle = currentSong.getTitle();

                            // Assuming songUrl is the path to the song file
                            if (songUrl != null && !songUrl.isEmpty()) {
                                try {
                                    // Get the file descriptor of the song
                                    File songFile = new File(songUrl);
                                    Uri songUri = Uri.fromFile(songFile);

                                    // Set the ringtone for incoming calls using RingtoneManager
                                    RingtoneManager.setActualDefaultRingtoneUri(requireContext(), RingtoneManager.TYPE_RINGTONE, songUri);

                                    // Show a toast indicating that the caller tune has been set
                                    Toast.makeText(requireContext(), "Caller tune set to " + songTitle, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // Show a toast if setting the caller tune fails
                                    Toast.makeText(requireContext(), "Failed to set caller tune", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Show a toast if the song URL is not available
                                Toast.makeText(requireContext(), "No URL available for this song", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Show a toast if no song is currently playing
                            Toast.makeText(requireContext(), "No song is currently playing", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

            }
        });


        download006 = view.findViewById(R.id.download006);
        download006.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(requireContext())) {
                        requestWriteSettingsPermission();
                    } else {

                        setRingtoneForCurrentSong();
                    }
                }

            }
        });

        playlist = view.findViewById(R.id.download05);
        download061 = view.findViewById(R.id.download061);

        download061.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySongUrlToClipboard();
            }
        });

        songImage1 = view.findViewById(R.id.songImage);
        title1 = view.findViewById(R.id.title);
        subtitle1 = view.findViewById(R.id.subtitle);
        lyrics = view.findViewById(R.id.download0);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        notification5705 = view.findViewById(R.id.notification5705);
        lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LyricsActivity.class));
            }
        });

        playliist1 = view.findViewById(R.id.imageView4);

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite();
            }
        });


        if (MediaPlayerManager.getCurrentSong() != null) {
            currentSong = MediaPlayerManager.getCurrentSong();

            // Get the current song's image URL, title, and subtitle
            String title = currentSong.getTitle();
            String subtitle = currentSong.getSubtitle();

            // Set the image using Glide or any other image loading library
            Glide.with(this)
                    .load(currentSong.getCoverUrl())
                    .into(songImage1);

            // Set the title and subtitle to their respective TextViews
            title1.setText(title);
            subtitle1.setText(subtitle);
        } else {
            // Handle the case where no song is currently playing
        }


        // Set click listener for the download section
        downloadSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the permission is granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, proceed with download
                    downloadCurrentSong();
                } else {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        });


        shareSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCurrentMusic();
            }
        });


        // Check if the current song is in the playlist
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userPlaylistRef = mDatabase.child("Users").child(userId).child("playlist");
            userPlaylistRef.orderByChild("title").equalTo(title1.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Song is in the playlist, set remove from playlist icon
                        playliist1.setImageResource(R.drawable.baseline_remove_circle_outline_24);
                        notification5705.setText("Remove playlist");
                    } else {
                        // Song is not in the playlist, set add to playlist icon
                        playliist1.setImageResource(R.drawable.addplay);
                        notification5705.setText("Add to playlist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        shareSection.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        lyrics.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        playlist.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        download061.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        download065.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        download006.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        download0650.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        download056.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        downloadSection.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        songImage1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler4));
        title1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
        subtitle1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.recycler2));
    }

    private void downloadCurrentSong() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            String songUrl = currentSong.getUrl();
            String fileName = "song_name.mp3"; // Provide a suitable file name

            // Use DownloadManager to handle the download process
            DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(songUrl);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(currentSong.getTitle());
            request.setDescription("Downloading " + currentSong.getTitle());
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName);

            downloadManager.enqueue(request);

            Toast.makeText(requireContext(), "Downloading..." + currentSong.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "No song is currently playing", Toast.LENGTH_SHORT).show();
        }
    }

    // Override onRequestPermissionsResult() to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with download
                downloadCurrentSong();
            } else {
                // Permission is denied, handle this case (e.g., display a message)
                Toast.makeText(requireContext(), "Permission denied. Give permission to download song", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void shareCurrentMusic() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            String songLink = currentSong.getUrl();

            // Load the song's image asynchronously using Glide
            Glide.with(requireContext())
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

            // Save the image to cache directory and get its Uri
            Uri imageUri = saveImageToCache(imageBitmap);

            // Add image and text to the share intent
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Now playing");

            // Start the chooser to share music via
            startActivity(Intent.createChooser(shareIntent, "Share music via"));
        } else {
            showToast("No link available for this song");
        }
    }

    private Uri saveImageToCache(Bitmap imageBitmap) {
        File cachePath = new File(requireContext().getCacheDir(), "images");
        cachePath.mkdirs(); // Make sure the directory exists
        File imageFile = new File(cachePath, "image.png");

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the content Uri for the saved image
        return FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", imageFile);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void toggleFavorite() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("playlist");

            // Get the current song details
            String songTitle = title1.getText().toString();
            String songSubtitle = subtitle1.getText().toString();
            // Assuming you have a variable to store coverUrl
            String songCoverUrl = currentSong.getCoverUrl();

            userFavoritesRef.orderByChild("title").equalTo(songTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Song is already in favorites, remove it
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            childSnapshot.getRef().removeValue();
                        }
                        // Update UI to show favorite icon
                        // Assuming you have a favButton reference
                        playliist1.setImageResource(R.drawable.addplay);

                        notification5705.setText("Added playlist");
                        showToast("Removed from playlist");
                    } else {
                        // Song is not in favorites, add it
                        String songUid = userFavoritesRef.push().getKey(); // Generate a unique key for the song
                        userFavoritesRef.child(songUid).child("title").setValue(songTitle);
                        userFavoritesRef.child(songUid).child("subtitle").setValue(songSubtitle);
                        userFavoritesRef.child(songUid).child("coverUrl").setValue(songCoverUrl);
                        userFavoritesRef.child(songUid).child("url").setValue(MediaPlayerManager.getCurrentSong().getUrl());// Set the coverUrl

                        // Add other song details as needed

                        // Update UI to show unfavorite icon
                        // Assuming you have a favButton reference
                        playliist1.setImageResource(R.drawable.baseline_remove_circle_outline_24);
                        notification5705.setText("Remove playlist");
                        showToast("Add to playlist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }



    }

    private void copySongUrlToClipboard() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            String songUrl = MediaPlayerManager.getCurrentSong().getUrl();
            if (songUrl != null && !songUrl.isEmpty()) {
                // Get the ClipboardManager
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // Create a ClipData object to store the copied URL
                ClipData clip = ClipData.newPlainText("Song URL", songUrl);
                // Put the ClipData into the clipboard
                clipboard.setPrimaryClip(clip);
                // Show a toast indicating that the URL has been copied
                Toast.makeText(requireContext(), "Song URL copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where the song URL is not available
                Toast.makeText(requireContext(), "No URL available for this song", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where no song is currently playing
            Toast.makeText(requireContext(), "No song is currently playing", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRingtoneForCurrentSong() {
        if (MediaPlayerManager.getCurrentSong() != null) {
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            String songUrl = currentSong.getUrl();

            // Assuming songUrl is the path to the song file

            try {
                // Get the file descriptor of the song
                File songFile = new File(songUrl);
                Uri songUri = Uri.fromFile(songFile);

                // Set the ringtone using RingtoneManager
                RingtoneManager.setActualDefaultRingtoneUri(requireContext(), RingtoneManager.TYPE_RINGTONE, songUri);

                Toast.makeText(requireContext(), "Ringtone set successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to set ringtone", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where no song is currently playing
            Toast.makeText(requireContext(), "No song is currently playing", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(requireContext())) {
                // Permission is not granted, request the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + requireContext().getPackageName()));
                startActivityForResult(intent, REQUEST_WRITE_SETTINGS);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_WRITE_SETTINGS) {
            if (Settings.System.canWrite(requireContext())) {
                // Permission has been granted
                // Perform the action that requires this permission
                setRingtoneForCurrentSong();
            } else {
                // Permission has been denied or not granted
                // Handle the denial or inform the user
                Toast.makeText(requireContext(), "Permission denied. Cannot set ringtone.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

