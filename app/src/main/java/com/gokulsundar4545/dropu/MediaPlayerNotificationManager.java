package com.gokulsundar4545.dropu;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class MediaPlayerNotificationManager {

    private static final int NOTIFICATION_ID = 123; // Unique ID for the notification
    private static final String CHANNEL_ID = "Media_Player_Channel"; // Unique channel ID for the notification channel
    private static final String CHANNEL_NAME = "Media Player"; // Name of the notification channel

    private final Context context;
    private final NotificationManager notificationManager;

    public MediaPlayerNotificationManager(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create a notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void showNotification(String title, String subtitle, Bitmap largeIcon, Bitmap bigPicture) {
        // Intent to launch the player activity when the notification is clicked
        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Add FLAG_IMMUTABLE to ensure compatibility with Android S+
        int flags = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, playerIntent, flags);

        // Build the big picture style notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.mus123) // Set the icon
                .setContentTitle(title) // Set the title
                .setContentText(subtitle) // Set the subtitle
                .setLargeIcon(largeIcon) // Set the large icon (song image)
                .setContentIntent(pendingIntent) // Set the pending intent
                .setAutoCancel(false); // Automatically cancel the notification when clicked

        // Set big picture style
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(bigPicture)
                .setBigContentTitle(title)
                .setSummaryText(subtitle);

        builder.setStyle(bigPictureStyle);
        builder.setOngoing(true);

        // Display the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }



    public void cancelNotification() {
        // Cancel the notification
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
