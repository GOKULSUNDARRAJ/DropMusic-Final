package com.gokulsundar4545.dropu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class CustomDialogforoR extends Dialog {

    private Context context;

    public CustomDialogforoR(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qrcode);

        ImageView claer=findViewById(R.id.claer);
        claer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // Assuming you have an ImageView to display the QR code
        try {
            ImageView qrCodeImageView = findViewById(R.id.song_cover_image_view);

            // Generate QR code for the current song's URL
            String currentSongUrl = getCurrentSongUrl();
            if (currentSongUrl != null && !currentSongUrl.isEmpty()) {
                Bitmap qrCodeBitmap = generateQRCode(currentSongUrl);
                if (qrCodeBitmap != null) {
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);

                    TextView title=findViewById(R.id.title);
                    TextView subtitle=findViewById(R.id.subtitle);
                    ImageView songimage=findViewById(R.id.itemImage);
                    title.setText(MediaPlayerManager.getCurrentSong().getTitle());
                    subtitle.setText(MediaPlayerManager.getCurrentSong().getSubtitle());

                    Glide.with(getContext())
                            .load(MediaPlayerManager.getCurrentSong().getCoverUrl())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))) // Set corner radius here (e.g., 20)
                            .into(songimage);

                    songimage.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler));
                    title.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));
                    subtitle.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recycler2));

                } else {
                    // Handle case where QR code generation failed
                    // For example, display an error message
                }
            } else {
                // Handle case where current song URL is not available
                // For example, display a message indicating no song is playing
            }
        }catch (Exception e){

        }


    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }

    @Override
    public void onBackPressed() {
        // Do nothing on back press
    }


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



}
