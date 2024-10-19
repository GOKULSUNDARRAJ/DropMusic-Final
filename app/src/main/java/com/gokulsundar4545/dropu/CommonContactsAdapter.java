package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class CommonContactsAdapter extends RecyclerView.Adapter<CommonContactsAdapter.ViewHolder> {

    private List<ContactActivity.User> contactsList;
    private Map<String, Long> timestamps = new HashMap<>();

    public CommonContactsAdapter(List<ContactActivity.User> contactsList) {
        this.contactsList = contactsList;
        fetchLastMessageTimestampsForAllContacts();
    }

    public void filterList(List<ContactActivity.User> filteredList) {
        contactsList = filteredList;
        fetchLastMessageTimestampsForAllContacts();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactActivity.User contact = contactsList.get(position);
        holder.nameTextView.setText(contact.getName() + ".");
        holder.phoneTextView.setText(contact.getPhoneNumber());
        holder.emailTextView.setText(contact.getEmail());

        holder.scan.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BarcodeScannerActivity.class);
            view.getContext().startActivity(intent);
            ((Activity) view.getContext()).finish();
        });

        Picasso.get()
                .load(contact.getProfileimage())
                .placeholder(R.drawable.personchat)
                .into(holder.profile_image);

        holder.carproduct.setOnClickListener(view -> {
            String clickedUserUid = contact.getUid();
            Intent intent = new Intent(view.getContext(), ChatActivity.class);
            intent.putExtra("contact_name", contact.getName());
            intent.putExtra("contact_phone", contact.getPhoneNumber());
            intent.putExtra("contact_email", contact.getEmail());
            intent.putExtra("contact_Profile", contact.getProfileimage());
            intent.putExtra("clicked_user_uid", clickedUserUid);
            view.getContext().startActivity(intent);
        });

        holder.carproduct.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler2));

        lastMessage(contact.getUid(), holder.lastmessage, contact.getName(), holder.lastmessagetime);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView, emailTextView, lastmessage, lastmessagetime;
        ImageView profile_image, scan;
        androidx.constraintlayout.widget.ConstraintLayout carproduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            carproduct = itemView.findViewById(R.id.carproduct);
            profile_image = itemView.findViewById(R.id.profile_image);
            lastmessage = itemView.findViewById(R.id.lastmessage);
            lastmessagetime = itemView.findViewById(R.id.lastmessagetime);
            scan = itemView.findViewById(R.id.scan);
        }
    }

    private void fetchLastMessageTimestampsForAllContacts() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");

        timestamps.clear();
        for (ContactActivity.User user : contactsList) {
            String userId = user.getUid();
            reference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long latestTimestamp = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message chat = snapshot.getValue(Message.class);
                        if ((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)) ||
                                (chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid()))) {
                            latestTimestamp = Math.max(latestTimestamp, chat.getTimestamp());
                        }
                    }

                    if (latestTimestamp > 0) {
                        timestamps.put(userId, latestTimestamp);
                    }

                    Log.d("FetchTimestamps", "User: " + userId + ", Timestamp: " + latestTimestamp);

                    // Sort contactsList based on updated timestamps
                    sortContactsListByTimestamp();

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
    }

    private void sortContactsListByTimestamp() {
        Collections.sort(contactsList, (u1, u2) -> {
            Long time1 = timestamps.getOrDefault(u1.getUid(), 0L);
            Long time2 = timestamps.getOrDefault(u2.getUid(), 0L);
            return Long.compare(time2, time1); // Descending order (latest message first)
        });
    }

    private void lastMessage(String userId, TextView last_msg, String name, TextView lastmessagetime) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMessage = "default";
                long lastTimestamp = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message chat = snapshot.getValue(Message.class);
                    if ((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)) ||
                            (chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid()))) {
                        lastMessage = chat.getMessage();
                        lastTimestamp = Math.max(lastTimestamp, chat.getTimestamp());
                    }
                }

                // Update timestamp
                timestamps.put(userId, lastTimestamp);

                Log.d("LastMessage", "User: " + userId + ", LastTimestamp: " + lastTimestamp);

                String indianTime = convertToIndianTime(lastTimestamp);
                lastmessagetime.setText(indianTime);

                if ("default".equals(lastMessage)) {
                    last_msg.setText("No message available");
                } else {
                    last_msg.setText(isUrl(lastMessage) ? "sent an image or link by " + name : lastMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private boolean isUrl(String message) {
        return message.matches("^(http://|https://).*$");
    }

    private String convertToIndianTime(long timestamp) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return timeFormat.format(new Date(timestamp));
    }
}
