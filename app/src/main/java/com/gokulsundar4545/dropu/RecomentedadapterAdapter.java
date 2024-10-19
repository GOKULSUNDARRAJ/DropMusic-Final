package com.gokulsundar4545.dropu;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecomentedadapterAdapter extends RecyclerView.Adapter<RecomentedadapterAdapter.MovieViewHolder> {

    private List<Recomented> movieList;

    public RecomentedadapterAdapter(List<Recomented> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie2, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Recomented movie = movieList.get(position);

        holder.movieTitle.setText(movie.getRecomentedTitle());
        holder.subTitle.setText(movie.getRecomentedsubTitle());

        Picasso.get().load(movie.getRecomentedCoverUrl()).into(holder.movieCoverUrl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayListRecomentedActivity.class);
                intent.putExtra("MOVIE_NAME", movie.getRecomentedName());
                intent.putExtra("MOVIE_COVER_URL", movie.getRecomentedCoverUrl());
                intent.putExtra("MOVIE_TITLE", movie.getRecomentedsubTitle());
                intent.putExtra("SUB_TITLE", movie.getRecomentedTitle());
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieCoverUrl;
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
