package com.gokulsundar4545.dropu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.gokulsundar4545.VerifyOTPFinalActivityActivity;
import com.hbb20.CountryCodePicker;

public class PhoneNumberActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText phoneNumberEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);

        // Initialize views
        ccp = findViewById(R.id.ccp);
        phoneNumberEditText = findViewById(R.id.phone_number);
        submitButton = findViewById(R.id.submit_button);

        // Retrieve data from the Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("EXTRA_USERNAME");
        String email = intent.getStringExtra("EXTRA_EMAIL");
        String password = intent.getStringExtra("EXTRA_PASSWORD");

        // Handle submit button click
        submitButton.setOnClickListener(view -> {

            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String countryCode = ccp.getSelectedCountryCodeWithPlus();

            if (phoneNumber.isEmpty()) {

                phoneNumberEditText.setError("Phone number is required");
                
            } else {
                // Process the phone number and country code
                // For example, you could start a new activity with this data
                Intent nextIntent = new Intent(PhoneNumberActivity.this, VerifyOTP.class);
                nextIntent.putExtra("EXTRA_USERNAME", username);
                nextIntent.putExtra("EXTRA_EMAIL", email);
                nextIntent.putExtra("EXTRA_PASSWORD", password);
                nextIntent.putExtra("EXTRA_PHONE_NUMBER", countryCode + phoneNumber);
                startActivity(nextIntent);

            }
        });
    }
}
