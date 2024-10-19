package com.gokulsundar4545.dropu;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChartActivity extends AppCompatActivity {


    FirebaseAuth auth;
    String hisUid;
    String myuid;
    String hisImage;

    TextView nametv, hisonline;
    de.hdodenhof.circleimageview.CircleImageView profile;

    FirebaseDatabase database;
    RecyclerView recyclerView;
    DatabaseReference reference;
    ConstraintLayout choose;

    String username;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_CAMER_CODE = 300;
    private static final int IMAGE_PICK_GALARY_CODE = 400;


    String[] cameraPermission;
    String[] storagePermission;

    Uri image_rui = null;

    APIService apiService;
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChatdrop> chatList;
    ChatAdapterdrop chatAdapter;

    ImageButton sendbtn, attachbtn1;

    EditText message1;

    ImageView back;

    String hisname;

    ConstraintLayout Imageboth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);

        StrictMode.ThreadPolicy p=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);
        choose=findViewById(R.id.choose);
        readMessage();
        seenMessage();


        progressBar=findViewById(R.id.progressBarnew);



        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        sendbtn = findViewById(R.id.sendbutton);

        recyclerView = findViewById(R.id.ChatRecycle);
        message1 = findViewById(R.id.messageEd);
        profile = findViewById(R.id.profile_image);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        nametv = findViewById(R.id.hisname);
        hisonline = findViewById(R.id.hisonline);

        attachbtn1 = findViewById(R.id.attachbtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUId");
        myuid = intent.getStringExtra("myUId");


        Log.e("tag","hisUid: " +hisUid);
        Log.e("tag","myuid: " +myuid);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String currentUserUid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve the user data from the Realtime Database
        databaseReference.child(currentUserUid1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    username = dataSnapshot.child("username").getValue(String.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ChartActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference databaseReferenc2 = FirebaseDatabase.getInstance().getReference("Users");
        // Retrieve the user data from the Realtime Database
        databaseReferenc2.child(hisUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    hisname = dataSnapshot.child("username").getValue(String.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ChartActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });





        Query Userquery = reference.orderByChild("uid").equalTo(hisUid);

        Userquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("Profile_photo").getValue();
                    String typingstatus = "" + ds.child("typingstatus").getValue();


                    if (typingstatus.equals(myuid)) {
                        hisonline.setText("Typing....");
                    } else {
                        try {
                            String onlinestatus = "" + ds.child("onlinestatus").getValue();
                            if (onlinestatus.equals("online")) {
                                hisonline.setText(onlinestatus);
                            } else {

                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeInMillis(Long.parseLong(onlinestatus));
                                String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                                hisonline.setText("Last seen at :" + dateTime);


                            }

                        } catch (Exception e) {

                        }

                    }


                    nametv.setText(name);
                    try {
                        Picasso.get()
                                .load(hisImage)
                                .placeholder(R.drawable.profile)
                                .into(profile);
                    } catch (Exception e) {
                        Picasso.get()
                                .load(R.drawable.profile)
                                .placeholder(R.drawable.profile)
                                .into(profile);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = message1.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);
                sendbtn.setVisibility(View.GONE);

                if (TextUtils.isEmpty(message)&& image_rui != null) {

                    try {
                        sendImageMessage(image_rui);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (message.contains("") && image_rui != null) {
                    try {
                        sendBotMessage(message,image_rui,hisUid);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                    sendMessage(message);
                }

            }

        });


        attachbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowImagePickDialog();
                Imageboth=findViewById(R.id.Imageboth);



                if (choose.getVisibility()==View.VISIBLE && Imageboth.getVisibility()==View.VISIBLE){
                    choose.setVisibility(View.INVISIBLE);
                    Imageboth.setVisibility(View.VISIBLE);
                }else {
                    choose.setVisibility(View.VISIBLE);
                    Imageboth.setVisibility(View.GONE);

                }







            }
        });





        message1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Drawable drawable = ContextCompat.getDrawable(ChartActivity.this, R.drawable.backgroundcclip2);
                sendbtn.setColorFilter(ContextCompat.getColor(ChartActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                sendbtn.setBackground(drawable);
                choose.setVisibility(View.GONE);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    chechtypingStatus("noOne");
                } else {
                    chechtypingStatus(hisUid);
                }

                Drawable drawable = ContextCompat.getDrawable(ChartActivity.this, R.drawable.backgroundcclip2);
                sendbtn.setColorFilter(ContextCompat.getColor(ChartActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                sendbtn.setBackground(drawable);
                choose.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Drawable drawable = ContextCompat.getDrawable(ChartActivity.this, R.drawable.backgroundcclip2);
                sendbtn.setColorFilter(ContextCompat.getColor(ChartActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                sendbtn.setBackground(drawable);

                choose.setVisibility(View.GONE);

            }
        });


    }

    private void ShowImagePickDialog() {


        ImageButton galler=findViewById(R.id.gallery);
        ImageButton camera=findViewById(R.id.camera);

        galler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkStoragePermission()) {
                    requsetStoragePermission();
                    choose.setVisibility(View.GONE);
                    choose.setVisibility(View.GONE);
                } else {
                    picKFromGallery();
                    choose.setVisibility(View.GONE);
                }
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    requsetCameraPermission();
                } else {
                    pickFromCamera();
                    choose.setVisibility(View.GONE);
                }
            }
        });

    }






    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requsetStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }



    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result & result1;
    }

    private void requsetCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQUEST_CODE);
    }



    private void chechOnlineStatus(String status) {

        DatabaseReference dcRef=FirebaseDatabase.getInstance().getReference("Users").child(myuid);
        HashMap<String,Object> map=new HashMap<>();
        map.put("onlinestatus",status);
        dcRef.updateChildren(map);
    }

    private void chechtypingStatus(String typing) {

        DatabaseReference dcRef=FirebaseDatabase.getInstance().getReference("Users").child(myuid);
        HashMap<String,Object> map=new HashMap<>();
        map.put("typingstatus",typing);
        dcRef.updateChildren(map);
    }

    private void seenMessage() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("Chat");
        seenListener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    ModelChatdrop chat=ds.getValue(ModelChatdrop.class);
                    if (chat.getReceiver().equals(myuid) && chat.getSender().equals(hisUid)){
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("isseen",true);
                        ds.getRef().updateChildren(map);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }


    private void readMessage() {
        chatList=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    ModelChatdrop chat=ds.getValue(ModelChatdrop.class);
                    if (chat.getReceiver().equals(myuid) && chat.getSender().equals(hisUid)
                            || chat.getReceiver().equals(hisUid) && chat.getSender().equals(myuid)){
                        chatList.add(chat);
                    }
                }

                Log.e("tag","chatList: " + chatList.size());
                chatAdapter=new ChatAdapterdrop(ChartActivity.this , chatList , hisImage);
                chatAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(chatAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp=String.valueOf(System.currentTimeMillis());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myuid=user.getUid();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myuid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("onlinestatus","onlinestatus");
        hashMap.put("timestamp",timestamp);
        hashMap.put("isseen",false);
        hashMap.put("type","text");

        databaseReference.child("Chat").push().setValue(hashMap);

        message1.setText("");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("lastmessagetime")
                .child(hisUid);

        userRef.child("time").setValue(timestamp);
        userRef.child("mesage").setValue(message);
        userRef.child("name").setValue(hisname);


        DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(hisUid)
                .child("lastmessagetime")
                .child(myuid);

        userRef2.child("time").setValue(timestamp);
        userRef2.child("mesage").setValue(message);
        userRef2.child("name").setValue(username);


        progressBar.setVisibility(View.GONE);
        sendbtn.setVisibility(View.VISIBLE);


        final DatabaseReference chatRef1=FirebaseDatabase.getInstance()
                .getReference("Chatlist")
                .child(myuid)
                .child(hisUid);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef1.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        final DatabaseReference chatRef2=FirebaseDatabase.getInstance()
                .getReference("Chatlist")
                .child(hisUid)
                .child(myuid);
        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef2.child("id").setValue(myuid);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        sendNotificationToReceiver(hisUid, message);
    }



    private void sendNotificationToReceiver(String receiverUid, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("Users").child(receiverUid);

        tokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String token = snapshot.child("fcmToken").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);

                    if (token != null && username != null) {
                        sendFCMNotification(token, username, message);
                    } else {
                        Toast.makeText(ChartActivity.this, "Error: Token or username is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }



    private void sendFCMNotification(String token, String username, String message) {

        SendNotification sendNotification=new SendNotification(token,username,message,ChartActivity.this);
        sendNotification.sendNotification();

    }



    @Override
    protected void onResume() {
        chechOnlineStatus("online");
        Status("online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
        String timestamp=String.valueOf(System.currentTimeMillis());
        chechOnlineStatus(timestamp);
        chechtypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onStart() {
        chechOnlineStatus("online");
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean cameraAccepted=grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1] ==PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();

                    }else{
                        Toast.makeText(this, "Camera & Storage Permission Are neccesssary", Toast.LENGTH_SHORT).show();
                    }
                }else {

                }

                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length>0) {


                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        picKFromGallery();
                    } else {
                        Toast.makeText(this, "Storage Permission Are neccesssary", Toast.LENGTH_SHORT).show();
                    }
                }else {

                }
                break;
        }
    }

    private void picKFromGallery() {

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALARY_CODE);
    }

    private void pickFromCamera() {

        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_rui=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_rui);
        startActivityForResult(intent,IMAGE_PICK_CAMER_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if (resultCode==RESULT_OK){
            if (requestCode==IMAGE_PICK_GALARY_CODE){
                image_rui=data.getData();

                if (image_rui==null){
                    Imageboth.setVisibility(View.GONE);
                }else {
                    Imageboth.setVisibility(View.VISIBLE);
                    Drawable drawable = ContextCompat.getDrawable(ChartActivity.this, R.drawable.backgroundcclip2);
                    sendbtn.setColorFilter(ContextCompat.getColor(ChartActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                    sendbtn.setBackground(drawable);
                    choose.setVisibility(View.GONE);
                }
                ImageView choseimg=findViewById(R.id.chooseimage);
                Picasso.get().load(image_rui).into(choseimg);


            }
            else if (requestCode==IMAGE_PICK_CAMER_CODE){

                if (image_rui==null){
                    Imageboth.setVisibility(View.GONE);
                }else {
                    Imageboth.setVisibility(View.VISIBLE);
                    Drawable drawable = ContextCompat.getDrawable(ChartActivity.this, R.drawable.backgroundcclip2);
                    sendbtn.setColorFilter(ContextCompat.getColor(ChartActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                    sendbtn.setBackground(drawable);
                    choose.setVisibility(View.GONE);
                }
                ImageView choseimg=findViewById(R.id.chooseimage);
                Picasso.get().load(image_rui).into(choseimg);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Status(String status) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        databaseReference.updateChildren(hashMap);
    }


    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(color);
        }
    }

    private void sendImageMessage(Uri image_rui2) throws IOException {



        progressBar.setVisibility(View.VISIBLE);
        sendbtn.setVisibility(View.GONE);

        String timeStamp =""+System.currentTimeMillis();
        String fileNameAndPath="ChatImage/"+"post_"+timeStamp;

        Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),image_rui2);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data=baos.toByteArray();

        StorageReference ref= FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        sendbtn.setVisibility(View.VISIBLE);
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String downloadUri=uriTask.getResult().toString();

                        if (uriTask.isSuccessful()){
                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("sender",myuid);
                            hashMap.put("receiver",hisUid);
                            hashMap.put("message",downloadUri);
                            hashMap.put("timestamp",timeStamp);
                            hashMap.put("type","image");
                            hashMap.put("isseen",false);

                            databaseReference.child("Chat").push().setValue(hashMap);



                            image_rui=null;

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("lastmessagetime")
                                    .child(hisUid);

                            userRef.child("time").setValue(timeStamp);
                            userRef.child("mesage").setValue("Shared image");
                            userRef.child("name").setValue(hisname);

                            DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(hisUid)
                                    .child("lastmessagetime")
                                    .child(myuid);


                            userRef2.child("time").setValue(timeStamp);
                            userRef2.child("mesage").setValue("Shared image");
                            userRef2.child("name").setValue(username);
                            message1.setText("");



                            choose.setVisibility(View.INVISIBLE);
                            Imageboth.setVisibility(View.INVISIBLE);

                            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users").child(myuid);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user=snapshot.getValue(User.class);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            final DatabaseReference chatRef1=FirebaseDatabase.getInstance().getReference("Chatlist")
                                    .child(myuid)
                                    .child(hisUid);
                            chatRef1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                    if (!snapshot.exists()){
                                        chatRef1.child("id").setValue(hisUid);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull  DatabaseError error) {

                                }
                            });


                            final DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("Chatlist")
                                    .child(hisUid)
                                    .child(myuid);
                            chatRef2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                    if (!snapshot.exists()){
                                        chatRef2.child("id").setValue(myuid);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull  DatabaseError error) {

                                }
                            });



                            sendNotificationToReceiver(hisUid, "Send Image");
                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        sendbtn.setVisibility(View.VISIBLE);
                    }
                });


    }
    private void sendBotMessage(String message, Uri imageUrl2, String recipientUid) throws IOException {

        progressBar.setVisibility(View.VISIBLE);
        sendbtn.setVisibility(View.GONE);

        String timeStamp =""+System.currentTimeMillis();
        String fileNameAndPath="ChatImage/"+"post_"+timeStamp;

        Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUrl2);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data=baos.toByteArray();

        StorageReference ref= FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        sendbtn.setVisibility(View.VISIBLE);
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String downloadUri=uriTask.getResult().toString();

                        if (uriTask.isSuccessful()){



                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            String timestamp = String.valueOf(System.currentTimeMillis());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String myUid = user.getUid();

                            // Combined message HashMap
                            HashMap<String, Object> messageMap = new HashMap<>();
                            messageMap.put("sender", myUid);
                            messageMap.put("timestamp", timestamp);
                            messageMap.put("isseen", false);
                            messageMap.put("type", "both"); // Set type to "both"

                            // Check if message is text
                            if (message != null && !message.trim().isEmpty()) {
                                messageMap.put("message", message);
                            }

                            // Check if message is image URL
                            if (downloadUri != null ) {
                                messageMap.put("messageimage", downloadUri);
                            }

                            // Set the receiver UID
                            messageMap.put("receiver", recipientUid);

                            choose.setVisibility(View.INVISIBLE);
                            Imageboth.setVisibility(View.INVISIBLE);
                            // Save message to Firebase Database for the single recipient
                            databaseReference.child("Chat").push().setValue(messageMap)
                                    .addOnSuccessListener(aVoid -> {
                                        updateChatList(myUid, recipientUid);
                                        sendNotificationToReceiver(recipientUid, message+"\n"+"Shared image");



                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                                .child("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("lastmessagetime")
                                                .child(hisUid);

                                        userRef.child("time").setValue(timestamp);
                                        userRef.child("mesage").setValue(message+" "+"Shared image");
                                        userRef.child("name").setValue(hisname);


                                        DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference()
                                                .child("Users")
                                                .child(hisUid)
                                                .child("lastmessagetime")
                                                .child(myuid);

                                        userRef2.child("time").setValue(timestamp);
                                        userRef2.child("mesage").setValue(message+" "+"Shared image");
                                        userRef2.child("name").setValue(username);
                                        message1.setText("");

                                        image_rui=null;

                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("TAG", "Failed to send message: " + e.getMessage());
                                        Toast.makeText(ChartActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                    });

                            // Send notification via FCM for the single recipient
                            // Note: You may need to implement the actual FCM sending logic here

                            Toast.makeText(ChartActivity.this, "Message sent", Toast.LENGTH_SHORT).show();

                            // Log and handle responses if needed for FCM notifications



                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        sendbtn.setVisibility(View.VISIBLE);
                    }
                });








    }

    private void updateChatList(String myUid, String hisUid) {
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(myUid).child(hisUid);
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef1.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to update chat list: " + error.getMessage());
            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist").child(hisUid).child(myUid);
        chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef2.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to update chat list: " + error.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}


