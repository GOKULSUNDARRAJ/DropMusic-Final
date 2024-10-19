package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.ViewHolder> {

    private Context context;
    private List<RecentChat> recentChatList;

    public RecentChatAdapter(Context context, List<RecentChat> recentChatList) {
        this.context = context;
        this.recentChatList = recentChatList != null ? recentChatList : new ArrayList<>(); // Ensure it's not null
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentChat chat = recentChatList.get(position);

        holder.messageTextView.setText(chat.getMesage());


        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(chat.getTime()));
        String dateTime = DateFormat.format("hh:mm aa", cal).toString();
        holder.timeTextView.setText(dateTime);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        // Retrieve the user data from the Realtime Database
        databaseReference.child(chat.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String profile=dataSnapshot.child("profileImageUrl").getValue(String.class);

                  holder.name.setText(username);
                    Picasso.get().load(profile).placeholder(R.drawable.picture).
                            into(holder.profile);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ChartActivity.class);
                intent.putExtra("hisUId",chat.getUid());
                intent.putExtra("myUId", FirebaseAuth.getInstance().getCurrentUser().getUid());

                context.startActivity(intent);


            }
        });


        holder.scan.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BarcodeScannerActivity.class);
            view.getContext().startActivity(intent);
            ((Activity) view.getContext()).finish();
        });


        countUnseenMessages(chat.getUid(),holder.count);
    }

    @Override
    public int getItemCount() {
        return recentChatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  messageTextView, timeTextView,name;
        CircleImageView profile;
        ImageView scan;
        TextView count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scan=itemView.findViewById(R.id.camera);
            messageTextView = itemView.findViewById(R.id.profession);
            timeTextView = itemView.findViewById(R.id.lasttime);
            name=itemView.findViewById(R.id.name);
            profile=itemView.findViewById(R.id.profile_image);
            count=itemView.findViewById(R.id.count);
        }
    }

    private void countUnseenMessages(String friendid, TextView count) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("Chat");

        chatRef.orderByChild("receiver").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int unseenCount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChatdrop chat = ds.getValue(ModelChatdrop.class);

                    if (chat != null && chat.getSender().equals(friendid) && !chat.isIsseen()) {
                        unseenCount++;
                    }
                }
                // Log the unseen count
                Log.d("UnseenMessagesCount", "Unseen messages count for friend " + friendid + ": " + unseenCount);

                // Update the TextView with the unseen count

                if (unseenCount==0){
                    count.setText("");
                    count.setVisibility(View.INVISIBLE);
                }else {
                    count.setText(String.valueOf(unseenCount));
                    count.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    public void updateList(List<RecentChat> newList) {
        recentChatList = newList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }


}
