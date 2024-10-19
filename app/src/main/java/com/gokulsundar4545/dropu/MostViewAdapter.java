package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MostViewAdapter extends RecyclerView.Adapter<MostViewAdapter.ViewHolder> {

    private List<SongModel> songList;

    public MostViewAdapter(List<SongModel> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most, parent, false);
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
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                .into(holder.songImage);
        // Set other song details to respective views if needed


        holder.carproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // Assuming you have the MediaPlayerManager class defined with startPlaying methodMediaPlayerManager.startPlaying(context, song)
                MediaPlayerManager.startPlaying(context, song, position, songList);
                Intent intent=new Intent(view.getContext(), PlayerActivity.class);
                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
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
            title = itemView.findViewById(R.id.title);
            subtitle=itemView.findViewById(R.id.subtitle);
            songImage=itemView.findViewById(R.id.itemImage);
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
