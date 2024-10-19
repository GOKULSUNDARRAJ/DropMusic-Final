package com.gokulsundar4545.dropu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<SongModel> favoritesList;

    // Constructor
    public FavoritesAdapter(List<SongModel> favoritesList) {
        this.favoritesList = favoritesList;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        ImageView coverImageView;

        LinearLayout carproduct;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize your ViewHolder components here
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.subtitle);
            coverImageView = itemView.findViewById(R.id.itemImage);
            carproduct=itemView.findViewById(R.id.carproduct);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
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
                // Assuming you have the MediaPlayerManager class defined with startPlaying methodMediaPlayerManager.startPlaying(context, song);
                MediaPlayerManager.startPlaying(context, song, position, favoritesList);


                Intent intent = new Intent(context, PlayerActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, String.valueOf(song.getUrl()), Toast.LENGTH_SHORT).show();
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
