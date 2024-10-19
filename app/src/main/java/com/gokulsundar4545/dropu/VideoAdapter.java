package com.gokulsundar4545.dropu;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MovieViewHolder> {

    private List<Video> movieList;

    public VideoAdapter(List<Video> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Video movie = movieList.get(position);

        holder.movieTitle.setText(movie.getVideoTitle());
        holder.subTitle.setText(movie.getVideosubTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayListVideoActivity.class);
                intent.putExtra("MOVIE_NAME", movie.getVideoName());
                intent.putExtra("MOVIE_COVER_URL", movie.getVideoCoverUrl());
                intent.putExtra("MOVIE_TITLE", movie.getVideoTitle());

                view.getContext().startActivity(intent);
            }
        });

        Uri videoUri = Uri.parse(movie.getVideosubTitle());

        holder.movieCoverUrl.setVideoURI(videoUri);
        holder.movieCoverUrl.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            holder.movieCoverUrl.start(); // Start playback when prepared
        });

        holder.movieCoverUrl.setOnErrorListener((mp, what, extra) -> {
            // Handle the error, e.g., show a message or a placeholder
            return true;
        });


    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        VideoView movieCoverUrl;
        TextView movieTitle;
        TextView subTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieCoverUrl = itemView.findViewById(R.id.song_cover_image_view);
            movieTitle = itemView.findViewById(R.id.song_title_text_view);
            subTitle = itemView.findViewById(R.id.song_subtitle_text_view);
        }
    }
}
