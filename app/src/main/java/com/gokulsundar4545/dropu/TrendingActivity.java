package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TrendingActivity extends AppCompatActivity {
    private CategoryAdapter4 categoryAdapter;
    private RecyclerView categoriesRecyclerView;

    ImageView imageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

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
        FirebaseFirestore.getInstance().collection("Trendings")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel> categoryList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                    setupCategoryRecyclerView(categoryList);
                });
    }

    public void setupCategoryRecyclerView(List<CategoryModel> categoryList) {
        categoryAdapter = new CategoryAdapter4(categoryList, this);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }



}