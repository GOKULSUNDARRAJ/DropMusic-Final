package com.gokulsundar4545.dropu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddHighlitescover extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    CircleImageView pro;
    TextView nextButton;
    Uri selectedImageUri;

    EditText covername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_highlitescover);

        pro = findViewById(R.id.pro);
        nextButton = findViewById(R.id.Next);
        covername=findViewById(R.id.editcovername);



        // Set OnClickListener on the CircleImageView to pick an image
        pro.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(AddHighlitescover.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });

        // Set OnClickListener on the Next button to send data to the new activity
        nextButton.setOnClickListener(v -> {

            if (covername.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter some Cover Name", Toast.LENGTH_SHORT).show();
            }else {
                if (selectedImageUri != null) {
                    Intent intent = new Intent(AddHighlitescover.this, SelectPlayListActivity.class);
                    intent.putExtra("imageUri", selectedImageUri.toString());
                    intent.putExtra("imageText",covername.getText().toString());

                    startActivity(intent);
                } else {
                    Toast.makeText(AddHighlitescover.this, "Please select an image first.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            pro.setImageURI(selectedImageUri);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Storage permission is required to pick an image.", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied. Unable to pick image from gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
