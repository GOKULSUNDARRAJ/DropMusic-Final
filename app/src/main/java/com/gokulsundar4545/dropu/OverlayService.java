package com.gokulsundar4545.dropu;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import de.hdodenhof.circleimageview.CircleImageView;

public class OverlayService extends Service {

    private static final String TAG = "OverlayService"; // Logging tag
    private WindowManager windowManager;
    private View overlayView;

    ImageView songcoverimage;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: OverlayService started");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the overlay layout
        overlayView = inflater.inflate(R.layout.overlay_layout, null);
        Log.d(TAG, "onCreate: Overlay layout inflated");

        // Set the layout parameters for the overlay window
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        params.y = 100;

        // Add the overlay view to the window
        windowManager.addView(overlayView, params);
        Log.d(TAG, "onCreate: Overlay view added to window");

        overlayView.findViewById(R.id.NextButton).setOnClickListener(v -> {
            Log.d(TAG, "Next button clicked");
            playNext(v);
        });

        overlayView.findViewById(R.id.previousButton).setOnClickListener(v -> {
            Log.d(TAG, "Previous button clicked");
            playPrevious(v);
        });

        songcoverimage = overlayView.findViewById(R.id.songimage);

        // Load the current song image
        SongModel currentSong = MediaPlayerManager.getCurrentSong();
        if (currentSong != null) {
            Log.d(TAG, "onCreate: Current song found: " + currentSong.getTitle());

            Glide.with(OverlayService.this)
                    .load(currentSong.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(songcoverimage);


            Glide.with(OverlayService.this)
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
                            Palette.from(resource).generate(palette -> {
                                // Get the dominant color
                                int dominantColor = palette.getDominantColor(getResources().getColor(android.R.color.black));
                                // Set the background color of the card based on the dominant color
                                overlayView.findViewById(R.id.cardparoduct).setBackgroundColor(dominantColor);
                            });
                            return false;
                        }
                    }).into(songcoverimage);



        } else {
            Log.w(TAG, "onCreate: No current song found");
        }

        IntentFilter filter = new IntentFilter("SONG_CHANGED");
        registerReceiver(songChangeReceiver, filter);


        overlayView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Remember the initial position
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // Calculate the new position
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        // Update the position of the overlay
                        windowManager.updateViewLayout(overlayView, params);
                        return true;
                }
                return false;
            }
        });

        overlayView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOverlay();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) {
            windowManager.removeView(overlayView);
            Log.d(TAG, "onDestroy: Overlay view removed");
        }
        Log.d(TAG, "onDestroy: OverlayService stopped");
    }

    public void playPrevious(View view) {
        Log.d(TAG, "playPrevious: Playing previous song");
        MediaPlayerManager.playPrevious(view.getContext());
        showNotification();
    }

    public void playNext(View view) {
        Log.d(TAG, "playNext: Playing next song");
        MediaPlayerManager.playNext(view.getContext());
        showNotification();
    }

    private void showNotification() {
        SongModel currentSong = MediaPlayerManager.getCurrentSong();
        if (currentSong != null) {
            Log.d(TAG, "showNotification: Now Playing: " + currentSong.getTitle());

            String title = "Now Playing: " + currentSong.getTitle();
            String subtitle = currentSong.getSubtitle();

            try {
                Glide.with(this)
                        .asBitmap()
                        .load(currentSong.getCoverUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Log.d(TAG, "showNotification: Notification image loaded");

                                MediaPlayerNotificationManager notificationManager = new MediaPlayerNotificationManager(OverlayService.this);

                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.music);

                                notificationManager.showNotification(title, subtitle, largeIcon, resource);
                                Log.d(TAG, "showNotification: Notification shown");
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                Log.d(TAG, "showNotification: Image load cleared");
                            }
                        });
            } catch (Exception e) {
                Log.e(TAG, "showNotification: Error showing notification", e);
            }
        } else {
            Log.w(TAG, "showNotification: No current song to display in notification");
        }
    }

    private BroadcastReceiver songChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            overlayView.findViewById(R.id.NextButton).setOnClickListener(v -> {
                Log.d(TAG, "Next button clicked");
                playNext(v);
            });

            overlayView.findViewById(R.id.previousButton).setOnClickListener(v -> {
                Log.d(TAG, "Previous button clicked");
                playPrevious(v);
            });

            songcoverimage = overlayView.findViewById(R.id.songimage);

            // Load the current song image
            SongModel currentSong = MediaPlayerManager.getCurrentSong();
            if (currentSong != null) {
                Log.d(TAG, "onCreate: Current song found: " + currentSong.getTitle());

                Glide.with(OverlayService.this)
                        .load(currentSong.getCoverUrl())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(songcoverimage);



                Glide.with(OverlayService.this)
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
                                Palette.from(resource).generate(palette -> {
                                    // Get the dominant color
                                    int dominantColor = palette.getDominantColor(getResources().getColor(android.R.color.black));
                                    // Set the background color of the card based on the dominant color
                                    overlayView.findViewById(R.id.cardparoduct).setBackgroundColor(dominantColor);
                                });
                                return false;
                            }
                        }).into(songcoverimage);


            } else {
                Log.w(TAG, "onCreate: No current song found");
            }
        }
    };

    private void removeOverlay() {
        if (overlayView != null && windowManager != null) {
            windowManager.removeView(overlayView);
            overlayView = null;  // Clean up reference
            Log.d(TAG, "Overlay removed");
            stopSelf();  // Stop the service if desired
        }
    }

}
