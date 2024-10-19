package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
public class ChatlistAdapterdropforsent extends RecyclerView.Adapter<ChatlistAdapterdropforsent.viewHolder> {

    Context context;
    ArrayList<User> list1;
    FirebaseAuth firebaseAuth;
    boolean ischat;
    ArrayList<String> selectedItems = new ArrayList<>(); // List to store selected item UIDs

    String thelastmsg;
    String time;

    ImageView check;
    public ChatlistAdapterdropforsent(Context context, ArrayList<User> list, boolean ischat) {
        this.context = context;
        this.list1 = list;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatlistforsent, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatlistAdapterdropforsent.viewHolder holder, int position) {
        final String hisUid = list1.get(position).getUid();
        firebaseAuth = FirebaseAuth.getInstance();



        holder.scan.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BarcodeScannerActivity.class);
            view.getContext().startActivity(intent);
            ((Activity) view.getContext()).finish();
        });

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        String myuid = user1.getUid();

        holder.ChatLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, ChartActivity.class);
                intent.putExtra("hisUId", hisUid);
                intent.putExtra("myUId", myuid);
                intent.putExtra("hisToken", list1.get(position).getToken());
                context.startActivity(intent);

                return false;

            }
        });


        User user = list1.get(position);
        if (user.getStatus().equals("online")) {
            holder.online.setVisibility(View.VISIBLE);
        } else {
            holder.online.setVisibility(View.GONE);
        }

        if (ischat) {
            LastMessage(user.getUid(), holder.lastMsg, holder.lasttime);
        } else {
            holder.lastMsg.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(user.getProfile_photo())
                .placeholder(R.drawable.placeholder)
                .into(holder.profileImage);

        FirebaseDatabase database;
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User1 user2 = snapshot.getValue(User1.class);
                    if (user.getName().contains(user2.getName())) {
                        holder.name.setText("You Contact");
                    } else {
                        holder.name.setText(user.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Handle item selection
        holder.itemView.setOnClickListener(view -> {
            if (selectedItems.contains(hisUid)) {
                selectedItems.remove(hisUid);
                holder.check.setVisibility(View.GONE);
            } else {
                selectedItems.add(hisUid);
                holder.check.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public TextView lastMsg, name;
        TextView lasttime;
        ImageView camera;
        ConstraintLayout ChatLayout;
        ImageView profileImage, scan;
        CircleImageView online;

        ImageView check;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ChatLayout = itemView.findViewById(R.id.ChatLayout);
            profileImage = itemView.findViewById(R.id.profile_image);
            lastMsg = itemView.findViewById(R.id.profession);
            lasttime = itemView.findViewById(R.id.lasttime);
            camera = itemView.findViewById(R.id.camera);
            online = itemView.findViewById(R.id.online);
            scan = itemView.findViewById(R.id.camera);

            check=itemView.findViewById(R.id.check);
        }
    }

    private void LastMessage(String friendid, TextView lastmsg, TextView lasttime) {
        thelastmsg = "default";
        time = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChatdrop chat = ds.getValue(ModelChatdrop.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getSender().equals(friendid) && chat.getReceiver().equals(firebaseUser.getUid()) ||
                                chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(friendid)) {
                            thelastmsg = chat.getMessage();
                            time = chat.getTimestamp();
                        }
                    }
                }
                switch (thelastmsg) {
                    case "default":
                        lastmsg.setText("No Message");
                        lasttime.setText("");
                        break;
                    default:
                        if (thelastmsg.contains("https://firebasestorage.googleapis.com/")) {
                            lastmsg.setText("send a image");
                        } else {
                            lastmsg.setText(thelastmsg);
                        }
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(time));
                        String dateTime = DateFormat.format("hh:mm aa", cal).toString();
                        lasttime.setText(dateTime);
                }
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(friendid);
                userRef.child("lasttime").setValue(time); // Update lasttime
                thelastmsg = "default";
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    // Method to get selected items
    public ArrayList<String> getSelectedItems() {
        return selectedItems;
    }
}
