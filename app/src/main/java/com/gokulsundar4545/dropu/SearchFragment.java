package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView userRv;
    private ArrayList<User> list = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private UserAdapter2 adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize FirebaseAuth and FirebaseDatabase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Initialize RecyclerView
        userRv = view.findViewById(R.id.matchedRv);
        adapter = new UserAdapter2(getContext(), list, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        userRv.setLayoutManager(layoutManager);

        getAllUser(); // Call method to fetch users from Firebase

        return view;
    }

    private void getAllUser() {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    User user = datasnapshot.getValue(User.class);
                    user.setUserID(datasnapshot.getKey());

                    // Exclude current user from the list
                    if (auth.getCurrentUser() != null && !datasnapshot.getKey().equals(auth.getCurrentUser().getUid())) {
                        list.add(user);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        userRv.setAdapter(adapter);
    }
}
