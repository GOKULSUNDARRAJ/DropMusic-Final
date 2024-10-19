package com.gokulsundar4545.dropu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddMovieActivity extends AppCompatActivity {
    private EditText editTextMovieName;
    private EditText editTextMovieCoverUrl;
    private EditText editTextMovieTitle;
    private EditText editTextSubTitle;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmovie);

        // Initialize views
        editTextMovieName = findViewById(R.id.editTextMovieName);
        editTextMovieCoverUrl = findViewById(R.id.editTextMovieCoverUrl);
        editTextMovieTitle = findViewById(R.id.editTextMovieTitle);
        editTextSubTitle = findViewById(R.id.editTextSubTitle);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Set up button click listener
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovieData();
            }
        });
    }

    private void saveMovieData() {
        // Get values from EditTexts
        String movieName = editTextMovieName.getText().toString();
        String movieCoverUrl = editTextMovieCoverUrl.getText().toString();
        String movieTitle = editTextMovieTitle.getText().toString();
        String subTitle = editTextSubTitle.getText().toString();

        // Create a Movie object
        Movie movie = new Movie(movieName, movieCoverUrl, movieTitle, subTitle);

        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MoviesongPlaylist");

        // Generate a unique key for the new movie entry
        String key = databaseReference.push().getKey();

        // Save the Movie object under the unique key
        if (key != null) {
            databaseReference.child(key).setValue(movie).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Data saved successfully
                        Toast.makeText(AddMovieActivity.this, "Movie added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error occurred
                        Toast.makeText(AddMovieActivity.this, "Failed to add movie.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}