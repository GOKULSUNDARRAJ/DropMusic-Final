package com.gokulsundar4545.dropu;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

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
        PendingIntent previousIntent = getPendingIntentForAction("ACTION_PREVIOUS");
        PendingIntent playIntent = getPendingIntentForAction("ACTION_PLAY");
        PendingIntent pauseIntent = getPendingIntentForAction("ACTION_PAUSE");
        PendingIntent nextIntent = getPendingIntentForAction("ACTION_NEXT");
        // Inflate the custom layout
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);

        // Set the content in the custom layout
        notificationLayout.setTextViewText(R.id.notification_title, title);
        notificationLayout.setTextViewText(R.id.notification_subtitle, subtitle);
        notificationLayout.setImageViewBitmap(R.id.notification_big_picture, bigPicture);


        notificationLayout.setOnClickPendingIntent(R.id.previousButton, previousIntent);
        notificationLayout.setOnClickPendingIntent(R.id.play, playIntent);
        notificationLayout.setOnClickPendingIntent(R.id.pause, pauseIntent);
        notificationLayout.setOnClickPendingIntent(R.id.nextbtn, nextIntent);



        // Build the notification with the custom layout
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round) // Set the icon
                .setContent(notificationLayout) // Set the custom content view
                .setContentIntent(pendingIntent) // Set the pending intent
                .setAutoCancel(false) // Automatically cancel the notification when clicked
                .setCustomBigContentView(notificationLayout) // For expanded notifications
                .setOngoing(true); // Make it a persistent notification

        // Display the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private PendingIntent getPendingIntentForAction(String action) {
        Intent intent = new Intent(context, MediaPlayerBroadcastReceiver.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }


    public void cancelNotification() {
        // Cancel the notification
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
