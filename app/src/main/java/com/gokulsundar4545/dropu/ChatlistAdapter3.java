package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
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

public class ChatlistAdapter3 extends RecyclerView.Adapter<ChatlistAdapter3.viewHolder> {

    Context context;
    ArrayList<User> list1;
    FirebaseAuth firebaseAuth;
    boolean ischat;

    String thelastmsg;
    String time;


    public ChatlistAdapter3(Context context, ArrayList<User> list, boolean ischat) {
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
    public void onBindViewHolder(@NonNull  ChatlistAdapter3.viewHolder holder, int position) {


        final String hisUid=list1.get(position).getUid();
        firebaseAuth=FirebaseAuth.getInstance();




        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        String myuid=user1.getUid();




        holder.ChatLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ChartActivity.class);
                intent.putExtra("hisUId",hisUid);
                intent.putExtra("myUId",myuid);
                intent.putExtra("hisToken",list1.get(position).getToken());
                context.startActivity(intent);

            }
        });

        User user=list1.get(position);

        holder.name.setText(user.getName());


    }

    @Override
    public int getItemCount() {
        return list1.size();

    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView lastMsg;
        TextView lasttime,name;

        ImageView camera;
        ConstraintLayout ChatLayout;
        public viewHolder(@NonNull  View itemView) {
            super(itemView);
            ChatLayout=itemView.findViewById(R.id.ChatLayout);
            name=itemView.findViewById(R.id.name);
        }
    }


}
