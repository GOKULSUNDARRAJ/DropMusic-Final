package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AllSearch extends AppCompatActivity {



    private RecyclerView recyclerView;
    private SearchAdapterallsearch adapter;
    private List<SongModel> songList;
    private EditText editText;

    LinearLayout linearLayoutPodcast, linearLayout15, linearLayout1654,songlayouteeall;
    private RecyclerView playlistrecyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private List<Movie> filteredMovieList;

    private RecyclerView yourplaylistRecyclerView;
    private ArtistnewAdapter movieAdaptera;
    private List<Artist> movieLista;
    private List<Artist> filteredMovieLista;

    LinearLayout songlayout,playlistlayout,artistlayout;


    ImageView searcgicon,addicon;
    LinearLayout editlayout;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_search);




        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);

                    TextView headername=findViewById(R.id.imageView2);
                    headername.setText(username);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });









        songlayout=findViewById(R.id.songlayout);
        playlistlayout=findViewById(R.id.playlistlayout);
        artistlayout=findViewById(R.id.artistlayout);


        linearLayoutPodcast = findViewById(R.id.linearLayout165);
        linearLayout1654 = findViewById(R.id.linearLayout1654);
        linearLayout15 =findViewById(R.id.linearLayout15);
        songlayouteeall=findViewById(R.id.linearLayout1654fffff);



        addicon=findViewById(R.id.addicon);
        addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),AddHighlitescover.class));
            }
        });



        searcgicon=findViewById(R.id.searchicon);
        editlayout=findViewById(R.id.editlayout);
        searcgicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editlayout.getVisibility()==View.VISIBLE){
                    editlayout.setVisibility(View.GONE);
                }else {
                    editlayout.setVisibility(View.VISIBLE);
                }

            }
        });


        editText = findViewById(R.id.search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the songList based on the user input
                filterSongs(charSequence.toString());
                filterMovies(charSequence.toString());
                filterArtists(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        recyclerView = findViewById(R.id.allsongrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        songList = new ArrayList<>();
        adapter = new SearchAdapterallsearch(this, songList);
        recyclerView.setAdapter(adapter);


        fetchAllSong();

        playlistrecyclerView = findViewById(R.id.yourplaylistrecyclerview);
        LinearLayoutManager layoutManagerp = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playlistrecyclerView.setLayoutManager(layoutManagerp);

        movieList = new ArrayList<>();
        filteredMovieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(filteredMovieList);
        playlistrecyclerView.setAdapter(movieAdapter);

        fetchMoviesFromFirebase();


        yourplaylistRecyclerView = findViewById(R.id.artistrecyclerview);


        yourplaylistRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        movieLista = new ArrayList<>();
        filteredMovieLista = new ArrayList<>();
        movieAdaptera = new ArtistnewAdapter(filteredMovieLista);
        yourplaylistRecyclerView.setAdapter(movieAdaptera);

        fetchArtistsFromFirebase();


        linearLayoutPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline123);
                linearLayout15.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline1234);
                songlayouteeall.setBackgroundResource(R.drawable.grayline1234);

                playlistlayout.setVisibility(View.VISIBLE);
                playlistrecyclerView.setLayoutManager(new GridLayoutManager(AllSearch.this,3));
                songlayout.setVisibility(View.GONE);
                artistlayout.setVisibility(View.GONE);
            }
        });


        linearLayout15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout15.setBackgroundResource(R.drawable.grayline123);
                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline1234);
                songlayouteeall.setBackgroundResource(R.drawable.grayline1234);

                playlistrecyclerView.setLayoutManager(new LinearLayoutManager(AllSearch.this,RecyclerView.HORIZONTAL,false));
                yourplaylistRecyclerView.setLayoutManager(new LinearLayoutManager(AllSearch.this,RecyclerView.HORIZONTAL,false));
                recyclerView.setLayoutManager(new LinearLayoutManager(AllSearch.this,RecyclerView.HORIZONTAL,false));
                playlistlayout.setVisibility(View.VISIBLE);
                songlayout.setVisibility(View.VISIBLE);
                artistlayout.setVisibility(View.VISIBLE);

            }
        });


        linearLayout1654.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout15.setBackgroundResource(R.drawable.grayline1234);
                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline123);
                songlayouteeall.setBackgroundResource(R.drawable.grayline1234);

                playlistlayout.setVisibility(View.GONE);
                artistlayout.setVisibility(View.VISIBLE);
                songlayout.setVisibility(View.GONE);
                yourplaylistRecyclerView.setLayoutManager(new GridLayoutManager(AllSearch.this,3));
            }
        });

        songlayouteeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout15.setBackgroundResource(R.drawable.grayline1234);
                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline1234);
                songlayouteeall.setBackgroundResource(R.drawable.grayline123);

                recyclerView.setLayoutManager(new GridLayoutManager(AllSearch.this,3));
                playlistlayout.setVisibility(View.GONE);
                artistlayout.setVisibility(View.GONE);
                songlayout.setVisibility(View.VISIBLE);


            }
        });



    }

    private void fetchAllSong() {

        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        songList.clear(); // Clear existing data
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String key = documentSnapshot.getId();
                                String songTitle = documentSnapshot.getString("title");
                                String subtitle = documentSnapshot.getString("subtitle");
                                String coverUrl = documentSnapshot.getString("coverUrl");
                                String Url = documentSnapshot.getString("url");
                                String id = documentSnapshot.getString("id");
                                String lyrics = documentSnapshot.getString("lyrics");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                Long count = documentSnapshot.getLong("count");
                                String moviename = documentSnapshot.getString("moviename");
                                SongModel song = new SongModel(key,id, songTitle, subtitle, Url, coverUrl,lyrics,artist,name,moviename, count);

                                songList.add(song);
                            } else {
                                Log.d("SearchActivity", "No such document");
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchActivity", "Error fetching songs", e);
                    }
                });

    }



    private void filterSongs(String searchText) {
        List<SongModel> filteredList = new ArrayList<>();
        for (SongModel song : songList) {
            if (song.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(song);
            }
        }


        adapter.filterList(filteredList);

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
                    filterMovies(editText.getText().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllSearch.this, "Failed to load movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMovies(String query) {
        filteredMovieList.clear();
        if (query.isEmpty()) {
            // If the search query is empty, show all movies
            filteredMovieList.addAll(movieList);
        } else {
            // If there is a search query, filter the movies
            for (Movie movie : movieList) {
                if (movie.getMovieTitle().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault())) ||
                        movie.getSubTitle().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                    filteredMovieList.add(movie);
                }
            }
        }
        movieAdapter.notifyDataSetChanged();
    }




    private void fetchArtistsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ArtistPlaylist");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieLista.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Artist artist = snapshot.getValue(Artist.class);
                    if (artist != null) {
                        movieLista.add(artist);
                    }
                }
                filterArtists(editText.getText().toString()); // Apply filter when data is fetched
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllSearch.this, "Failed to load artists.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterArtists(String query) {
        filteredMovieLista.clear();
        if (query.isEmpty()) {
            filteredMovieLista.addAll(movieLista); // Show all artists if the query is empty
        } else {
            for (Artist artist : movieLista) {
                if (artist.getArtistName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                    filteredMovieLista.add(artist);
                }
            }
        }
        movieAdaptera.notifyDataSetChanged();
    }

    private void filterArtists2(String query) {
        filteredMovieLista.clear();
        if (query.isEmpty()) {
            filteredMovieLista.addAll(movieLista); // Show all artists if the query is empty
        } else {
            for (Artist artist : movieLista) {
                if (artist.getArtistTitle().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                    filteredMovieLista.add(artist);
                }
            }
        }
        movieAdaptera.notifyDataSetChanged();
    }
}