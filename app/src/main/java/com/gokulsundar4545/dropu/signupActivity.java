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

    EditText username1, email1, Password1;
    Button signup1;



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

        signup1 = findViewById(R.id.login);

        signup1.setOnClickListener(view -> {
            String username = username1.getText().toString().trim();
            String email = email1.getText().toString().trim();
            String password = Password1.getText().toString().trim();

            // Email validation pattern
            String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Snackbar.make(view, "All fields are required", Snackbar.LENGTH_SHORT).show();
            } else if (!email.matches(emailPattern)) {
                Snackbar.make(view, "Invalid email address", Snackbar.LENGTH_SHORT).show();
            } else if (password.length() <= 6) {
                Snackbar.make(view, "Password must be more than 6 characters", Snackbar.LENGTH_SHORT).show();
            } else  if ( username.length()<=7){
                Snackbar.make(view, "User Name  be more than 7 characters", Snackbar.LENGTH_SHORT).show();
            }else {
                // Create an Intent to start the NextActivity
                Intent intent = new Intent(signupActivity.this, PhoneNumberActivity.class);

                // Put the data into the Intent
                intent.putExtra("EXTRA_USERNAME", username);
                intent.putExtra("EXTRA_EMAIL", email);
                intent.putExtra("EXTRA_PASSWORD", password);

                // Start the NextActivity
                startActivity(intent);
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