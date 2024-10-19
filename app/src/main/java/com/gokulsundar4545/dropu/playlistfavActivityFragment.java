package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class playlistfavActivityFragment extends Fragment {
    ImageView imageView3;

    private RecyclerView recyclerViewplaylist;
    private PlaylistAdapter24 adapterplay;
    private List<PlaylistItem> playlistItems;

    private ShimmerFrameLayout shimmerFrameLayout;

    ConstraintLayout emptylayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlistfav_activity, container, false);

        emptylayout=view.findViewById(R.id.emptylayout);

   shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        if (shimmerFrameLayout != null) {
            // Start shimmer effect if it's not null
            shimmerFrameLayout.startShimmer();

            // Optional: Customize shimmer properties (speed, direction, etc.)
            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT) // Customize direction
                    .setDuration(1500) // Customize duration (in ms)
                    .build();
            shimmerFrameLayout.setShimmer(shimmer);
        } else {
            // Log an error if the shimmer layout was not found
            Log.e("ShimmerFragment", "ShimmerFrameLayout not found in layout");
        }



        imageView3=view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), searchallActivity.class);
                startActivity(intent);

            }
        });

        RecyclerView recyclerViewplaylist =view.findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 is the number of columns
        recyclerViewplaylist.setLayoutManager(gridLayoutManager);

// Initialize playlistItems list
        playlistItems = new ArrayList<>();

// Initialize the adapter with the playlistItems list
        adapterplay = new PlaylistAdapter24(getContext(), playlistItems);

// Set the adapter to the RecyclerView
        recyclerViewplaylist.setAdapter(adapterplay);

        loadPlaylistData();




        return view ;
    }

    private void loadPlaylistData() {
        // Fetch data from Firebase and update playlistItems
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playlistRef = database
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("playlists")
                .child("selectedSongs");

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PlaylistData", "Data fetched successfully from Firebase.");

                playlistItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract the ID from the snapshot key
                    String id = snapshot.getKey();

                    // Create PlaylistItem from snapshot
                    PlaylistItem item = snapshot.getValue(PlaylistItem.class);
                    if (item != null) {
                        item.setId(id); // Set the ID in the PlaylistItem object
                        playlistItems.add(item);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);

                    }
                }
                adapterplay.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
               if (playlistItems.isEmpty()){
                   emptylayout.setVisibility(View.VISIBLE);
                   shimmerFrameLayout.stopShimmer();
                   shimmerFrameLayout.setVisibility(View.INVISIBLE);
               }else {

                   emptylayout.setVisibility(View.GONE);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());

                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}