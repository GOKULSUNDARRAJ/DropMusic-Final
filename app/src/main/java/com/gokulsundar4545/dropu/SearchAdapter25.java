package com.gokulsundar4545.dropu;

import android.content.Context;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchAdapter25 extends RecyclerView.Adapter<SearchAdapter25.SongViewHolder> {

    private Context context;
    private List<SongModel> songList;
    private Set<Integer> selectedItems; // To keep track of selected items

    public SearchAdapter25(Context context, List<SongModel> songList) {
        this.context = context;
        this.songList = songList;
        this.selectedItems = new HashSet<>(); // Initialize the set
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
        Glide.with(context)
                .load(song.getCoverUrl())
                .into(holder.songImage);

        // Set selection state
        if (selectedItems.contains(position)) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.progressBarSecondaryProgress)); // Example color
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); // Default color
        }

        holder.carproduct.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler2));

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position); // Deselect the item
            } else {
                selectedItems.add(position); // Select the item
            }
            notifyItemChanged(position); // Notify the change
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
            songImage = itemView.findViewById(R.id.itemImage);
            carproduct = itemView.findViewById(R.id.carproduct);

            menu=itemView.findViewById(R.id.itemImage2);
        }
    }

    public void filterList(List<SongModel> filteredList) {
        songList = filteredList;
        notifyDataSetChanged();
    }

    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }
}
