package com.gokulsundar4545.dropu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBottomSheetFragmentforsentMessage extends BottomSheetDialogFragment {

    ArrayList<Chatlistdrop> userlist;
    ArrayList<User> mUser;
    RecyclerView recyclerView;
    ChatlistAdapterdropforsent mAdapter;
    FirebaseAuth auth;

    ImageView back;


    CircleImageView profileimage;

    DatabaseReference reference;
    FirebaseUser firebaseuser;
    FirebaseDatabase database;

    TextView name;

    ConstraintLayout bottom;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_bottom_sheetforsentmessage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
                    Chatlistdrop chatlist = ds.getValue(Chatlistdrop.class);
                    userlist.add(chatlist);
                }
                ChatListing();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });







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
                    for (Chatlistdrop chatlist : userlist) {
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

                mAdapter = new ChatlistAdapterdropforsent(getContext(), mUser, true);
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


    private void sendMessage(String message, String imageUrl, ArrayList<String> selectedUIDs) {
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String myUid = user.getUid();

        // Combined message HashMap
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("sender", myUid);
        messageMap.put("timestamp", timestamp);
        messageMap.put("isseen", false);

        messageMap.put("type", "both"); // Set type to "both"

        // Check if message is text
        if (message != null && !message.trim().isEmpty()) {
            messageMap.put("message", message);
        }

        // Check if message is image URL
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            messageMap.put("messageimage", imageUrl);
        }

        // Save message to Firebase Database for each recipient
        for (int i = 0; i < selectedUIDs.size(); i++) {
            String hisUid = selectedUIDs.get(i);
            messageMap.put("receiver", hisUid);

            databaseReference.child("Chat").push().setValue(messageMap)
                    .addOnSuccessListener(aVoid -> {
                        updateChatList(myUid, hisUid);
                        sendNotificationToReceiver(hisUid, "Shared a Song");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TAG", "Failed to send message: " + e.getMessage());
                        Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
                    });

            // Send notification via FCM for each recipient


        }



        Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();

        // Log and handle responses if needed for FCM notifications
    }

    private void sendNotificationToReceiver(String receiverUid, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("Users").child(receiverUid);

        tokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String token = snapshot.child("fcmToken").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);

                    if (token != null && username != null) {
                        sendFCMNotification(token, username, message);
                    } else {
                        Toast.makeText(getContext(), "Error: Token or username is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }

    private void sendFCMNotification(String token, String username, String message) {

        SendNotification sendNotification=new SendNotification(token,username,message,getContext());
        sendNotification.sendNotification();

    }


    private void sendNotification(APIService apiService, String myUid, String hisUid, String hisToken, String messageContent) {
        Data data = new Data(myUid, R.drawable.logo,
                messageContent,
                "New Message or Image",
                hisUid);
        Sender sender = new Sender(data, hisToken);

        apiService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        Log.e("tag", "response.body().success: " + response.body());
                        Log.e("tag", "response.code(): " + response.code());
                        if (response.code() == 200) {
                            Log.e("tag", "response.body().success: " + response.body().success);
                            if (response.body().success != 1) {
                                // Handle failure scenario if needed
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.e("tag", "onFailure: " + t.getMessage());
                    }
                });
    }

    private void updateChatList(String myUid, String hisUid) {
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(myUid).child(hisUid);
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef1.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to update chat list: " + error.getMessage());
            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist").child(hisUid).child(myUid);
        chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef2.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to update chat list: " + error.getMessage());
            }
        });
    }
}

