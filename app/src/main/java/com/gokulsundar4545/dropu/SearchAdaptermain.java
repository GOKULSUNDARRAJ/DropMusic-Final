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

import java.util.List;

public class SearchAdaptermain extends RecyclerView.Adapter<SearchAdaptermain.SongViewHolder> {

    private Context context;
    private List<SongModel> songList;

    public SearchAdaptermain(Context context, List<SongModel> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allsong, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.titleTextView.setText(song.getTitle());
        holder.subtitleTextView.setText(song.getSubtitle());
        Context context = holder.itemView.getContext();
        Glide.with(context)
                .load(song.getCoverUrl())
                .into(holder.songImage);

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
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        ImageView songImage;

        LinearLayout carproduct;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.song_title_text_view);
            subtitleTextView = itemView.findViewById(R.id.song_subtitle_text_view);
            songImage=itemView.findViewById(R.id.song_cover_image_view);
            carproduct=itemView.findViewById(R.id.carproduct);



        }
    }
    public void filterList(List<SongModel> filteredList) {
        songList = filteredList;
        notifyDataSetChanged();
    }


}
