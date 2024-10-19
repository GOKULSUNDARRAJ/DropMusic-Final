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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MostViewAdapter2 extends RecyclerView.Adapter<MostViewAdapter2.ViewHolder> {

    private List<SongModel> songList;

    public MostViewAdapter2(List<SongModel> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.title.setText(song.getTitle());
        holder.subtitle.setText(song.getSubtitle());
        Context context = holder.itemView.getContext();
        Glide.with(context)
                .load(song.getCoverUrl())
                .into(holder.songImage);
        // Set other song details to respective views if needed


        holder.carproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // Assuming you have the MediaPlayerManager class defined with startPlaying method

                MediaPlayerManager.startPlaying(context, song, position, songList);
                Intent intent=new Intent(view.getContext(), PlayerActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        holder.carproduct.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,subtitle;
        ImageView songImage;
        LinearLayout carproduct;
        // Define other views here if needed

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_title_text_view);
            subtitle=itemView.findViewById(R.id.song_subtitle_text_view);
            songImage=itemView.findViewById(R.id.song_cover_image_view);
            carproduct=itemView.findViewById(R.id.carproduct);
            // Initialize other views here if needed
        }
    }

    // Method to update song list with only songs having count greater than 5
    public void updateSongList(List<SongModel> updatedList) {
        songList.clear();
        for (SongModel song : updatedList) {
            if (song.getCount() != null && song.getCount() > 5) {
                songList.add(song);
            }
        }
        notifyDataSetChanged();
    }
}
