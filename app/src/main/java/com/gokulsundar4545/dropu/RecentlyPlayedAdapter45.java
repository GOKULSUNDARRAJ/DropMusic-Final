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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentlyPlayedAdapter45 extends RecyclerView.Adapter<RecentlyPlayedAdapter45.ViewHolder> {
    private List<SongModel> songList;
    private Context context;

    public RecentlyPlayedAdapter45(Context context, List<SongModel> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recently_played45, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView artistTextView;
        ImageView songImge,clear;
        LinearLayout carproduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.song_title_text_view);
            artistTextView = itemView.findViewById(R.id.song_subtitle_text_view);
            songImge=itemView.findViewById(R.id.song_cover_image_view);
            carproduct=itemView.findViewById(R.id.carproduct);
            clear=itemView.findViewById(R.id.claer);
        }

        public void bind(SongModel song) {
            titleTextView.setText(song.getTitle());
            artistTextView.setText(song.getSubtitle());
            Picasso.get().load(song.getCoverUrl()).into(songImge);


            carproduct.setOnClickListener(new View.OnClickListener() {
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

            carproduct.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler));

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the song at the clicked position
                        SongModel song = songList.get(position);

                        // Remove the song from Firebase
                        removeFromFirebase(song,position);

                        // Remove the item from the RecyclerView
                        songList.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });

        }
    }

    private void removeFromFirebase(SongModel song, int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRecentRef = database.child("Users").child(userId).child("recently_played");

            // Find the specific song node in Firebase and remove it
            Query query = userRecentRef.orderByChild("url").equalTo(song.getUrl());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Song removed from Firebase, you can perform any additional actions if needed
                                } else {
                                    // Handle the error
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }
}
