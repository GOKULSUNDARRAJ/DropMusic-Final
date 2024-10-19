package com.gokulsundar4545.dropu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
public class PlaylistAdapter25 extends RecyclerView.Adapter<PlaylistAdapter25.PlaylistViewHolder> {

    private List<PlaylistModel> playlistList;

    public PlaylistAdapter25(List<PlaylistModel> playlistList) {
        this.playlistList = playlistList;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistModel playlist = playlistList.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private TextView coverNameTextView;
        private ImageView coverImageView;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            coverNameTextView = itemView.findViewById(R.id.songTitle);
            coverImageView = itemView.findViewById(R.id.songCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Show a Toast message with the index position + 1
                        Toast.makeText(view.getContext(), "Item position: " + (position + 1), Toast.LENGTH_SHORT).show();
                        int pos=position+1;

                        // Optional: You can still perform other actions like starting a new activity
                        // Create an Intent to start a new activity
                        Intent intent = new Intent(view.getContext(), SongPlaylistActivity.class);
                        // Pass data to the new activity
                        intent.putExtra("coverName", playlistList.get(position).getCoverName());
                        intent.putExtra("coverImage", playlistList.get(position).getCoverImage());
                        intent.putExtra("position", String.valueOf(pos));
                        Toast.makeText(view.getContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();


                        // Start the new activity
                        view.getContext().startActivity(intent);
                    }
                }
            });


        }

        public void bind(PlaylistModel playlist) {
            coverNameTextView.setText(playlist.getCoverName());
            Glide.with(itemView.getContext()).load(playlist.getCoverImage()).into(coverImageView);
        }
    }
}
