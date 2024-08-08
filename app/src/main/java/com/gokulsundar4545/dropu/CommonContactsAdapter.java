package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommonContactsAdapter extends RecyclerView.Adapter<CommonContactsAdapter.ViewHolder> {

    private List<ContactActivity.User> contactsList;
    String thelastMessage;


    public CommonContactsAdapter(List<ContactActivity.User> contactsList) {
        this.contactsList = contactsList;
    }


    public void filterList(List<ContactActivity.User> filteredList) {
        contactsList = filteredList;
        notifyDataSetChanged();
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

        holder.scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(),BarcodeScannerActivity.class);
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).finish();

            }
        });


        Picasso.get().
                load(contact.getProfileimage())
                .placeholder(R.drawable.personchat)
                .into(holder.profile_image);

        holder.carproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the UID of the clicked user
                String clickedUserUid = contact.getUid();

                // Start ChatActivity and pass contact information and clicked user UID
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("contact_name", contact.getName());
                intent.putExtra("contact_phone", contact.getPhoneNumber());
                intent.putExtra("contact_email", contact.getEmail());
                intent.putExtra("contact_Profile", contact.getProfileimage());
                intent.putExtra("clicked_user_uid", clickedUserUid); // Pass the clicked user UID
                view.getContext().startActivity(intent);
            }
        });
        holder.carproduct.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recycler2));

        lastMessage(contact.getUid(), holder.lastmessage, contact.getName(), holder.lastmessagetime);

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView profile_image,scan;
        TextView phoneTextView;
        TextView emailTextView, lastmessage, lastmessagetime;
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
            scan=itemView.findViewById(R.id.scan);
        }
    }

    private synchronized void lastMessage(String userid, TextView last_msg, String name, TextView lastmessagetime) {
        thelastMessage = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message chat = snapshot.getValue(Message.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                        thelastMessage = chat.getMessage();

                        String indianTime = convertToIndianTime(chat.getTimestamp());
                        lastmessagetime.setText(indianTime);


                    }
                }

                switch (thelastMessage) {
                    case "default":
                        last_msg.setText("No message available");
                        break;
                    default:
                        // Check if the last message is a URL
                        if (isUrl(thelastMessage)) {
                            last_msg.setText("sent a image or link by " + name);
                        } else {
                            last_msg.setText(thelastMessage);


                        }
                }

                thelastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the read operation
            }
        });
    }

    // Helper method to check if a string is a URL
    private boolean isUrl(String message) {
        // You can implement a simple URL detection logic here
        // For simplicity, let's assume any string starting with "http://" or "https://" is a URL
        return message.startsWith("http://") || message.startsWith("https://");
    }

    private String convertToIndianTime(long timestamp) {
        // Create SimpleDateFormat objects for time with the desired format and time zone (IST)
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set time zone to IST

        // Convert timestamp to Date object
        Date date = new Date(timestamp);

        // Format the time
        String formattedTime = timeFormat.format(date);

        // Get the hour of the day from the formatted time
        int hour = Integer.parseInt(formattedTime.substring(0, 2));

        // Set "PM" or "AM" explicitly based on the hour
        if (hour >= 12) {
            formattedTime = formattedTime.replace("AM", "PM");
        } else {
            formattedTime = formattedTime.replace("PM", "AM");
        }

        // Return formatted time
        return formattedTime;
    }


}
