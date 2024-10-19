package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class recentchatActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private List<RecentChat> recentChatList;
    private RecentChatAdapter recentChatAdapter;
    private RecyclerView recyclerView;
    private EditText searchEditText; // Search bar

    ConstraintLayout emptylayout1,emptylayout2;
    TextView emptytxt2;

    private ShimmerFrameLayout shimmerFrameLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recentchat);

        ImageView back=findViewById(R.id.imageView9);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

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


        emptylayout1=findViewById(R.id.emptylayout);
        emptylayout2=findViewById(R.id.emptylayout2);
        // Initialize views
        fab = findViewById(R.id.fab);
        searchEditText = findViewById(R.id.messageEd); // Initialize search EditText

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchChatMainActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(findViewById(R.id.fab), "transition_login");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(recentchatActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        recyclerView = findViewById(R.id.RecyclerView);

        // Initialize the list
        recentChatList = new ArrayList<>();
        recentChatAdapter = new RecentChatAdapter(recentchatActivity.this, recentChatList);

        recyclerView.setLayoutManager(new LinearLayoutManager(recentchatActivity.this));
        recyclerView.setAdapter(recentChatAdapter);

        // Call recentChat() to load data
        recentChat();

        // Add a TextWatcher to filter the list based on user input
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
                emptylayout1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list as the user types
                filterRecentChats(charSequence.toString());
                emptylayout1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
                emptylayout1.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Method to load recent chats from Firebase
    private void recentChat() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(currentUserId)
                .child("lastmessagetime");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentChatList.clear(); // Clear old data

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // UID like "d56tbHPC3iZOn5jR3a7ryhVRNVi1"
                    String message = snapshot.child("mesage").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    if (message == null) message = "";
                    if (time == null) time = "";

                    recentChatList.add(new RecentChat(uid, message, time, name));
                }

                // Sort by time (Descending)
                Collections.sort(recentChatList, new Comparator<RecentChat>() {
                    @Override
                    public int compare(RecentChat chat1, RecentChat chat2) {
                        return Long.compare(Long.parseLong(chat2.getTime()), Long.parseLong(chat1.getTime()));
                    }
                });

                if (recentChatList.isEmpty()){
                    emptylayout1.setVisibility(View.VISIBLE);
                }else {
                    emptylayout1.setVisibility(View.INVISIBLE);
                }
                // Notify the adapter of data changes
                recentChatAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.stopShimmer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Database error: " + databaseError.getMessage());
            }
        });
    }

    // Method to filter the chat list
    private void filterRecentChats(String query) {
        List<RecentChat> filteredList = new ArrayList<>();

        for (RecentChat chat : recentChatList) {
            if (chat.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(chat);
            }
        }

        emptytxt2=findViewById(R.id.emptytxt2);
        if (filteredList.isEmpty()){
            emptylayout2.setVisibility(View.VISIBLE);
            emptytxt2.setText("Friends not found in your search "+searchEditText.getText().toString());
        }else {
            emptylayout2.setVisibility(View.INVISIBLE);
        }
        recentChatAdapter.updateList(filteredList);
    }
}
