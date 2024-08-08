package com.gokulsundar4545.dropu;


import android.content.Context;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MediaPlayerManager {

    private static ExoPlayer exoPlayer = null;
    private static SongModel currentSong = null;

    public static SongModel getCurrentSong() {
        return currentSong;
    }

    public static ExoPlayer getInstance() {
        return exoPlayer;
    }

    public static void startPlaying(Context context, SongModel song) {
        if (exoPlayer == null)
            exoPlayer = new ExoPlayer.Builder(context).build();

        if (!song.equals(currentSong)) {
            // It's a new song so start playing
            currentSong = song;
            updateCount();
            if (currentSong != null) {
                String url = currentSong.getUrl();
                if (url != null) {
                    MediaItem mediaItem = MediaItem.fromUri(url);
                    exoPlayer.setMediaItem(mediaItem);
                    exoPlayer.prepare();
                    exoPlayer.play();
                }
            }

        }


    }

    public static void updateCount() {
        if (currentSong != null) {
            String id = currentSong.getId();
            if (id != null) {
                FirebaseFirestore.getInstance().collection("song")
                        .document(id)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Long latestCount = documentSnapshot.getLong("count");
                                if (latestCount == null) {
                                    latestCount = 1L;
                                } else {
                                    latestCount += 1;
                                }

                                FirebaseFirestore.getInstance().collection("song")
                                        .document(id)
                                        .update("count", latestCount);
                            }
                        });
            }
        }
    }
}
