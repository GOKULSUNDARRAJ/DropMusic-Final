package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityHomeSubFragment extends Fragment {

    ImageView chatactivity;
    TextView menu;
    DrawerLayout drawerlayout;
    TextView seeall;
    static final float Endscale = 0.7f;
    ConstraintLayout contentView;
    FrameLayout child_fragment_container;
    LinearLayout linearLayoutPodcast, linearLayout15, linearLayout1654;
    Button add, addartist,video;

    MainActivity2 mainActivity2;



    public MainActivityHomeSubFragment(MainActivity2 mainActivity2) {
        this.mainActivity2 = mainActivity2;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_home_sub, container, false);
        visibility_messagelayout();

        video=view.findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddVideoActivity.class));
            }
        });


        addartist = view.findViewById(R.id.gotoplaylist);
        addartist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddArtistActivity.class));
            }
        });
        add = view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddMovieActivity.class));
            }
        });


        chatactivity = view.findViewById(R.id.imageView7);
        chatactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),recentchatActivity.class);
                startActivity(intent);
            }
        });
        // Initialize DrawerLayout and other views
        drawerlayout = view.findViewById(R.id.drawer_layout);
        contentView = view.findViewById(R.id.content);
        menu = view.findViewById(R.id.imageView2);
        child_fragment_container = view.findViewById(R.id.child_fragment_container);

        linearLayoutPodcast = view.findViewById(R.id.linearLayout165);
        linearLayout1654 = view.findViewById(R.id.linearLayout1654);
        linearLayout15 = view.findViewById(R.id.linearLayout15);
        linearLayoutPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetailActivityFragment fragmentB = new MovieDetailActivityFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.child_fragment_container, fragmentB);
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                fragmentTransaction.commit();

                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline123);
                linearLayout15.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline1234);

            }
        });


        linearLayout15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment fragmentB = new HomeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.child_fragment_container, fragmentB);
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                fragmentTransaction.commit();

                linearLayout15.setBackgroundResource(R.drawable.grayline123);
                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline1234);
            }
        });


        linearLayout1654.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistfavActivityFragment fragmentB = new playlistfavActivityFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.child_fragment_container, fragmentB);
                fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                fragmentTransaction.commit();
                linearLayout15.setBackgroundResource(R.drawable.grayline1234);
                linearLayoutPodcast.setBackgroundResource(R.drawable.grayline1234);
                linearLayout1654.setBackgroundResource(R.drawable.grayline123);
            }
        });






        // Set up the child fragment
        if (savedInstanceState == null) {
            HomeFragment childFragment = new HomeFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.child_fragment_container, childFragment)
                    .commit();
        }






        // Set up menu click listener
        // Set up menu click listener
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerlayout.isDrawerVisible(GravityCompat.START)) {
                    // If the drawer is visible (open), close it and set status bar color to black
                    drawerlayout.closeDrawer(GravityCompat.START);

                } else {
                    // If the drawer is not visible (closed), open it and set status bar color to darker gray
                    drawerlayout.openDrawer(GravityCompat.START);

                }
            }
        });


        // Call the method to animate the navigation drawer
        animateNavigationDrawer();

        NavigationView navigationView = view.findViewById(R.id.navigation_view2);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();
                if (id == R.id.nav_home) {

                    Intent intent = new Intent(getContext(), MainActivity2.class);
                    startActivity(intent);

                    return true;
                } else if (id == R.id.nav_settings) {

                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                    drawerlayout.close();
                    mainActivity2.finish();


                    return true;
                } else if (id == R.id.history) {
                    Intent intent = new Intent(getContext(), RecentlyPlayedActivity.class);
                    startActivity(intent);


                    return true;
                } else if (id == R.id.new12) {
                    Intent intent = new Intent(getContext(), playbackActivity.class);
                    startActivity(intent);


                    return true;
                }
                // Add other menu item click handling here if needed
                return false;
            }
        });



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

                    // Get the NavigationView
                    NavigationView navigationView = view.findViewById(R.id.navigation_view2);

                    // Get the header view from the NavigationView
                    View headerView = navigationView.getHeaderView(0);

                    // Find the TextView in the header view
                    TextView navHeaderUsername = headerView.findViewById(R.id.nav_header_username);
                    TextView navHeaderUsername2 = headerView.findViewById(R.id.nav_header_username2);
                    TextView headername=view.findViewById(R.id.imageView2);

                    headername.setText(username);

                    // Set the username in the TextView
                    navHeaderUsername.setText(username);
                    navHeaderUsername2.setText(username);

                    // Set click listeners for the TextViews
                    navHeaderUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ProfileActivityFragment fragmentB = new ProfileActivityFragment();
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragmentB);
                            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                            fragmentTransaction.commit();
                        }
                    });

                    navHeaderUsername2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ProfileActivityFragment fragmentB = new ProfileActivityFragment();
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragmentB);
                            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack to enable back navigation
                            fragmentTransaction.commit();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });




        return view;
    }

    private void animateNavigationDrawer() {
        drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Scale the View based on the current slide offset
                final float diffScaledOffset = slideOffset * (1 - Endscale);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);
                child_fragment_container.setScaleX(offsetScale);
                child_fragment_container.setScaleY(offsetScale);

                // Translate the contentView based on the slide offset
                final float xoffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xoffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);

                final float xoffset2 = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff2 = child_fragment_container.getWidth() * diffScaledOffset / 2;
                final float xTranslation2 = xoffset2 - xOffsetDiff2;
                child_fragment_container.setTranslationX(xTranslation2);

            }
        });
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(color);
        }
    }



    private void visibility_messagelayout(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recyclerview").child("view");

// Attach a listener to read the data at our reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                Boolean bigSongView = dataSnapshot.child("Messagelayout").getValue(Boolean.class);

                if (bigSongView){
                    chatactivity.setVisibility(View.VISIBLE);
                }else {
                    chatactivity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });




    }
}
