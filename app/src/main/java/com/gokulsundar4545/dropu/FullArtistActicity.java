package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FullArtistActicity extends AppCompatActivity {

    private CategoryAdapter4 categoryAdapter2;
    private RecyclerView categoriesRecyclerView;
    private EditText searchEditText;
    ImageView imageView36;

    LinearLayout linearLayout2;
    private List<CategoryModel> categoryList = new ArrayList<>(); // Declare as class-level variable

    ImageView imageView3;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        linearLayout2=findViewById(R.id.linearLayout2);




        imageView3=findViewById(R.id.imageView3);
        textView=findViewById(R.id.textView);


        imageView36=findViewById(R.id.imageView36);
        imageView36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout2.setVisibility(View.VISIBLE);
            }
        });

        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        searchEditText = findViewById(R.id.search);


        imageView3.startAnimation(AnimationUtils.loadAnimation(FullArtistActicity.this, R.anim.recycler2));
        textView.startAnimation(AnimationUtils.loadAnimation(FullArtistActicity.this, R.anim.recycler4));
        imageView36.startAnimation(AnimationUtils.loadAnimation(FullArtistActicity.this, R.anim.recycler2));
        searchEditText.startAnimation(AnimationUtils.loadAnimation(FullArtistActicity.this, R.anim.recycler));
        // Fetch categories from Firestore
        getCategories();

        // Add text change listener to search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter categories based on the search query
                filterSongs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });
    }

    // Method to fetch categories from Firestore
    private void getCategories() {
        FirebaseFirestore.getInstance().collection("Category")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                    // Setup RecyclerView with fetched categories
                    setupCategoryRecyclerView(categoryList);
                });
    }

    // Method to set up RecyclerView with categories
    private void setupCategoryRecyclerView(List<CategoryModel> categoryList) {
        categoryAdapter2 = new CategoryAdapter4(categoryList, this);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoriesRecyclerView.setAdapter(categoryAdapter2);
    }



    // Method to filter the song list based on search text
    private void filterSongs(String searchText) {
        List<CategoryModel> filteredList = new ArrayList<>();
        for (CategoryModel song : categoryList) {
            if (song.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(song);
            }
        }


        categoryAdapter2.filter(filteredList);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_from_top,R.anim.slid_to_bottom);
    }
}

