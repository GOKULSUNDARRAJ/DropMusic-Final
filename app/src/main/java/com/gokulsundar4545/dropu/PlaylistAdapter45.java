package com.gokulsundar4545.dropu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlaylistAdapter45 extends RecyclerView.Adapter<PlaylistAdapter45.ViewHolder> {
    private List<SongModel> favoritesList;

    // Constructor
    public PlaylistAdapter45(List<SongModel> favoritesList) {
        this.favoritesList = favoritesList;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        ImageView coverImageView;

        androidx.cardview.widget.CardView carproduct;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize your ViewHolder components here
            titleTextView = itemView.findViewById(R.id.song_title_text_view);
            subtitleTextView = itemView.findViewById(R.id.song_title_text_view4);
            coverImageView = itemView.findViewById(R.id.song_cover_image_view);
            carproduct=itemView.findViewById(R.id.carproduct);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favplaylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the SongModel at the current position
        SongModel song = favoritesList.get(position);

        // Set the title and subtitle to the TextViews in your ViewHolder
        holder.titleTextView.setText(song.getTitle());
        holder.subtitleTextView.setText(song.getSubtitle());

        // Load the cover image using Glide or any other image loading library
        Glide.with(holder.itemView.getContext())
                .load(song.getCoverUrl())
                .into(holder.coverImageView);
        holder.carproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // Assuming you have the MediaPlayerManager class defined with startPlaying method
                MediaPlayerManager.startPlaying(context, song);


                Intent intent = new Intent(context, PlayerActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        holder.carproduct.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler));
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    // Update the adapter data
    public void setFavoritesList(List<SongModel> favoritesList) {
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
    }
}
