package com.gokulsundar4545.dropu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListFragment extends Fragment {


    ArrayList<Chatlist> userlist;
    ArrayList<User> mUser;
    RecyclerView recyclerView;
    ChatlistAdapter3 mAdapter;
    FirebaseAuth auth;

    ImageView back;


    CircleImageView profileimage;

    DatabaseReference reference;
    FirebaseUser firebaseuser;
    FirebaseDatabase database;

    TextView name;

    ConstraintLayout bottom;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        database = FirebaseDatabase.getInstance();

        profileimage=view.findViewById(R.id.profile_image);
        auth=FirebaseAuth.getInstance();
        firebaseuser=FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bottom=view.findViewById(R.id.bottom);







        name=view.findViewById(R.id.name);

        userlist=new ArrayList<>();

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getProfile_photo())
                            .placeholder(R.drawable.profile)
                            .into(profileimage);

                    name.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference= FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                   Chatlist chatlist = ds.getValue(Chatlist.class);
                    userlist.add(chatlist);
                }
                ChatListing();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        return view;
    }

    private void ChatListing() {
        mUser = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                mUser.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    // Assuming you have a list of Chatlist (userlist) to match against
                    for (Chatlist chatlist : userlist) {
                        if (user.getUid().equals(chatlist.getId())) {
                            mUser.add(user);
                            break; // Once matched, add user and break inner loop
                        }
                    }
                }

                // Sort mUser list by last message time
                Collections.sort(mUser, new Comparator<User>() {
                    @Override
                    public int compare(User u1, User u2) {
                        Long time1 = (u1.getLasttime() != null && !u1.getLasttime().isEmpty()) ? Long.parseLong(u1.getLasttime()) : 0;
                        Long time2 = (u2.getLasttime() != null && !u2.getLasttime().isEmpty()) ? Long.parseLong(u2.getLasttime()) : 0;
                        return time2.compareTo(time1); // Descending order (latest message first)
                    }
                });

                mAdapter = new ChatlistAdapter3(getContext(), mUser, true);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }



    @Override
    public void onPause() {
        super.onPause( );
        Status("offline");
    }

    @Override
    public void onResume() {
        Status("Online");
        super.onResume( );
    }

    private void Status(String status) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        databaseReference.updateChildren(hashMap);
    }


    public void gotomain2(View view) {
        startActivity(new Intent(view.getContext(), MainActivity.class));
    }
}