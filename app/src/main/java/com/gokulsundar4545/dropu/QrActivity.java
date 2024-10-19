package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QrActivity extends AppCompatActivity {

    private String getCurrentSongUrl() {
        // Return the URL of the current song
        return MediaPlayerManager.getCurrentSong().getUrl();
    }

    // Method to generate QR code from text
    private Bitmap generateQRCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri saveBitmap(Bitmap bitmap) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs(); // Make sure directory exists
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // Overwrite this file every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.parse("file://" + getCacheDir() + "/images/image.png");
    }

    ImageView itemImage,itemImage2;

    TextView title, subtitle,nosong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        itemImage = findViewById(R.id.itemImage);
        itemImage2 = findViewById(R.id.itemImage2);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        nosong=findViewById(R.id.nosong);

        if (MediaPlayerManager.getCurrentSong() != null) {
            title.setText(MediaPlayerManager.getCurrentSong().getTitle());
            subtitle.setText(MediaPlayerManager.getCurrentSong().getSubtitle());
            Glide.with(this)
                    .load(MediaPlayerManager.getCurrentSong().getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(itemImage);
            nosong.setVisibility(View.GONE);
        }

        try {
            ImageView qrCodeImageView = findViewById(R.id.song_cover_image_view);

            // Generate QR code for the current song's URL
            String currentSongUrl = getCurrentSongUrl();
            if (currentSongUrl != null && !currentSongUrl.isEmpty()) {
                Bitmap qrCodeBitmap = generateQRCode(currentSongUrl);
                if (qrCodeBitmap != null) {
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);

                    // Set OnClickListener for the QR code image
                    itemImage2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Share the QR code image
                            shareImageAndText(qrCodeBitmap,title.getText().toString());
                        }
                    });
                } else {
                    // Handle case where QR code generation failed
                    // For example, display an error message
                }
            } else {
                // Handle case where current song URL is not available
                // For example, display a message indicating no song is playing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to share the QR code image and title text
    private void shareImageAndText(Bitmap bitmap, String titleText) {
        try {
            // Save the bitmap to device storage
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs(); // Make sure directory exists
            File imageFile = new File(cachePath, "image.png");
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            // Get content URI for the saved image file
            Uri imageUri = FileProvider.getUriForFile(this, "com.gokulsundar4545.dropu.fileprovider", imageFile);


            // Create a share intentaza
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, titleText); // Include title text
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share QR Code and Title"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
