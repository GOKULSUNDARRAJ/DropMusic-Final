package com.gokulsundar4545.dropu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivityFragment extends Fragment {

    TextView username2,username3,email4;

    private RecyclerView recyclerView;
    private PlaylistAdapter2 adapter;
    private List<SongModel> favoritesList;
    ImageView imageView3;

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView,addhighlites;


    private RecyclerView recyclerViewplaylist;
    private PlaylistAdapter12 adapterplay;
    private List<PlaylistItem> playlistItems;

    private ShimmerFrameLayout shimmerFrameLayout;

    TextView textView71;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile_activity, container, false);

        textView71=view.findViewById(R.id.textView7);

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
// Initialize RecyclerView
        RecyclerView recyclerViewplaylist = view.findViewById(R.id.recyclerview12);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewplaylist.setLayoutManager(layoutManager);

// Initialize playlistItems list
        playlistItems = new ArrayList<>();

// Initialize the adapter with the playlistItems list
        adapterplay = new PlaylistAdapter12(getContext(), playlistItems);

// Set the adapter to the RecyclerView
        recyclerViewplaylist.setAdapter(adapterplay);


        loadPlaylistData();

        addhighlites=view.findViewById(R.id.addhighlites);
        addhighlites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),AddHighlitescover.class));
            }
        });

        imageView3=view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritesList = new ArrayList<>();
        adapter = new PlaylistAdapter2(favoritesList);
        recyclerView.setAdapter(adapter);

        // Fetch the user's favorites from Firebase
        fetchFavorites();


        profileImageView = view.findViewById(R.id.imageprofile);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    openGallery();
                }

            }
        });

        username2=view.findViewById(R.id.imageView2);
        username3=view.findViewById(R.id.nav_header_username);
        email4=view.findViewById(R.id.nav_header_email);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        String currentUserUid1 = firebaseAuth.getCurrentUser().getUid();

        // Retrieve the user data from the Realtime Database
        databaseReference.child(currentUserUid1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String profile=dataSnapshot.child("profileImageUrl").getValue(String.class);

                    Picasso.get().load(profile).placeholder(R.drawable.picture).
                            into(profileImageView);


                    // Set the username in the TextView

                    username2.setText(username);
                    username3.setText(username);
                    email4.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });






        return view;
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            // Set the selected image to the profile image view
            profileImageView.setImageURI(selectedImageUri);
            // Upload the selected image to Firebase Storage
            uploadImageToFirebase(selectedImageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Get current user ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Create a reference to the Firebase Storage location where the image will be stored
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(currentUserId);
        // Upload the image to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful, get the download URL
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Update the user's profile image URL in Firebase Realtime Database
                        updateProfileImageInDatabase(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Image upload failed
                    Log.e("TAG", "Failed to upload image: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileImageInDatabase(String imageUrl) {
        // Get reference to the current user's node in Firebase Realtime Database
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        // Set the profile image URL in the user's node
        userRef.child("profileImageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Profile image URL updated successfully
                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to update profile image URL
                    Log.e("TAG", "Failed to update profile image: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show();
                });

        userRef.child("Profile_photo").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {

                })
                .addOnFailureListener(e -> {

                    Log.e("TAG", "Failed to update profile image: " + e.getMessage());

                });



    }


    private void fetchFavorites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("playlist");

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoritesList.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SongModel song = snapshot.getValue(SongModel.class);
                    favoritesList.add(song);
                }
                adapter.setFavoritesList(favoritesList); // Update adapter data



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
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
                        Log.d("PlaylistData", "Added item: " + item.getCoverName() + ", ID: " + item.getId());
                    } else {
                        Log.w("PlaylistData", "Item is null at snapshot key: " + id);
                    }
                }

                adapterplay.notifyDataSetChanged();
                Log.d("PlaylistData", "RecyclerView data updated.");
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.stopShimmer();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaylistData", "Failed to load data", databaseError.toException());
                try {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                }
            }
        });
    }



}