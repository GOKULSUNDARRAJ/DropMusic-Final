package com.gokulsundar4545.dropu;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "chat_channel";
    String hisUid;
    String myuid;

    String hisname;
    String username;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String message = remoteMessage.getNotification().getBody();
        String hisUid = remoteMessage.getData().get("chatId");
        sendNotification(message, hisUid);
    }

    private void sendNotification(String messageBody, String senderUid) {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("hisUId", senderUid);
        intent.putExtra("myUId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieve the user data from the Realtime Database
        databaseReference.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_custom_layout);




                    String username = dataSnapshot.child("username").getValue(String.class);
                    String profile=dataSnapshot.child("profileImageUrl").getValue(String.class);
//                    Toast.makeText(MyFirebaseMessagingService.this, ""+profile, Toast.LENGTH_SHORT).show();
//
//                    Glide.with(MyFirebaseMessagingService.this)
//                            .asBitmap()
//                            .load(profile)
//                            .placeholder(R.drawable.placeholder) // Optional placeholder
//                            .into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                    notificationLayout.setImageViewBitmap(R.id.profile_image, resource);
//
//
//                                }
//                            });


                    notificationLayout.setTextViewText(R.id.notification_title, username);
                    notificationLayout.setTextViewText(R.id.notification_text, messageBody);

                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    String channelId = CHANNEL_ID;

                    // Inflate the custom layout


                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.mipmap.ic_launcher_round) // Ensure this icon is correctly set
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setCustomContentView(notificationLayout);
//                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle()); // Set custom layout

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Chat Notifications",
                                NotificationManager.IMPORTANCE_HIGH);
                        channel.setDescription("Channel description");
                        channel.enableLights(true);
                        channel.setLightColor(Color.RED);
                        notificationManager.createNotificationChannel(channel);

                    }

                    notificationManager.notify(0, notificationBuilder.build());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error

            }
        });

    }

//    private void sendMessage(String message) {
//
//        DatabaseReference databaseReferenc2 = FirebaseDatabase.getInstance().getReference("Users");
//        // Retrieve the user data from the Realtime Database
//        databaseReferenc2.child(hisUid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Check if the user data exists
//                if (dataSnapshot.exists()) {
//                    // User data exists, retrieve the username
//                    hisname = dataSnapshot.child("username").getValue(String.class);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle database error
//                Toast.makeText(getApplicationContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Users");
//        String currentUserUid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Retrieve the user data from the Realtime Database
//        databaseReference3.child(currentUserUid1).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Check if the user data exists
//                if (dataSnapshot.exists()) {
//                    // User data exists, retrieve the username
//                    username = dataSnapshot.child("username").getValue(String.class);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle database error
//                Toast.makeText(getApplicationContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        String timestamp=String.valueOf(System.currentTimeMillis());
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        myuid=user.getUid();
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("sender",myuid);
//        hashMap.put("receiver",hisUid);
//        hashMap.put("message",message);
//        hashMap.put("onlinestatus","onlinestatus");
//        hashMap.put("timestamp",timestamp);
//        hashMap.put("isseen",false);
//        hashMap.put("type","text");
//
//        databaseReference.child("Chat").push().setValue(hashMap);
//
//        message1.setText("");
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
//                .child("Users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("lastmessagetime")
//                .child(hisUid);
//
//        userRef.child("time").setValue(timestamp);
//        userRef.child("mesage").setValue(message);
//        userRef.child("name").setValue(hisname);
//
//
//        DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference()
//                .child("Users")
//                .child(hisUid)
//                .child("lastmessagetime")
//                .child(myuid);
//
//        userRef2.child("time").setValue(timestamp);
//        userRef2.child("mesage").setValue(message);
//        userRef2.child("name").setValue(username);
//
//
//
//
//
//
//        final DatabaseReference chatRef1=FirebaseDatabase.getInstance()
//                .getReference("Chatlist")
//                .child(myuid)
//                .child(hisUid);
//        chatRef1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull  DataSnapshot snapshot) {
//                if (!snapshot.exists()){
//                    chatRef1.child("id").setValue(hisUid);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull  DatabaseError error) {
//
//            }
//        });
//
//
//        final DatabaseReference chatRef2=FirebaseDatabase.getInstance()
//                .getReference("Chatlist")
//                .child(hisUid)
//                .child(myuid);
//        chatRef2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull  DataSnapshot snapshot) {
//                if (!snapshot.exists()){
//                    chatRef2.child("id").setValue(myuid);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull  DatabaseError error) {
//
//            }
//        });
//
//
//        sendNotificationToReceiver(hisUid, message);
//    }

}
