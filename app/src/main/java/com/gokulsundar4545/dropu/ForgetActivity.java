package com.gokulsundar4545.dropu;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetButton;
    private FirebaseAuth firebaseAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        progressBar = findViewById(R.id.progressBar); // Initialize progress bar
        emailEditText = findViewById(R.id.emailedt);
        resetButton = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {

                    Snackbar.make(v, "Enter your email", Snackbar.LENGTH_SHORT).show();

                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.INVISIBLE);
                // Firebase password reset
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    resetButton.setVisibility(View.VISIBLE);
                                    Snackbar.make(v, "Password reset email sent", Snackbar.LENGTH_SHORT).show();

                                    startActivity(new Intent(v.getContext(), LoginActivity.class));
                                    overridePendingTransition(R.anim.slid_from_top, R.anim.slid_to_bottom);
                                } else {
                                    Snackbar.make(v, "Failed to send reset email", Snackbar.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    resetButton.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_from_top, R.anim.slid_to_bottom);
    }
}
