package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String hisUid;
    private TextView hisname;

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;


    private static final int IMAGE_PICK_CODE = 1000;

    private ProgressBar progressBar;

    private TextView hisOnlineTextView;
    String Profile;
    ImageView profileImageView;

    private static final int PERMISSION_CODE = 1001;

    private ImageView playButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        hisOnlineTextView = findViewById(R.id.hisonline); // Initialize hisOnlineTextView if not already done


        profileImageView = findViewById(R.id.profile);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ChatActivity.this, ZoomActivity.class);
                // Pass image URL as an extra
                intent.putExtra("imageUrl", Profile);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(profileImageView, "transaction_player");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);

                startActivity(intent, options.toBundle());

            }
        });

        progressBar = findViewById(R.id.progressBar);


        hisname = findViewById(R.id.hisname);
        recyclerView = findViewById(R.id.ChatRecycle);

        Intent intent = getIntent();
        if (intent != null) {
            String hisName = intent.getStringExtra("contact_name");
            Profile = intent.getStringExtra("contact_Profile");
            hisUid = intent.getStringExtra("clicked_user_uid");
            loadLastSeenStatus(hisUid);
            hisname.setText(hisName);


            Picasso.get().load(Profile).into(profileImageView);


            setTitle(hisName);
        }

        // Initialize play and stop buttons
        playButton = findViewById(R.id.playButton);


        // Initialize Firebase Realtime Database
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUserId());
        DatabaseReference otherUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(hisUid);

        // Listen for changes in the online status of both users
        currentUserRef.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentUserOnlineStatus = dataSnapshot.getValue(String.class);
                // Handle current user's online status
                handleOnlineStatus(currentUserOnlineStatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        otherUserRef.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String otherUserOnlineStatus = dataSnapshot.getValue(String.class);
                // Handle other user's online status
                handleOnlineStatus(otherUserOnlineStatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });





        findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = findViewById(R.id.messageEd);
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessageToUser(message, "text");
                    messageEditText.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.attachbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Request the permission
                    ActivityCompat.requestPermissions(ChatActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_CODE);
                } else {
                    // Permission has already been granted
                    // Proceed with your code to set OnClickListener
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_PICK_CODE);
                }

            }
        });

        // Initialize RecyclerView
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Initialize message list
        messageList = new ArrayList<>();

        // Set up adapter
        chatAdapter = new ChatAdapter(messageList, getCurrentUserId());
        recyclerView.setAdapter(chatAdapter);

        // Load messages from Firebase
        loadMessagesFromFirebase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            sendImageMessage(imageUri);
        }
    }

    private void sendMessageToUser(String message, String messageType) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String myUid = currentUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("sender", myUid);
        messageMap.put("receiver", hisUid); // Use the recipient's UID here
        messageMap.put("message", message);
        messageMap.put("timestamp", ServerValue.TIMESTAMP);
        messageMap.put("isseen", false);
        messageMap.put("onlinestatus", "onlinestatus");
        messageMap.put("type", messageType);


        String messageKey = databaseReference.child("Messages").push().getKey();

        databaseReference.child("Messages").child(messageKey).setValue(messageMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendImageMessage(Uri imageUri) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("chat_images");
        String imageName = System.currentTimeMillis() + "." + getFileExtension(imageUri);
        storageReference.child(imageName).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                sendMessageToUser(downloadUri.toString(), "image");
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void loadMessagesFromFirebase() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        Query query = messagesRef.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    // Check if the message is sent or received by the current user
                    if (message != null && (message.getSender().equals(getCurrentUserId()) && message.getReceiver().equals(hisUid)) ||
                            (message.getSender().equals(hisUid) && message.getReceiver().equals(getCurrentUserId()))) {
                        messageList.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to load messages: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }

    private void updateOnlineStatus(boolean isOnline) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
            HashMap<String, Object> statusMap = new HashMap<>();
            statusMap.put("online", isOnline ? "true" : "false");
            if (!isOnline) {
                statusMap.put("lastSeen", ServerValue.TIMESTAMP);
            }
            userRef.updateChildren(statusMap);
        }
    }

    private void loadLastSeenStatus(String hisUid) {
        if (hisUid != null) {
            DatabaseReference recipientRef = FirebaseDatabase.getInstance().getReference().child("Users").child(hisUid);
            recipientRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String onlineStatus = dataSnapshot.child("online").getValue(String.class);
                        if (onlineStatus != null && onlineStatus.equals("true")) {
                            // User is online
                            hisOnlineTextView.setText("Online");
                        } else {
                            // User is offline, show last seen
                            Long lastSeenTimestamp = dataSnapshot.child("lastSeen").getValue(Long.class);
                            if (lastSeenTimestamp != null) {
                                String lastSeen = "Last seen " + DateUtils.getRelativeTimeSpanString(lastSeenTimestamp);
                                hisOnlineTextView.setText(lastSeen);
                            } else {
                                hisOnlineTextView.setText("Last seen not available");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ChatActivity.this, "Failed to load last seen status: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Update online status to true when activity starts
            updateOnlineStatus(true);
        } else {
            Toast.makeText(ChatActivity.this, "Error: hisUid is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateOnlineStatus(false);
    }

    private void handleOnlineStatus(String onlineStatus) {
        // If both users are online, enable the play button
        if (onlineStatus != null && onlineStatus.equals("true")) {
            playButton.setVisibility(View.VISIBLE);
        } else {
            // If any user goes offline, disable the play button
            playButton.setVisibility(View.INVISIBLE);
        }
    }

}


