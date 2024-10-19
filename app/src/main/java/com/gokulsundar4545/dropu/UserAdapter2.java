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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter2 extends RecyclerView.Adapter<UserAdapter2.ViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private boolean isChat;

    private String lastMessage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public UserAdapter2(Context context, ArrayList<User> userList, boolean isChat) {
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


        holder.ChatLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChartActivity.class);
            intent.putExtra("uid", hisUid);
            intent.putExtra("myUId",FirebaseAuth.getInstance().getCurrentUser().getUid());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });



        holder.name.setText(user.getName());



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout ChatLayout;
        TextView name;
        CircleImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.name);
            ChatLayout=itemView.findViewById(R.id.ChatLayout);

        }
    }






}
