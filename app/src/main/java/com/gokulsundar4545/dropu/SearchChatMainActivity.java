package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SearchChatMainActivity extends AppCompatActivity {



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saerchchatactivitymain);

        Fragment SearchchatFragment;
        SearchchatFragment = new SearchFragmentdrop();
        if (SearchchatFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_chat, SearchchatFragment)
                    .commit();
        }




    }
}