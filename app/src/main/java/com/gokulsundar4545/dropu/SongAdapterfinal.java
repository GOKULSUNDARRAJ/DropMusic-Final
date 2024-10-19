// SongAdapter.java
package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapterfinal extends RecyclerView.Adapter<SongAdapterfinal.SongViewHolder> {

    private List<SongModel> songList;

    public SongAdapterfinal(List<SongModel> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getSubtitle());
        Picasso.get().load(song.getCoverUrl()).into(holder.songImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // Assuming you have the MediaPlayerManager class defined with startPlaying method

                MediaPlayerManager.startPlaying(context, song, position, songList);
                Intent intent = new Intent(context, PlayerActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new instance of MyBottomSheetFragment2 with songModel as parameter
                MyBottomSheetFragment2 bottomSheetFragment = new MyBottomSheetFragment2(song);

                // Show the bottom sheet fragment
                bottomSheetFragment.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtist;
        ImageView songImage,menu;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.title);
            songArtist = itemView.findViewById(R.id.subtitle);
            songImage=itemView.findViewById(R.id.itemImage);
            menu=itemView.findViewById(R.id.itemImage2);
        }
    }

    public void filterList(List<SongModel> filteredList) {
        this.songList = filteredList;
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

}
