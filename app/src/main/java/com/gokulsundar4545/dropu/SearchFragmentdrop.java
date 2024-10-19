package com.gokulsundar4545.dropu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SearchFragmentdrop extends Fragment {

    ArrayList<User> list = new ArrayList<>();
    ArrayList<User> filteredList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;
    UserAdapterdrop adapter;
    RecyclerView userRv;
    Map<String, String> contactsMap;
    ImageView invite;
    ConstraintLayout emptylayout,emptylayout2;
    ShimmerFrameLayout shimmerFrameLayout;
    EditText searchBar;
    TextView emptytxt2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        emptytxt2=view.findViewById(R.id.emptytxt2);
        emptylayout = view.findViewById(R.id.emptylayout);
        emptylayout2 = view.findViewById(R.id.emptylayout2);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        invite = view.findViewById(R.id.invite);
        searchBar = view.findViewById(R.id.name);

        // Initialize FirebaseAuth and FirebaseDatabase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Initialize RecyclerView and adapter
        userRv = view.findViewById(R.id.matchedRv);
        adapter = new UserAdapterdrop(getContext(), list, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        userRv.setLayoutManager(layoutManager);
        userRv.setAdapter(adapter);

        // Start shimmer effect
        if (shimmerFrameLayout != null) {
            shimmerFrameLayout.startShimmer();
            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setDuration(1500)
                    .build();
            shimmerFrameLayout.setShimmer(shimmer);
        } else {
            Log.e("ShimmerFragment", "ShimmerFrameLayout not found in layout");
        }

        // Invite button click listener
        invite.setOnClickListener(v -> startActivity(new Intent(v.getContext(), InviteActivity.class)));

        // Check and request permission to read contacts
        if (checkPermission()) {
            contactsMap = ContactHelper.getContacts(getActivity().getContentResolver());
            getAllUser(); // Fetch users from Firebase after getting contacts
        } else {
            requestPermission(); // Request permission if not granted
        }

        // Add text change listener for search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString()); // Filter the list as the text changes
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    // Fetch all users from Firebase
    private void getAllUser() {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    User user = datasnapshot.getValue(User.class);
                    if (user != null) {
                        String phoneNumber = user.getProfission(); // Fetch profession or phone number
                        if (phoneNumber != null && contactsMap.containsKey(phoneNumber)) {
                            user.setUserID(datasnapshot.getKey());
                            list.add(user); // Add to list if the user matches a contact
                        }
                    }
                }
                adapter.updateList(list); // Notify the adapter of the new data
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.stopShimmer();

                // Show empty layout if no users are found
                if (list.isEmpty()) {
                    emptylayout.setVisibility(View.VISIBLE);
                } else {
                    emptylayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter users based on search text
    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(list); // Show all users if search text is empty
        } else {
            for (User user : list) {
                if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(user); // Add users that match the search text
                }
            }
        }
        adapter.updateList(filteredList); // Update adapter with the filtered list

        // Show empty layout if no results are found
        if (filteredList.isEmpty()) {
            emptylayout2.setVisibility(View.VISIBLE);
            emptytxt2.setText("Friends not found in your search "+searchBar.getText().toString());
        } else {
            emptylayout2.setVisibility(View.INVISIBLE);
        }
    }

    // Check for READ_CONTACTS permission
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    // Request READ_CONTACTS permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactsMap = ContactHelper.getContacts(getActivity().getContentResolver());
                getAllUser(); // Fetch users after permission is granted
            } else {
                Toast.makeText(getContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
