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

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistnewAdapter extends RecyclerView.Adapter<ArtistnewAdapter.MovieViewHolder> {

    private List<Artist> movieList;

    public ArtistnewAdapter(List<Artist> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item2, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Artist movie = movieList.get(position);

        holder.movieTitle.setText(movie.getTitle());
        holder.subTitle.setText(movie.getArtistTitle());

        Picasso.get().load(movie.getArtistCoverUrl()).into(holder.movieCoverUrl);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayListAritistActivity.class);
                intent.putExtra("MOVIE_NAME", movie.getArtistName());
                intent.putExtra("MOVIE_COVER_URL", movie.getArtistCoverUrl());
                intent.putExtra("MOVIE_TITLE", movie.getTitle());
                intent.putExtra("SUB_TITLE", movie.getArtistTitle());
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CircleImageView movieCoverUrl;
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
