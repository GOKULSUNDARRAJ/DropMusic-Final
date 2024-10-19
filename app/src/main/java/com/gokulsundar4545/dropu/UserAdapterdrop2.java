package com.gokulsundar4545.dropu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

public class UserAdapterdrop2 extends RecyclerView.Adapter<UserAdapterdrop2.ViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private boolean isChat;

    private String lastMessage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String token;
    public UserAdapterdrop2(Context context, ArrayList<User> userList, boolean isChat) {
        this.context = context;
        this.userList = userList;
        this.isChat = isChat;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);


        holder.name.setText(user.getName());
        holder.phonenumber.setText(user.getProfission());

        // Handle send SMS button click
        holder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "SMS permission is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendSms(user.getProfission(), "https://play.google.com/store/apps/details?id=com.gokulsundar4545.dropu",user.getName());
            }
        });

    }

    private void sendSms(String phoneNumber, String message,String name) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(context, "SMS sent to " + name, Toast.LENGTH_SHORT).show();
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


}
