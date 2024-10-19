package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ZoomActivitynew extends AppCompatActivity {

    ImageView zoomImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_activitynew);


        zoomImageView = findViewById(R.id.zoomImageView);

        // Retrieve the image URL from the intent
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the image into the ImageView using Glide
        Glide.with(this)
                .load(imageUrl)

                .into(zoomImageView);
    }
}