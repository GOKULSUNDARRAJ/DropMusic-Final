package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity {

    EditText username1, email1, Password1,phone1;
    Button signup1;
    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    TextView forget;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        forget=findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),ForgetActivity.class));
            }
        });


        username1 = findViewById(R.id.user);
        email1 = findViewById(R.id.emailedt);
        Password1 = findViewById(R.id.passdt);
        phone1=findViewById(R.id.phonedt);
        signup1 = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar); // Initialize progress bar
        firebaseAuth = FirebaseAuth.getInstance();

        signup1.setOnClickListener(view -> {
            String username = username1.getText().toString().trim();
            String email = email1.getText().toString().trim();
            String password = Password1.getText().toString().trim();
            String phone=phone1.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || !phone.contains("+91")) {
                Snackbar.make(view, "All fields are required and phone must contain '+91'", Snackbar.LENGTH_SHORT).show();
            }
            else {
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);
                signup1.setVisibility(View.INVISIBLE);

                // Sign up the user with email and password
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            // Hide progress bar regardless of the result
                            progressBar.setVisibility(View.GONE);
                            signup1.setVisibility(View.VISIBLE);

                            if (task.isSuccessful()) {
                                // Sign up successful, save user data to Realtime Database
                                User1 user = new User1(username, email,phone);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                // User data saved successfully
                                                startActivity(new Intent(signupActivity.this,LoginActivity.class));
                                                Snackbar.make(view, "User registered successfully", Snackbar.LENGTH_SHORT).show();

                                            } else {
                                                // Failed to save user data
                                                Snackbar.make(view, "Failed to register user", Snackbar.LENGTH_SHORT).show();

                                            }
                                        });
                            } else {
                                // Sign up failed
                                Snackbar.make(view, "Sign up failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_from_left,R.anim.slid_to_right);
    }

    public void gotoLogin1(View view) {

        startActivity(new Intent(view.getContext(),LoginActivity.class));
        overridePendingTransition(R.anim.slid_from_left,R.anim.slid_to_right);
    }


}