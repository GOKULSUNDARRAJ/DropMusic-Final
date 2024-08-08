package com.gokulsundar4545.dropu;


import android.Manifest;
import android.app.Activity;
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

public class MyBottomSheetFragment3 extends BottomSheetDialogFragment {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private SongModel songModel;

    private static final int REQUEST_WRITE_SETTINGS = 1001;

    // Constructor to accept parameters
    public MyBottomSheetFragment3(SongModel songModel) {
        this.songModel = songModel;
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ImageView playliist1, bar;
    LinearLayout download, share, playlist, download061, download065, download006;
    TextView notification5705;


    ImageView songImage1;
    TextView title1, subtitle1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_bottom_sheet2, container, false);
        download = view.findViewById(R.id.download);
        share = view.findViewById(R.id.share);

        songImage1 = view.findViewById(R.id.songImage);
        title1 = view.findViewById(R.id.title);
        subtitle1 = view.findViewById(R.id.subtitle);

        Glide.with(this)
                .load(songModel.getCoverUrl())
                .into(songImage1);

        // Set the title and subtitle to their respective TextViews
        title1.setText(songModel.getTitle());
        subtitle1.setText(songModel.getSubtitle());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        notification5705 = view.findViewById(R.id.notification5705);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermissionAndDownload(songModel);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareCurrentMusic();
            }
        });

        playliist1 = view.findViewById(R.id.imageView4);

        playlist = view.findViewById(R.id.download05);

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite();
            }
        });

        // Check if the current song is in the playlist
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userPlaylistRef = mDatabase.child("Users").child(userId).child("playlist");
            userPlaylistRef.orderByChild("title").equalTo(songModel.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        download061 = view.findViewById(R.id.download061);

        download061.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySongUrlToClipboard();
            }
        });
        download065 = view.findViewById(R.id.download054);

        download065.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogforoR2 customDialog = new CustomDialogforoR2(getContext(), songModel);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
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
                        // Permission has already been granted
                        // Perform the action that requires this permission
                        setRingtoneForCurrentSong();
                    }
                }

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void checkStoragePermissionAndDownload(SongModel songModel) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions((Activity) getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, proceed with download
            downloadSong(songModel);
        }
    }


    private void shareCurrentMusic() {


        String songLink = songModel.getUrl();

        // Load the song's image asynchronously using Glide
        Glide.with(requireContext())
                .asBitmap()
                .load(songModel.getCoverUrl()) // Assuming you have a method getCoverUrl() in SongModel to get the URL of the cover image
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Share music details and image
                        shareMusicDetailsWithImage(songModel, songLink, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle case where the bitmap loading is cleared
                    }
                });

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
            Toast.makeText(getContext(), "No link available for this song", Toast.LENGTH_SHORT).show();
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


    private void downloadSong(SongModel songModel) {
        // Example download logic (replace with your implementation)
        String downloadUrl = songModel.getUrl();
        String fileName = songModel.getTitle() + ".mp3";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle(songModel.getTitle());
        request.setDescription("Downloading");

        // Set destination folder and file name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            // Enqueue the download
            downloadManager.enqueue(request);
            Toast.makeText(getContext(), "Downloading " + songModel.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Download Manager not available", Toast.LENGTH_SHORT).show();
        }
    }


    private void toggleFavorite() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("playlist");

            // Get the current song details
            String songTitle = songModel.getTitle();
            String songSubtitle = songModel.getSubtitle();
            // Assuming you have a variable to store coverUrl
            String songCoverUrl = songModel.getCoverUrl();

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
                        userFavoritesRef.child(songUid).child("url").setValue(songModel.getUrl());// Set the coverUrl

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

        String songUrl = songModel.getUrl();
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

    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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

    private void setRingtoneForCurrentSong() {


        String songUrl = songModel.getUrl();

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

    }
}

