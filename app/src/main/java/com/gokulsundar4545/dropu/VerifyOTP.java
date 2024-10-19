package com.gokulsundar4545.dropu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    private static final String TAG = "VerifyOTP";

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private EditText otpEditText1, otpEditText2, otpEditText3, otpEditText4, otpEditText5, otpEditText6;
    private Button verifyButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        mAuth = FirebaseAuth.getInstance();

        otpEditText1 = findViewById(R.id.otp_edit_text_1);
        otpEditText2 = findViewById(R.id.otp_edit_text_2);
        otpEditText3 = findViewById(R.id.otp_edit_text_3);
        otpEditText4 = findViewById(R.id.otp_edit_text_4);
        otpEditText5 = findViewById(R.id.otp_edit_text_5);
        otpEditText6 = findViewById(R.id.otp_edit_text_6);
        verifyButton = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("EXTRA_PHONE_NUMBER");

        Log.d(TAG, "Phone number received: " + phoneNumber);

        // Start phone number verification
        startPhoneNumberVerification(phoneNumber);

        setOtpEditTextListeners();
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpCode = getOtpCode();

                if (otpCode.isEmpty()) {
                    Toast.makeText(VerifyOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE); // Show progress bar
                    verifyButton.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "OTP entered: " + otpCode);
                    try {
                        //verifyPhoneNumberWithCode(mVerificationId, otpCode);
                        signUpWithEmailAndPassword();
                    }catch (Exception e){

                    }

                }
            }
        });
    }

    private void setOtpEditTextListeners() {
        otpEditText1.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpEditText2.requestFocus();
                }
            }
        });

        otpEditText2.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpEditText3.requestFocus();
                } else if (s.length() == 0) {
                    otpEditText1.requestFocus();
                }
            }
        });

        otpEditText3.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpEditText4.requestFocus();
                } else if (s.length() == 0) {
                    otpEditText2.requestFocus();
                }
            }
        });

        otpEditText4.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpEditText5.requestFocus();
                } else if (s.length() == 0) {
                    otpEditText3.requestFocus();
                }
            }
        });

        otpEditText5.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpEditText6.requestFocus();
                } else if (s.length() == 0) {
                    otpEditText4.requestFocus();
                }
            }
        });

        otpEditText6.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    otpEditText5.requestFocus();
                }
            }
        });
    }

    private String getOtpCode() {
        return otpEditText1.getText().toString() +
                otpEditText2.getText().toString() +
                otpEditText3.getText().toString() +
                otpEditText4.getText().toString() +
                otpEditText5.getText().toString() +
                otpEditText6.getText().toString();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    Log.d(TAG, "Verification completed with credential: " + phoneAuthCredential);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                    Log.e(TAG, "Verification failed: " + e.getMessage(), e);
                    Toast.makeText(VerifyOTP.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    Log.d(TAG, "Code sent. Verification ID: " + verificationId);
                    mVerificationId = verificationId;
                }
            };

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){

        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE); // Hide progress bar regardless of the result

                        verifyButton.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "Sign in successful");
                            Toast.makeText(VerifyOTP.this, "Verification successful", Toast.LENGTH_SHORT).show();
                            signUpWithEmailAndPassword();
                        } else {
                            // Sign in failed
                            Log.e(TAG, "Sign in failed", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE); // Hide progress bar regardless of the result

                                verifyButton.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(VerifyOTP.this, "Sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE); // Hide progress bar regardless of the result

                                verifyButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private void signUpWithEmailAndPassword() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("EXTRA_USERNAME");
        String email = intent.getStringExtra("EXTRA_EMAIL");
        String password = intent.getStringExtra("EXTRA_PASSWORD");
        String phoneNumber = intent.getStringExtra("EXTRA_PHONE_NUMBER");

        Log.d(TAG, "Signing up user: " + username + ", Email: " + email);

        progressBar.setVisibility(View.VISIBLE); // Show progress bar
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar regardless of the result
                    verifyButton.setVisibility(View.VISIBLE);
                    if (task.isSuccessful()) {
                        // Sign up successful, save user data to Realtime Database
                        Log.d(TAG, "Sign up successful");


                        User1 newUser = new User1(username, email, phoneNumber,email,password,username,phoneNumber,FirebaseAuth.getInstance().getCurrentUser().getUid(),"","offline" );
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User data saved to database");
                                        startActivity(new Intent(VerifyOTP.this, LoginActivity.class));
                                        finish();
                                        Toast.makeText(VerifyOTP.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e(TAG, "Failed to save user data", task1.getException());
                                        Toast.makeText(VerifyOTP.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Log.e(TAG, "Sign up failed", task.getException());
                        Toast.makeText(VerifyOTP.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // Hide progress bar regardless of the result
                        verifyButton.setVisibility(View.VISIBLE);
                    }
                });
    }
}
