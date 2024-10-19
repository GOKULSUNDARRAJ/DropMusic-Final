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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    private static final String ERROR_MESSAGE_REQUIRED_FIELDS = "All fields are required";
    private static final String ERROR_MESSAGE_LOGIN_FAILED_PREFIX = "Login failed: ";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailedt);
        passwordEditText = findViewById(R.id.passdt);
        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
            finish(); // Finish LoginActivity to prevent going back to it
        }

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Snackbar.make(view, ERROR_MESSAGE_REQUIRED_FIELDS, Snackbar.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);

                // Sign in the user with email and password
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);

                            if (task.isSuccessful()) {
                                // Retrieve FCM token and update in database
                                updateFcmToken();

                                // Navigate to MainActivity
                                startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                                finish();
                            } else {
                                Snackbar.make(view, ERROR_MESSAGE_LOGIN_FAILED_PREFIX + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void gotoLogin(View view) {
        startActivity(new Intent(view.getContext(), signupActivity.class));
        overridePendingTransition(R.anim.slid_from_right, R.anim.slid_to_left);
    }

    public void gotoforgetpass(View view) {
        startActivity(new Intent(view.getContext(), ForgetActivity.class));
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slid_to_top);
    }

    private void updateFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String fcmToken = task.getResult();
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            // Update FCM token in database under the user's node
                            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("fcmToken").setValue(fcmToken);
                            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("token").setValue(fcmToken);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to retrieve FCM token", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
