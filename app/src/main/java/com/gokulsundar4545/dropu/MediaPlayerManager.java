package com.gokulsundar4545.dropu;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaPlayerManager {

    private static ExoPlayer exoPlayer = null;
    private static SongModel currentSong = null;
    private static int currentIndex = -1;
    private static List<SongModel> songList;
    private static boolean isPlaying = false; // Track playback state
    private static boolean isRepeatOn = false; // Track repeat mode
    private static boolean isShuffleOn = false; // Track shuffle mode
    private static List<SongModel> originalSongList; // To keep the original list

    // Method to get the current song being played
    public static SongModel getCurrentSong() {
        return currentSong;
    }

    // Method to get the ExoPlayer instance
    public static ExoPlayer getInstance() {
        return exoPlayer;
    }

    // Method to start playing a song from the provided list at the given index
    public static void startPlaying(Context context, SongModel song, int index, List<SongModel> list) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).build();
            addPlaybackListener(context);
        }

        if (!song.equals(currentSong)) {
            // It's a new song so start playing
            currentSong = song;
            currentIndex = index;

            // Store the original list if shuffle mode is enabled
            if (isShuffleOn) {
                originalSongList = new ArrayList<>(list);
                Collections.shuffle(list); // Shuffle the list
            } else {
                originalSongList = null; // Clear original list
            }

            songList = list;
            updateCount();  // Update the play count in Firebase
            playCurrentSong();  // Start playing the current song
        }
    }

    public static synchronized void playNext(Context context) {
        if (songList != null && currentIndex >= 0) {
            if (isRepeatOn) {
                // Replay the current song
                playCurrentSong();
            } else if (currentIndex < songList.size() - 1) {
                // Play the next song
                currentIndex++;
                currentSong = songList.get(currentIndex);
                updateCount();
                playCurrentSong();
            } else {
                // Reached the last song, loop back to the first song
                currentIndex = 0;
                currentSong = songList.get(currentIndex);
                updateCount();
                playCurrentSong();
            }

            if (currentSong != null) {
                // Broadcast song change
                Intent intent = new Intent("SONG_CHANGED");
                intent.putExtra("songTitle", currentSong.getTitle());
                context.sendBroadcast(intent);
            } else {
                Log.e(TAG, "No song available to play.");
            }
        }
    }


    public static synchronized void playPrevious(Context context) {
        try {
            if (songList != null && !songList.isEmpty()) {
                if (currentIndex > 0) {
                    // Play the previous song
                    currentIndex--;
                } else {
                    // If at the beginning of the playlist, loop to the last song
                    currentIndex = songList.size() - 1;
                }
                currentSong = songList.get(currentIndex);
                updateCount();  // Update the play count in Firebase
                playCurrentSong();  // Start playing the previous song
                Log.d(TAG, "Playing previous song. Current Index: " + currentIndex);

                // Broadcast song change
                Intent intent = new Intent("SONG_CHANGED");
                intent.putExtra("songTitle", currentSong.getTitle());
                context.sendBroadcast(intent);
            } else {
                Log.d(TAG, "Playlist is empty or not initialized.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing previous song: " + e.getMessage());
        }
    }




    // Helper method to play the current song
    private static void playCurrentSong() {
        if (currentSong != null) {
            String url = currentSong.getUrl();
            if (url != null) {
                MediaItem mediaItem = MediaItem.fromUri(url);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();
                exoPlayer.play();  // Start playback
                isPlaying = true; // Update playback state
            }
        }
    }

    // Method to play the current song
    public static void play() {
        if (exoPlayer != null && !exoPlayer.isPlaying()) {
            exoPlayer.play();
            isPlaying = true;
        }
    }

    // Method to pause the current song
    public static void pause() {
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.pause();
            isPlaying = false;
        }
    }

    // Method to update the play count in Firebase Firestore
    public static void updateCount() {
        if (currentSong != null) {
            String id = currentSong.getId();
            if (id != null) {
                FirebaseFirestore.getInstance().collection("song")
                        .document(id)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            Long latestCount = documentSnapshot.getLong("count");
                            if (latestCount == null) {
                                latestCount = 1L;
                            } else {
                                latestCount += 1;
                            }

                            FirebaseFirestore.getInstance().collection("song")
                                    .document(id)
                                    .update("count", latestCount);
                        });
            }
        }
    }

    // Method to add a listener to handle automatic transitions to the next song
    private static void addPlaybackListener(Context context) {
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    if (isRepeatOn) {
                        playCurrentSong();  // Replay the current song
                    } else {
                        playNext(context);  // Play the next song
                    }
                }
            }
        });
    }

    // Method to get the current playback state
    public static boolean isPlaying() {
        return isPlaying;
    }

    // Method to enable repeat mode
    public static void enableRepeatMode() {
        isRepeatOn = true;
    }

    // Method to disable repeat mode
    public static void disableRepeatMode() {
        isRepeatOn = false;
    }

    // Method to check if repeat mode is enabled
    public static boolean isRepeatModeOn() {
        return isRepeatOn;
    }

    // Method to enable shuffle mode
    public static void enableShuffleMode() {
        isShuffleOn = true;
    }

    // Method to disable shuffle mode
    public static void disableShuffleMode() {
        isShuffleOn = false;
        if (originalSongList != null && songList != null) {
            songList.clear();
            songList.addAll(originalSongList); // Restore the original list
            originalSongList = null;
        }
    }

    // Method to check if shuffle mode is enabled
    public static boolean isShuffleModeOn() {
        return isShuffleOn;
    }




}
