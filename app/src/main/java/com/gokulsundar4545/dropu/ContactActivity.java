package com.gokulsundar4545.dropu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 1;
    private DatabaseReference usersRef;
    private List<String> phoneNumbersList;
    private Set<String> myContactsSet;
    private List<User> commonContactsList;

    private RecyclerView recyclerView;
    private CommonContactsAdapter adapter;

    private EditText searchEditText;


    private List<SongModel> songList;
    private RecyclerView recyclerView2;
    private MostViewAdapter3 adapter2;
    TextView username1,textView445,req;

    FloatingActionButton fab;
    ImageView imageView5;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), SearchChatMainActivity.class));
            }
        });



        imageView5=findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUid1 = firebaseAuth.getCurrentUser().getUid();

        textView445=findViewById(R.id.textView445);
        req=findViewById(R.id.req);

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ContactActivity.this,EditProfileActivity.class);
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext()).toBundle();
                startActivity(intent,bundle);
            }
        });

        textView445.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ContactActivity.this,SearchActivity.class);
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext()).toBundle();
                startActivity(intent,bundle);
            }
        });

        username1=findViewById(R.id.username);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(currentUserUid1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists
                if (dataSnapshot.exists()) {
                    // User data exists, retrieve the username
                    String username = dataSnapshot.child("username").getValue(String.class);
                    // Set the username in the TextView
                    username1.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ContactActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });




        songList = new ArrayList<>();
        recyclerView2 = findViewById(R.id.recycler_view_online_status);
        // For LinearLayout Horizontal
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter2 = new MostViewAdapter3(songList);
        recyclerView2.setAdapter(adapter2);

        FirebaseFirestore.getInstance().collection("song")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<SongModel> updatedList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {

                                String key = documentSnapshot.getId();
                                String songTitle = documentSnapshot.getString("title");
                                String subtitle = documentSnapshot.getString("subtitle");
                                String coverUrl = documentSnapshot.getString("coverUrl");
                                String Url = documentSnapshot.getString("url");
                                String id = documentSnapshot.getString("id");
                                String lyrics = documentSnapshot.getString("lyrics");
                                String artist = documentSnapshot.getString("artist");
                                String name = documentSnapshot.getString("name");
                                Long count = documentSnapshot.getLong("count");
                                String moviename = documentSnapshot.getString("moviename");
                                SongModel song = new SongModel(key, id, songTitle, subtitle, Url, coverUrl, lyrics, artist, name,moviename, count);

                                if (count != null && count > 5) {
                                    updatedList.add(song);
                                }
                            } else {
                                Log.d("MostViewActivity", "No such document");
                            }
                        }
                        adapter2.updateSongList(updatedList);
                        checkAndDisplayToast(updatedList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MostViewActivity", "Error fetching songs", e);
                    }
                });











        searchEditText = findViewById(R.id.search);

        // Set a text change listener on the search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the contacts when the text changes
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementation needed
            }
        });



        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView searchIcon = findViewById(R.id.searchicon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText imageView = findViewById(R.id.search);
                if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });



        // Initialize Firebase database reference and other variables
        initialize();
        recyclerView = findViewById(R.id.recycler_view_common_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonContactsAdapter(commonContactsList);
        recyclerView.setAdapter(adapter);

        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        } else {
            // Permission is already granted, proceed with the app
            retrieveContactsFromDevice();
        }
    }

    private void initialize() {
        // Initialize Firebase database reference and other variables
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        phoneNumbersList = new ArrayList<>();
        myContactsSet = new HashSet<>();
        commonContactsList = new ArrayList<>();
    }

    private void retrieveContactsFromDevice() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId}, null);
                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            myContactsSet.add(phoneNumber);
                        }
                        phoneCursor.close();
                    }
                }
            }
            cursor.close();
        }

        // Retrieve user's phone numbers from the Firebase database
        getUsersPhoneNumbers();
        getonlineuserUsers();
    }

    private void getUsersPhoneNumbers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Retrieve UID
                    String phoneNumber = snapshot.child("phone").getValue(String.class);
                    if (myContactsSet.contains(phoneNumber)) {
                        String name = snapshot.child("username").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String Profile = snapshot.child("profileImageUrl").getValue(String.class);
                        User user = new User(uid, name, phoneNumber, email,false,Profile); // Pass UID to User constructor
                        commonContactsList.add(user);
                    }
                }
                displayCommonContactsInRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ContactActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getonlineuserUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create a new list to hold the online users
                List<User> onlineUsersList = new ArrayList<>();

                // Iterate through the dataSnapshot to retrieve online users
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Retrieve UID
                    String phoneNumber = snapshot.child("phone").getValue(String.class);
                    if (myContactsSet.contains(phoneNumber)) {
                        String name = snapshot.child("username").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String onlineString = snapshot.child("online").getValue(String.class);
                        boolean online = Boolean.parseBoolean(onlineString);
                        String Profile = snapshot.child("profileImageUrl").getValue(String.class);




                        if (online) { // Only add online users to the list
                            User user = new User(uid, name, phoneNumber, email, true,Profile); // Pass UID to User constructor
                            onlineUsersList.add(user);
                        }
                    }
                }

                // Create a new adapter with the updated list of online users
                CommonContactsAdapter2 newAdapter = new CommonContactsAdapter2(onlineUsersList);

                RecyclerView recyclerView3=findViewById(R.id.recycler3);
                // Set the RecyclerView to use the new adapter
                recyclerView3.setAdapter(newAdapter);
                recyclerView3.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ContactActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







    private void displayCommonContactsInRecyclerView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the app
                retrieveContactsFromDevice();
            } else {
                // Permission denied, notify the user
                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class User {
        private String uid; // UID field
        private String name;
        private String phoneNumber;
        private String email;
        private boolean online;
        private String profileimage;

        public User(String uid, String name, String phoneNumber, String email, boolean online, String profileimage) {
            this.uid = uid;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.online = online;
            this.profileimage = profileimage;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public String getProfileimage() {
            return profileimage;
        }

        public void setProfileimage(String profileimage) {
            this.profileimage = profileimage;
        }

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
    }



    private void filterContacts(String query) {
        List<User> filteredContacts = new ArrayList<>();

        // Iterate through all contacts and add those that match the query to the filtered list
        for (User contact : commonContactsList) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredContacts.add(contact);
            }
        }

        // Update the RecyclerView with the filtered list of contacts
        adapter.filterList(filteredContacts);
    }


    private void checkAndDisplayToast(List<SongModel> songs) {
        if (!songs.isEmpty()) {

        }}


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
