package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private EditText phoneEditText;
    private Button updateButton;
    private DatabaseReference currentUserRef;
    private FirebaseAuth mAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressBar=findViewById(R.id.progressBar);
        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get reference to the current user's node in the database
            currentUserRef = database.getReference("Users").child(currentUser.getUid());
        }

        // Initialize Views
        phoneEditText = findViewById(R.id.phone);
        updateButton = findViewById(R.id.update);

        // Set OnClickListener for the updateButton
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // Get text from EditText
                String phoneNumber = phoneEditText.getText().toString().trim();
                if (phoneNumber.isEmpty() || !phoneNumber.contains("+91")) {
                    // Display Snackbar indicating that a valid phone number with "+91" is required
                    Snackbar.make(v, "Please provide a valid phone number with '+91'.", Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    // Set the retrieved text to the database under the current user's node
                    currentUserRef.child("phone").setValue(phoneNumber);
                    currentUserRef.child("uid").setValue(currentUser.getUid());
                    // Display success message
                    Snackbar.make(v, "Request is Successful, now you can able to chat.", Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}