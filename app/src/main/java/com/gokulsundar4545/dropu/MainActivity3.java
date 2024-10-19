package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity3 extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter3 adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        viewPager = findViewById(R.id.viewPager);

        adapter = new ViewPagerAdapter3(this);
        viewPager.setAdapter(adapter);

}
}