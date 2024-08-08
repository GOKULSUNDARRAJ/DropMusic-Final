package com.gokulsundar4545.dropu;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {

    private List<TrendingModel> trendingList;
    private Context context; // Context to start the activity

    public TrendingAdapter(List<TrendingModel> trendingList, Context context) {
        this.trendingList = trendingList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending, parent, false);
        return new TrendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        TrendingModel trending = trendingList.get(position);
        holder.bind(trending);
    }

    @Override
    public int getItemCount() {
        return trendingList.size();
    }

    public class TrendingViewHolder extends RecyclerView.ViewHolder {

        private ImageView trendingImageView;
        private TextView trendingNameTextView;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);

            trendingImageView = itemView.findViewById(R.id.categoryCoverUrlTextView);
            trendingNameTextView = itemView.findViewById(R.id.categorySongsTextView);

            // Set click listener for the whole item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        TrendingModel clickedItem = trendingList.get(position);
                        // Start TrendingDetailActivity with the selected trending item
                        Intent intent = new Intent(context, SongsListActivity.class);
                        intent.putExtra("trending", (Serializable) clickedItem);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(TrendingModel trending) {
            trendingNameTextView.setText(trending.getName());
            Glide.with(itemView.getContext())
                    .load(trending.getCoverUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(trendingImageView);
        }
    }
}
