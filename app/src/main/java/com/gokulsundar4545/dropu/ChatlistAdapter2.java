package com.gokulsundar4545.dropu;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ChatlistAdapter2 extends RecyclerView.Adapter<ChatlistAdapter2.viewHolder> {

    Context context;
    ArrayList<User> list1;
    FirebaseAuth firebaseAuth;
    boolean ischat;

    String thelastmsg;
    String time;


    public ChatlistAdapter2(Context context, ArrayList<User> list, boolean ischat) {
        this.context = context;
        this.list1 = list;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chatlist,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  viewHolder holder, int position) {


        final String hisUid=list1.get(position).getUid();
        firebaseAuth=FirebaseAuth.getInstance();




        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        String myuid=user1.getUid();


        User user=list1.get(position);





        if (ischat){
            LastMessage(user.getUid(),holder.lastMsg,holder.lasttime);
        }else {
            holder.lastMsg.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(user.getProfile_photo())
                .placeholder(R.drawable.placeholder)
                .into(holder.profileImage);

        holder.name.setText(user.getName());











    }

    @Override
    public int getItemCount() {
        return list1.size();

    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView lastMsg;
        TextView lasttime;
        ConstraintLayout ChatLayout;
        TextView name;
        CircleImageView profileImage,online;

        public viewHolder(@NonNull  View itemView) {
            super(itemView);

            profileImage=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.name);
            ChatLayout=itemView.findViewById(R.id.ChatLayout);
            lastMsg=itemView.findViewById(R.id.profession);
            lasttime=itemView.findViewById(R.id.lasttime);
            online=itemView.findViewById(R.id.online);
        }
    }

    private void LastMessage(String friendid, TextView lastmsg,TextView lasttime){
        thelastmsg="default";
        time="dafault";
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    ModelChat2 chat=ds.getValue(ModelChat2.class);

                    if (firebaseUser!=null && chat!=null){

                        if (chat.getSender().equals(friendid) && chat.getReceiver().equals(firebaseUser.getUid()) ||
                                chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(friendid)){
                            thelastmsg=chat.getMessage();
                            time=chat.getTimestamp();


                        }
                    }

                }

                switch (thelastmsg){
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
                thelastmsg="default";
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
