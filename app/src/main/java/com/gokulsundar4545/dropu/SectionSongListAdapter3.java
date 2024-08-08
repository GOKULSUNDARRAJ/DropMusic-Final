package com.gokulsundar4545.dropu;



import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SectionSongListAdapter3 extends RecyclerView.Adapter<SectionSongListAdapter3.ViewHolder> {
    private List<String> songs;

    public SectionSongListAdapter3(CategoryModel categoryModel) {
        this.songs = categoryModel != null ? categoryModel.getSongs() : new ArrayList<>();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String songId = songs.get(position);
        holder.bind(songId);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, subtitle1,title,subtitle;
        ImageView coverimage,itemImage;
        androidx.cardview.widget.CardView carproduct;
        LinearLayout laypro;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.song_title_text_view);
            subtitle1 = itemView.findViewById(R.id.song_subtitle_text_view);
            coverimage = itemView.findViewById(R.id.song_cover_image_view);
            carproduct = itemView.findViewById(R.id.carproduct);
            itemImage=itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.title);
            subtitle=itemView.findViewById(R.id.subtitle);
            laypro=itemView.findViewById(R.id.laypro);
        }

        public void bind(String songId) {



            coverimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Scale the coverimage to zoom in
                    ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(coverimage, View.SCALE_X, 1.0f, 1.2f);
                    ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(coverimage, View.SCALE_Y, 1.0f, 1.2f);
                    AnimatorSet scaleAnimatorSet = new AnimatorSet();
                    scaleAnimatorSet.setDuration(300); // Set the duration as needed
                    scaleAnimatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
                    scaleAnimatorSet.start();

                    // Optionally, handle the zoom-out effect after a delay
                    coverimage.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator scaleAnimatorXReverse = ObjectAnimator.ofFloat(coverimage, View.SCALE_X, 1.2f, 1.0f);
                            ObjectAnimator scaleAnimatorYReverse = ObjectAnimator.ofFloat(coverimage, View.SCALE_Y, 1.2f, 1.0f);
                            AnimatorSet scaleAnimatorSetReverse = new AnimatorSet();
                            scaleAnimatorSetReverse.setDuration(300); // Set the duration as needed
                            scaleAnimatorSetReverse.playTogether(scaleAnimatorXReverse, scaleAnimatorYReverse);
                            scaleAnimatorSetReverse.start();
                        }
                    }, 1000); // Adjust the delay time (in milliseconds) before zoom-out
                }
            });




            FirebaseFirestore.getInstance().collection("song")
                    .document(songId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                SongModel songModel = getSongModel(documentSnapshot);

                                Glide.with(itemView.getContext())
                                        .load(songModel.getCoverUrl())
                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                                        .into(coverimage);

                                Glide.with(itemView.getContext())
                                        .load(songModel.getCoverUrl())
                                        .into(itemImage);



                                Glide.with(itemView.getContext())
                                        .asBitmap()
                                        .load(songModel.getCoverUrl())
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                                        .listener(new RequestListener<Bitmap>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                // Use Palette API to extract dominant color
                                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                                    @Override
                                                    public void onGenerated(Palette palette) {
                                                        // Get the dominant color
                                                        int dominantColor = palette.getDominantColor(itemView.getContext().getResources().getColor(android.R.color.black));
                                                        // Now you have the dominant color, you can use it as needed
                                                        // For example, set the background color of a view
                                                        laypro.setBackgroundColor(dominantColor);
                                                    }
                                                });
                                                return false;
                                            }
                                        })
                                        .into(itemImage);





                                carproduct.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Context context = view.getContext();
                                        // Assuming you have the MediaPlayerManager class defined with startPlaying method
                                        MediaPlayerManager.startPlaying(context, songModel);



                                        Intent intent = new Intent(context, PlayerActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    }
                                });

                                carproduct.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler));

                                titleTextView.setText(songModel.getTitle());
                                subtitle1.setText(songModel.getSubtitle());
                                title.setText(songModel.getTitle());
                                subtitle.setText(songModel.getSubtitle());
                            } else {
                                Log.d("SongAdapter", "No such document for song ID: " + songId);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SongAdapter", "Error getting document for song ID: " + songId, e);
                        }
                    });



        }

        private SongModel getSongModel(DocumentSnapshot documentSnapshot) {
            String songTitle = documentSnapshot.getString("title");
            String subtitle = documentSnapshot.getString("subtitle");
            String coverUrl = documentSnapshot.getString("coverUrl");
            String Url = documentSnapshot.getString("url");
            String id = documentSnapshot.getString("id");
            String lyrics = documentSnapshot.getString("lyrics");
            String artist = documentSnapshot.getString("artist");
            String name = documentSnapshot.getString("name");
            Long count = documentSnapshot.getLong("count");


            String key = documentSnapshot.getId();
            return new SongModel(key,id, songTitle, subtitle, Url, coverUrl,lyrics,artist,name, count);
        }
    }

}
