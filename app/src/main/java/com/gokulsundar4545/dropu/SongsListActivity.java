package com.gokulsundar4545.dropu;

import static android.content.ContentValues.TAG;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
public class SongsListActivity extends AppCompatActivity {
    private List<String> songsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ImageView categoryCoverUrl;
    private TextView categorySongsTextView;
    private Intent intent;
    private FirebaseFirestore db;;
    List<SongModel> songModelList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);

        // Initialize views
        categoryCoverUrl = findViewById(R.id.categoryCoverUrl);
        categorySongsTextView = findViewById(R.id.categorySongsTextView);
        recyclerView = findViewById(R.id.recyclerview);

        // Retrieve the CategoryModel object from the Intent extras
        intent = getIntent();
        if (intent != null && intent.hasExtra("category")) {
            CategoryModel category = (CategoryModel) intent.getSerializableExtra("category");

            // Load category cover image
            Glide.with(this)
                    .load(category.getCoverUrl())
                    .into(categoryCoverUrl);

            androidx.coordinatorlayout.widget.CoordinatorLayout card;
            card = findViewById(R.id.card);

            com.google.android.material.appbar.CollapsingToolbarLayout collaps;
            collaps = findViewById(R.id.collapslayput);
            collaps.setTitle(category.getName());

            Glide.with(this)
                    .asBitmap()
                    .load(category.getCoverUrl())
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
                                    recyclerView.setBackgroundColor(dominantColor);
                                    card.setBackgroundColor(dominantColor);
                                    collaps.setBackgroundColor(dominantColor);
                                }
                            });
                            return false;
                        }
                    })
                    .into(categoryCoverUrl);

            // Use the category object as needed
            if (category != null) {
                songsList = category.getSongs();
                // Initialize and set adapter
                songAdapter = new SongAdapter(songsList,songModelList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(songAdapter);

                // Show list of songs in a toast

                ImageView imageView5d=findViewById(R.id.imageView5d);
                imageView5d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSongsListInToast(songsList);
                    }
                });



            }
        } else {
            // Handle case where no category object was passed
            Toast.makeText(this, "No category data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (intent.getBooleanExtra("fromCategoryActivity", false)) {
            Intent intent = new Intent(SongsListActivity.this, ProdcastActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SongsListActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void showSongsListInToast(List<String> songsList) {
        // Initialize Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Iterate over each song title in the list
        for (String songTitle : songsList) {
            // Display each song title in a Toast message


            // Retrieve the document corresponding to the current song title from Firestore
            db.collection("song")
                    .whereEqualTo("id", songTitle) // Assuming 'title' is the field name in Firestore
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Check if any matching document was found
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Document with the song title exists

                            // Handle document data if needed
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                // Example: Retrieve other data from the document
                                String url=documentSnapshot.getString("url");

                                initiateSongDownload(url, songTitle);
                                // Use the retrieved data as needed

                            }
                        } else {
                            // No document found with the song title

                        }
                    })
                    .addOnFailureListener(e -> {
                        // Failed to retrieve document(s) from Firestore
                        Log.e(TAG, "Error fetching song: " + songTitle, e);
                    });
        }
    }

    private void downloadSong(String url, String songTitle) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(songTitle);
        request.setDescription("Downloading " + songTitle);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songTitle + ".mp3");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Downloading " + songTitle, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Download Manager not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Use this method to trigger the download based on the song URL
    private void initiateSongDownload(String songUrl, String songTitle) {
        // Example usage:
        downloadSong(songUrl, songTitle);
    }

}

