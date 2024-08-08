package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MusicsActivity extends AppCompatActivity {

    private CategoryAdapter3 categoryAdapter;
    private RecyclerView categoriesRecyclerView;

    ImageView imageView3;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musics);


        imageView3=findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), searchallActivity.class);
                startActivity(intent);
                finish();
            }
        });


        categoriesRecyclerView = findViewById(R.id.recyclerview);
        getCategories();
    }

    public void getCategories() {
        FirebaseFirestore.getInstance().collection("musics")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel> categoryList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                    setupCategoryRecyclerView(categoryList);
                });
    }

    public void setupCategoryRecyclerView(List<CategoryModel> categoryList) {
        categoryAdapter = new CategoryAdapter3(categoryList, this);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoriesRecyclerView.setAdapter(categoryAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MusicsActivity.this, searchallActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slid_from_left, R.anim.slid_to_right);
    }
}