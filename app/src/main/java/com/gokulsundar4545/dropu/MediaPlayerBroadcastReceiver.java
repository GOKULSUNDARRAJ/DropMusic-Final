package com.gokulsundar4545.dropu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MediaPlayerBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    private static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";
    private static final String ACTION_NEXT = "ACTION_NEXT";

    private MediaPlayerNotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) {
            Log.e("MediaPlayerReceiver", "Received null action");
            return;
        }

        // Process the action and manage the player accordingly
        switch (action) {
            case ACTION_PREVIOUS:
                MediaPlayerManager.playPrevious(context.getApplicationContext());
                break;

            case ACTION_PLAY:
                if (!MediaPlayerManager.isPlaying()) {
                    MediaPlayerManager.play();
                }
                break;

            case ACTION_PAUSE:
                if (MediaPlayerManager.isPlaying()) {
                    MediaPlayerManager.pause();
                }
                break;

            case ACTION_NEXT:
                MediaPlayerManager.playNext(context.getApplicationContext());
                break;

            default:
                Log.w("MediaPlayerReceiver", "Unknown action received: " + action);
                return;  // Exit if the action is not recognized
        }

        // Notify the PlayerActivity to update the UI
        notifyPlayerActivity(context);

        Log.d("MediaPlayerReceiver", "Action processed: " + action);
    }



    private void notifyPlayerActivity(Context context) {
        Intent updateIntent = new Intent("MEDIA_PLAYER_UPDATE");
        context.sendBroadcast(updateIntent); // Broadcast to update the UI in PlayerActivity
        Log.d("MediaPlayerReceiver", "Broadcast sent for UI update");
    }
}
