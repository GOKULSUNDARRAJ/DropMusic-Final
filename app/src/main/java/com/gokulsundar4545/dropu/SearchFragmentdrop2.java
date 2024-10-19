package com.gokulsundar4545.dropu;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import android.text.Editable;
import android.text.TextWatcher;

// Other imports remain the same

public class SearchFragmentdrop2 extends Fragment {

    private ArrayList<User> matchedContactsList = new ArrayList<>();
    private ArrayList<User> phoneContactsList = new ArrayList<>();

    private ArrayList<User> filteredPhoneContactsList = new ArrayList<>(); // For filtered phone contacts
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private UserAdapterdrop2 phoneContactsAdapter;

    private RecyclerView phoneContactsRv;
    private EditText searchEditText; // EditText for search input
    private Map<String, String> contactsMap;

    TextView title2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search45, container, false);


        title2=view.findViewById(R.id.title2);

        // Initialize FirebaseAuth and FirebaseDatabase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        phoneContactsRv = view.findViewById(R.id.unmatchedRv);
        phoneContactsAdapter = new UserAdapterdrop2(getContext(), filteredPhoneContactsList, false);
        phoneContactsRv.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText = view.findViewById(R.id.name); // Initialize EditText

        // TextWatcher for search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if (checkPermission()) {
            contactsMap = ContactHelper.getContacts(getActivity().getContentResolver());
            getAllUsers(); // Fetch users from Firebase
        } else {
            requestPermission();
        }

        return view;
    }

    private void getAllUsers() {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchedContactsList.clear();
                phoneContactsList.clear();

                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    User user = datasnapshot.getValue(User.class);
                    if (user != null) {
                        String phoneNumber = user.getProfission(); // Update this if needed

                        // If phone number matches, add to matched list
                        if (phoneNumber != null && contactsMap.containsKey(phoneNumber)) {
                            user.setUserID(datasnapshot.getKey());
                            matchedContactsList.add(user);
                        }
                    }
                }



                // After matching users, get the phone contacts not present in Firebase
                for (Map.Entry<String, String> entry : contactsMap.entrySet()) {
                    String phoneNumber = entry.getKey();
                    String contactName = entry.getValue();
                    boolean isMatched = false;
                    for (User user : matchedContactsList) {
                        if (user.getProfission().equals(phoneNumber)) {
                            isMatched = true;
                            break;
                        }
                    }
                    if (!isMatched) {
                        User phoneContact = new User();
                        phoneContact.setProfission(phoneNumber);
                        phoneContact.setName(contactName);
                        phoneContactsList.add(phoneContact);
                    }
                }




                filteredPhoneContactsList.addAll(phoneContactsList);



                if (phoneContactsList.isEmpty()){
                    title2.setVisibility(View.GONE);
                }else {
                    title2.setVisibility(View.VISIBLE);
                }





                phoneContactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });


        phoneContactsRv.setAdapter(phoneContactsAdapter);
    }

    private void filter(String query) {

        filteredPhoneContactsList.clear();

        if (query.isEmpty()) {

            filteredPhoneContactsList.addAll(phoneContactsList);
        } else {
            for (User user : matchedContactsList) {
                if (user.getName() != null && user.getName().toLowerCase().contains(query.toLowerCase())) {

                }
            }
            for (User user : phoneContactsList) {
                if (user.getName() != null && user.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredPhoneContactsList.add(user);
                }
            }
        }



        if (filteredPhoneContactsList.isEmpty()){
            title2.setVisibility(View.GONE);
        }else {
            title2.setVisibility(View.VISIBLE);
        }


        phoneContactsAdapter.notifyDataSetChanged();
    }

    // Check for permission
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    // Request permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactsMap = ContactHelper.getContacts(getActivity().getContentResolver());
            } else {
                Toast.makeText(getContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
