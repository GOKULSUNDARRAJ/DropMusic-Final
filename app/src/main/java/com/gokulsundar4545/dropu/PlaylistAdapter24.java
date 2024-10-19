package com.gokulsundar4545.dropu;

import android.content.Context;
import android.content.Intent;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaylistAdapter24 extends RecyclerView.Adapter<PlaylistAdapter24.ViewHolder> {
    private List<PlaylistItem> playlistItems;
    private Context context;

    public PlaylistAdapter24(Context context, List<PlaylistItem> playlistItems) {
        this.context = context;
        this.playlistItems = playlistItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist24, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistItem item = playlistItems.get(position);
        holder.coverName.setText(item.getCoverName());
        Glide.with(context)
                .load(item.getCoverImage())
                .into(holder.coverImage);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, yourSongplaylistActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                intent.putExtra("Name", item.getCoverName());
                intent.putExtra("profile", item.getCoverImage());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;
        TextView coverName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.cover_image);
            coverName = itemView.findViewById(R.id.cover_name);
        }
    }
}
