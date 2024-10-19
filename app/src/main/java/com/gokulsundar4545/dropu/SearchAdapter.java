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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SongViewHolder> {

    private Context context;
    private List<SongModel> songList;

    public SearchAdapter(Context context, List<SongModel> songList) {
        this.context = context;
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
        holder.titleTextView.setText(song.getTitle());
        holder.subtitleTextView.setText(song.getSubtitle());
        Context context = holder.itemView.getContext();
        Glide.with(context)
                .load(song.getCoverUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                .into(holder.songImage);

        holder.carproduct.setOnClickListener(new View.OnClickListener() {
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
                bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        holder.carproduct.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        ImageView songImage,menu;

        LinearLayout carproduct;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.subtitle);
            songImage=itemView.findViewById(R.id.itemImage);
            carproduct=itemView.findViewById(R.id.carproduct);

            menu=itemView.findViewById(R.id.itemImage2);


        }
    }
    public void filterList(List<SongModel> filteredList) {
        songList = filteredList;
        notifyDataSetChanged();
    }


}
