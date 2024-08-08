package com.gokulsundar4545.dropu;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private List<String> songIds;

    private List<SongModel> songList;

    public SongAdapter(List<String> songIds, List<SongModel> songList) {
        this.songIds = songIds;
        this.songList = songList;
    }

    public String getSongTitles() {
        StringBuilder builder = new StringBuilder();
        for (SongModel song : songList) {
            builder.append(song.getTitle()).append("\n");
        }
        return builder.toString();
    }

    public SongAdapter(List<String> songIds) {
        this.songIds = songIds;
    }

    private SongModel getSongModel(DocumentSnapshot documentSnapshot) {
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
        return new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics, artist, name, count);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String songId = songIds.get(position);
        holder.bind(songId);
    }

    @Override
    public int getItemCount() {
        return songIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, subtitleTextView;
        private ImageView coverImageView,itemImage2;
        private LinearLayout carProduct;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.subtitle);
            coverImageView = itemView.findViewById(R.id.itemImage);
            carProduct = itemView.findViewById(R.id.carproduct);
            itemImage2=itemView.findViewById(R.id.itemImage2);
        }

        public void bind(String songId) {
            carProduct.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.recycler2));

            FirebaseFirestore.getInstance().collection("song")
                    .document(songId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                SongModel songModel = documentSnapshot.toObject(SongModel.class);
                                if (songModel != null) {
                                    titleTextView.setText(songModel.getTitle());
                                    subtitleTextView.setText(songModel.getSubtitle());
                                    Glide.with(context)
                                            .load(songModel.getCoverUrl())
                                            .into(coverImageView);

                                    carProduct.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            MediaPlayerManager.startPlaying(context, songModel);
                                            Intent intent = new Intent(context, PlayerActivity.class);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();
                                        }
                                    });

                                    itemImage2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // Create a new instance of MyBottomSheetFragment2 with songModel as parameter
                                            MyBottomSheetFragment2 bottomSheetFragment = new MyBottomSheetFragment2(songModel);

                                            // Show the bottom sheet fragment
                                            bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());
                                        }
                                    });



                                    carProduct.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View view) {
                                            MyBottomSheetFragment2 bottomSheetFragment = new MyBottomSheetFragment2(songModel);

                                            // Show the bottom sheet fragment
                                            bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());
                                            return true; // Indicate that the long click is consumed
                                        }
                                    });
                                }
                            } else {
                                Log.d("SongAdapter", "No such document for song ID: " + songId);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SongAdapter", "Error getting document for song ID: " + songId, e);
                        }
                    });
        }

        private void checkStoragePermissionAndDownload(SongModel songModel) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                // Permission already granted, proceed with download
                downloadSong(songModel);
            }
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

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                // Enqueue the download
                downloadManager.enqueue(request);
                Toast.makeText(context, "Downloading " + songModel.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Download Manager not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
