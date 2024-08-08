package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class LyricsActivity extends AppCompatActivity {

    TextView title1,subtitl1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        // Find views in your layout by their IDs
        TextView lyricsTextView = findViewById(R.id.categorySongsTextView);
        title1 = findViewById(R.id.title);
        subtitl1 = findViewById(R.id.subtitle);
        ImageView coverImageView = findViewById(R.id.categoryCoverUrl);

        lyricsTextView.startAnimation(AnimationUtils.loadAnimation(LyricsActivity.this, R.anim.recycler4));
        title1.startAnimation(AnimationUtils.loadAnimation(LyricsActivity.this, R.anim.recycler));
        subtitl1.startAnimation(AnimationUtils.loadAnimation(LyricsActivity.this, R.anim.recycler));
        coverImageView.startAnimation(AnimationUtils.loadAnimation(LyricsActivity.this, R.anim.recycler2));

        // Retrieve the current song
        SongModel currentSong = MediaPlayerManager.getCurrentSong();

        // Set lyrics to the TextView
        if (currentSong != null) {
            String lyrics = currentSong.getLyrics();
            if (lyrics != null && !lyrics.isEmpty()) {
                lyricsTextView.setText(lyrics);
            } else {
                lyricsTextView.setText("No lyrics available");
            }

            // Load and set cover image using Glide
            String coverUrl = currentSong.getCoverUrl();
            Glide.with(this)
                    .load(coverUrl)// Placeholder image while loading
                    .into(coverImageView);
            title1.setText("Title : "+currentSong.getTitle());
            subtitl1.setText("Subtitle : "+currentSong.getSubtitle());
        } else {
            // If no song is playing, set default text and image
            lyricsTextView.setText("No song is currently playing");

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_from_left,R.anim.slid_to_right);
    }
}
