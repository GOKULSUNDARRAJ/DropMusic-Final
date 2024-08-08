package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter5 extends RecyclerView.Adapter<CategoryAdapter5.CategoryViewHolder> {


    private List<CategoryModel> categoryList;
    private List<CategoryModel> filteredList; // Add a filtered list
    private Context context;

    public CategoryAdapter5(List<CategoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.filteredList = new ArrayList<>(categoryList); // Initialize filtered list
        this.context = context;
    }

    public void filter(String searchText) {
        filteredList.clear();
        if (searchText.isEmpty()) {
            filteredList.addAll(categoryList); // Add all items if search text is empty
        } else {
            String searchLowerCase = searchText.toLowerCase(Locale.getDefault());
            for (CategoryModel item : categoryList) {
                if (item.getName().toLowerCase(Locale.getDefault()).contains(searchLowerCase)) {
                    filteredList.add(item); // Add item to filtered list if it matches search text
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter that data set has changed
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item_prodcast, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = filteredList.get(position); // Use filtered list here
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return filteredList.size(); // Return size of filtered list
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryCoverUrlImageView;
        private TextView categoryNameTextView;
        ImageView fav,playlist;
        TextView song_title_text_view3,song_title_text_view4;


        CardView carproduct;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);


            categoryCoverUrlImageView = itemView.findViewById(R.id.song_cover_image_view);
            categoryNameTextView = itemView.findViewById(R.id.song_title_text_view);
            fav=itemView.findViewById(R.id.fav);
            playlist=itemView.findViewById(R.id.playlist);
            carproduct=itemView.findViewById(R.id.carproduct);

            song_title_text_view3=itemView.findViewById(R.id.song_title_text_view3);
            song_title_text_view4=itemView.findViewById(R.id.song_title_text_view4);

            song_title_text_view4.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler3));
            song_title_text_view3.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler3));
            categoryNameTextView.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler3));

            fav.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler4));
            playlist.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler4));

            playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CategoryModel clickedItem = categoryList.get(position);
                        String categoryName = clickedItem.getName(); // Get the category name of the clicked item

                        // Get the current user
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        // Call toggleFavorite method with the category name and current user
                        toggleFavoritePlaylist(categoryName, currentUser,playlist);
                    }
                }
            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CategoryModel clickedItem = categoryList.get(position);
                        String categoryName = clickedItem.getName(); // Get the category name of the clicked item

                        // Get the current user
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        // Call toggleFavorite method with the category name and current user
                        toggleFavorite(categoryName, currentUser,fav);
                    }
                }
            });


            // Set click listener for the whole item view
            carproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CategoryModel clickedItem = filteredList.get(position);
                        // Start SonglistActivity with the selected category
                        Intent intent = new Intent(context, SongsListActivity.class);
                        intent.putExtra("category", (Serializable) clickedItem);
                        intent.putExtra("fromCategoryActivity", true);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }
            });

            carproduct.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler));
        }

        public void bind(CategoryModel category) {


            categoryNameTextView.setText(category.getName());
            Glide.with(itemView.getContext())
                    .load(category.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(categoryCoverUrlImageView);

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(userId).child("favoritesmovie");
                userFavoritesRef.orderByChild("title").equalTo(category.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Category is in favorites, set favorite icon
                            fav.setImageResource(R.drawable.baseline_favorite_24un);
                        } else {
                            // Category is not in favorites, set unfavorite icon
                            fav.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            } else {
                // User is not logged in, set default icon
                fav.setImageResource(R.drawable.baseline_favorite_24);
            }







            FirebaseUser currentUser2 = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser2 != null) {
                String userId = currentUser2.getUid();
                DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(userId).child("favoritesmovieplaylist");
                userFavoritesRef.orderByChild("title").equalTo(category.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Category is in favorites, set favorite icon
                            playlist.setImageResource(R.drawable.baseline_remove_circle_outline_24);
                        } else {
                            // Category is not in favorites, set unfavorite icon
                            playlist.setImageResource(R.drawable.addplay);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            } else {
                // User is not logged in, set default icon
                playlist.setImageResource(R.drawable.addplay);
            }
        }

    }

    private void toggleFavorite(String categoryName, FirebaseUser currentUser, ImageView fav) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("favoritesmovie");
            userFavoritesRef.orderByChild("title").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Category is already in favorites, remove it
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            childSnapshot.getRef().removeValue();
                        }
                        // Update UI to show favorite icon
                        fav.setImageResource(R.drawable.baseline_favorite_24);
                        showToast("Removed from favorites");
                    } else {
                        // Category is not in favorites, add it
                        String categoryUid = userFavoritesRef.push().getKey(); // Generate a unique key for the category
                        if (categoryUid != null) {
                            userFavoritesRef.child(categoryUid).child("title").setValue(categoryName);
                        }

                        fav.setImageResource(R.drawable.baseline_favorite_24un);
                        showToast("Added to favorites");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }



    private void toggleFavoritePlaylist(String categoryName, FirebaseUser currentUser, ImageView fav) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userFavoritesRef = mDatabase.child("Users").child(userId).child("favoritesmovieplaylist");
            userFavoritesRef.orderByChild("title").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Category is already in favorites, remove it
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            childSnapshot.getRef().removeValue();
                        }
                        // Update UI to show favorite icon
                        fav.setImageResource(R.drawable.addplay);
                        showToast("Removed from favorites");
                    } else {
                        // Category is not in favorites, add it
                        String categoryUid = userFavoritesRef.push().getKey(); // Generate a unique key for the category
                        if (categoryUid != null) {
                            userFavoritesRef.child(categoryUid).child("title").setValue(categoryName);
                        }

                        fav.setImageResource(R.drawable.baseline_remove_circle_outline_24);
                        showToast("Added to favorites");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }


    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
