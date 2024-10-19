package com.gokulsundar4545.dropu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivityFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieAdapter23 movieAdapter;
    private List<Movie> movieList;
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_detail_activity, container, false);

        recyclerView =view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter23(movieList);
        recyclerView.setAdapter(movieAdapter);

        fetchMoviesFromFirebase();


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


        return view;
    }

    private void fetchMoviesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MoviesongPlaylist");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    if (movie != null) {
                        movieList.add(movie);
                    }
                }
                movieAdapter.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}