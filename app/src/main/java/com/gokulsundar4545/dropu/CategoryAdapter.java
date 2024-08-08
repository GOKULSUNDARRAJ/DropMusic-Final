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
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryModel> categoryList;
    private Context context; // Context to start the activity
    public CategoryAdapter(List<CategoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
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

        private ImageView categoryCoverUrlImageView;
        private TextView categoryNameTextView;
        ConstraintLayout categorycard;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryCoverUrlImageView = itemView.findViewById(R.id.categoryCoverUrlTextView);
            categoryNameTextView = itemView.findViewById(R.id.categorySongsTextView);
            categorycard=itemView.findViewById(R.id.categorycard);

            // Set click listener for the whole item view
            itemView.setOnClickListener(new View.OnClickListener() {
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

            categorycard.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.recycler));

        }

        public void bind(CategoryModel category) {
            categoryNameTextView.setText(category.getName());
            Glide.with(itemView.getContext())
                    .load(category.getCoverUrl())
                    .into(categoryCoverUrlImageView);
        }
    }
}
