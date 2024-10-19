package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class playbackActivity extends AppCompatActivity {


    private CategoryAdapter3 categoryAdapter;
    private RecyclerView categoriesRecyclerView,categoriesRecyclerView2;

    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);


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
        categoriesRecyclerView2 = findViewById(R.id.recyclerview2);
        getCategories();
        getCategories2();
    }

    public void getCategories() {
        FirebaseFirestore.getInstance().collection("playbackfolders")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel> categoryList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                    setupCategoryRecyclerView(categoryList);
                });
    }

    public void setupCategoryRecyclerView(List<CategoryModel> categoryList) {
        categoryAdapter = new CategoryAdapter3(categoryList, this);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    public void getCategories2() {
        FirebaseFirestore.getInstance().collection("droptrack")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel> categoryList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                    setupCategoryRecyclerView3(categoryList);
                });
    }

    public void setupCategoryRecyclerView3(List<CategoryModel> categoryList) {
        categoryAdapter = new CategoryAdapter3(categoryList, this);
        categoriesRecyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView2.setAdapter(categoryAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}