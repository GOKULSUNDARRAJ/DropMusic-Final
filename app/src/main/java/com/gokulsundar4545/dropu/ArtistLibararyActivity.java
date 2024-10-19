package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class ArtistLibararyActivity extends AppCompatActivity {
    RecyclerView artistrecyclerview;
    EditText searchEditText;

    private ArtistnewAdapter movieAdapter;
    private List<Artist> movieList;
    private List<Artist> filteredList;

    ImageView imageView36;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arist_libararyactivity);

        imageView36=findViewById(R.id.imageView36);

        artistrecyclerview = findViewById(R.id.categories_recycler_view);
        searchEditText = findViewById(R.id.search);

        artistrecyclerview.setLayoutManager(new GridLayoutManager(this,3));

        movieList = new ArrayList<>();
        filteredList = new ArrayList<>();
        movieAdapter = new ArtistnewAdapter(filteredList);
        artistrecyclerview.setAdapter(movieAdapter);

        fetchMoviesFromFirebase();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        imageView36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchEditText.getVisibility()==View.VISIBLE){
                    searchEditText.setVisibility(View.GONE);
                }else {
                    searchEditText.setVisibility(View.VISIBLE);
                }

            }
        });



    }

    private void fetchMoviesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtistPlaylist");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Artist movie = snapshot.getValue(Artist.class);
                    if (movie != null) {
                        movieList.add(movie);
                    }
                }
                filteredList.clear();
                filteredList.addAll(movieList);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ArtistLibararyActivity.this, "Failed to load movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(movieList);
        } else {
            for (Artist artist : movieList) {
                if (artist.getArtistName().toLowerCase().contains(query.toLowerCase())) { // Adjust to your Artist class's search field
                    filteredList.add(artist);
                }
            }
        }
        movieAdapter.notifyDataSetChanged();
    }
}
