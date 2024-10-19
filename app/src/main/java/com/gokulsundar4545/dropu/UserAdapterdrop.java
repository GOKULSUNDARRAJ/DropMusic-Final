package com.gokulsundar4545.dropu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterdrop extends RecyclerView.Adapter<UserAdapterdrop.ViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private boolean isChat;

    private String lastMessage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String token;
    public UserAdapterdrop(Context context, ArrayList<User> userList, boolean isChat) {
        this.context = context;
        this.userList = userList;
        this.isChat = isChat;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        String hisUid = user.getUid();


        Query query=FirebaseDatabase.getInstance( ).getReference("Users").orderByChild("uid").equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){
                    String name=""+ds.child("name").getValue();
                    String professional=""+ds.child("profission").getValue();
                    String profile=""+ds.child("Profile_photo").getValue();
                    String followcount=""+ds.child("followerCount").getValue(  );
                    String hisuid=""+ds.child("uid").getValue();
                    token=""+ds.child("token").getValue();


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        holder.chatLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChartActivity.class);
            intent.putExtra("uid", hisUid);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ChartActivity.class);
                intent.putExtra("hisUId",hisUid);
                intent.putExtra("myUId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                intent.putExtra("hisToken",token);
                view.getContext().startActivity(intent);

            }
        });

        Picasso.get()
                .load(user.getProfile_photo())
                .placeholder(R.drawable.profile)
                .into(holder.profileImage);


        FirebaseDatabase database;
        FirebaseAuth auth;
        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    User1 user2 = snapshot.getValue(User1.class);


                    if (user.getName().contains(user2.getName())){
                        holder.name.setText("You Contact");
                    }else {
                        holder.name.setText(user.getName());
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        holder.phonenumber.setText(user.getProfission());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ConstraintLayout chatLayout;
        public CircleImageView profileImage;
        public TextView name,phonenumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatLayout = itemView.findViewById(R.id.ChatLayout);
            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            phonenumber=itemView.findViewById(R.id.phonenumber);

        }
    }
    public void updateList(ArrayList<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }


}
