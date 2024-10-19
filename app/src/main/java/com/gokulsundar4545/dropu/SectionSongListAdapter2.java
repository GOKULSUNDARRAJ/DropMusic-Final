package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SectionSongListAdapter2 extends RecyclerView.Adapter<SectionSongListAdapter2.ViewHolder> {
    private List<String> songs;

    public SectionSongListAdapter2(CategoryModel categoryModel) {
        this.songs = categoryModel != null ? categoryModel.getSongs() : new ArrayList<>();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String songId = songs.get(position);
        holder.bind(songId);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, subtitle1;
        ImageView coverimage;
        LinearLayout carproduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            subtitle1 = itemView.findViewById(R.id.subtitle);
            coverimage = itemView.findViewById(R.id.itemImage);
            carproduct = itemView.findViewById(R.id.carproduct);
        }

        public void bind(String songId) {
            FirebaseFirestore.getInstance().collection("song")
                    .document(songId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                SongModel songModel = getSongModel(documentSnapshot);

                                Glide.with(itemView.getContext())
                                        .load(songModel.getCoverUrl())
                                        .into(coverimage);

                                carproduct.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Context context = view.getContext();
                                        // Assuming you have the MediaPlayerManager class defined with startPlaying method




                                        Intent intent = new Intent(context, PlayerActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    }
                                });

                                titleTextView.setText(songModel.getTitle());
                                subtitle1.setText(songModel.getSubtitle());
                            } else {
                                Log.d("SongAdapter", "No such document for song ID: " + songId);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SongAdapter", "Error getting document for song ID: " + songId, e);
                        }
                    });

            carproduct.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler));
        }

        private SongModel getSongModel(DocumentSnapshot documentSnapshot) {
            String songTitle = documentSnapshot.getString("title");
            String subtitle = documentSnapshot.getString("subtitle");
            String coverUrl = documentSnapshot.getString("coverUrl");
            String Url = documentSnapshot.getString("url");
            String id = documentSnapshot.getString("id");
            String lyrics = documentSnapshot.getString("lyrics");
            String artist = documentSnapshot.getString("artist");
            String name = documentSnapshot.getString("name");
            Long count = documentSnapshot.getLong("count");

            String key = documentSnapshot.getId();
            String moviename = documentSnapshot.getString("moviename");
            return new SongModel(key,id, songTitle, subtitle, Url, coverUrl,lyrics,artist,name,moviename, count);
        }
    }

}
