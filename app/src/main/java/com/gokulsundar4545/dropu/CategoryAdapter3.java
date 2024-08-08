package com.gokulsundar4545.dropu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.io.Serializable;
import java.util.List;

public class CategoryAdapter3 extends RecyclerView.Adapter<CategoryAdapter3.CategoryViewHolder> {

    private List<CategoryModel> categoryList;
    private Context context; // Context to start the activity

    public CategoryAdapter3(List<CategoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item3, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryCoverUrlImageView,itemImage;
        private TextView title;

        private static final float INITIAL_SCALE = 1.0f;
        private static final float ZOOM_IN_SCALE = 1.2f;
        private static final long ZOOM_DURATION = 10000; // Zoom animation duration (5 seconds)

        private ValueAnimator zoomAnimator;
        CardView carproduct;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryCoverUrlImageView = itemView.findViewById(R.id.song_cover_image_view);
            title=itemView.findViewById(R.id.title);
            itemImage=itemView.findViewById(R.id.itemImage);

            carproduct=itemView.findViewById(R.id.carproduct);



        }

        public void bind(CategoryModel category) {
            // Load image into the ImageView using Glide
            Glide.with(itemView.getContext())
                    .load(category.getCoverUrl())
                    .transform(new RoundedCorners(16)) // Example: Rounded corners (optional)
                    .into(categoryCoverUrlImageView);

            Glide.with(itemView.getContext())
                    .load(category.getCoverUrl())
                    .transform(new RoundedCorners(16)) // Example: Rounded corners (optional)
                    .into(itemImage);

            title.setText(category.getName());


            carproduct.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler));

            // Start the zoom animation
            startZoomAnimation();


            categoryCoverUrlImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CategoryModel clickedItem = categoryList.get(position);
                        // Start SonglistActivity with the selected category
                        Intent intent = new Intent(context, SongsListActivity.class);
                        intent.putExtra("category", (Serializable) clickedItem);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }
            });
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
                categoryCoverUrlImageView.setScaleX(animatedValue);
                categoryCoverUrlImageView.setScaleY(animatedValue);
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


}
