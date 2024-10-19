package com.gokulsundar4545.dropu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter5 extends RecyclerView.Adapter<SearchAdapter5.SongViewHolder> {

    private Context context;
    private List<SongModel> songList;

    public SearchAdapter5(Context context, List<SongModel> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item4, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.titleTextView.setText(song.getTitle());
        holder.subtitleTextView.setText(song.getSubtitle());


        holder.subtitleTextView4.setText(song.getTitle());

        if (song.getName()!=null){
            holder.titleTextView4.setText(song.getName());

        }else {
            holder.titleTextView4.setText("Artist Name");
        }



        Context context = holder.itemView.getContext();
        Glide.with(context)
                .load(song.getCoverUrl())
                .into(holder.songImage);

        Glide.with(context)
                .load(song.getCoverUrl())
                .into(holder.song_cover_image_view);

        holder.carproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // Assuming you have the MediaPlayerManager class defined with startPlaying method

                MediaPlayerManager.startPlaying(context, song, position, songList);
                Toast.makeText(context, "Clicked: " + song.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlayerActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        Picasso.get().
                load(song.getArtist())
                .placeholder(R.drawable.picture)
                .into(holder.profile);

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ZoomActivity.class);
                // Pass image URL as an extra
                intent.putExtra("imageUrl", song.getArtist());

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(holder.profile, "transaction_player");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);

                context.startActivity(intent, options.toBundle());
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
        TextView titleTextView4;
        TextView subtitleTextView4;
        ImageView songImage,song_cover_image_view;

        CardView carproduct;

        CircleImageView profile;

        private ImageView categoryCoverUrlImageView,itemImage;
        private TextView title;

        private static final float INITIAL_SCALE = 1.0f;
        private static final float ZOOM_IN_SCALE = 1.2f;
        private static final long ZOOM_DURATION = 10000; // Zoom animation duration (5 seconds)

        private ValueAnimator zoomAnimator;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.subtitle);

            titleTextView4 = itemView.findViewById(R.id.title4);
            subtitleTextView4 = itemView.findViewById(R.id.subtitle4);

            songImage=itemView.findViewById(R.id.itemImage);
            carproduct=itemView.findViewById(R.id.carproduct);
            song_cover_image_view=itemView.findViewById(R.id.song_cover_image_view);

            profile=itemView.findViewById(R.id.profile);




            startZoomAnimation();





        }


        private void startZoomAnimation() {
            if (zoomAnimator != null && zoomAnimator.isRunning()) {
                zoomAnimator.cancel();
            }

            // Create a ValueAnimator for continuous zoom effect
            zoomAnimator = ValueAnimator.ofFloat(INITIAL_SCALE, ZOOM_IN_SCALE, INITIAL_SCALE);
            zoomAnimator.setDuration(ZOOM_DURATION);
            zoomAnimator.setInterpolator(new DecelerateInterpolator());
            zoomAnimator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                song_cover_image_view.setScaleX(animatedValue);
                song_cover_image_view.setScaleY(animatedValue);
            });

            // Set listener to restart animation when it ends
            zoomAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    startZoomAnimation(); // Restart the animation
                }
            });

            zoomAnimator.start();
        }
    }
    public void filterList(List<SongModel> filteredList) {
        songList = filteredList;
        notifyDataSetChanged();
    }




}
