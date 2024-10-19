package com.gokulsundar4545.dropu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ChatlistActivityfragmentActivity extends AppCompatActivity {
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlistactivityfragment);

        Fragment SearchchatFragment;
        SearchchatFragment = new ChatListFragmentdrop();
        if (SearchchatFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_chat2, SearchchatFragment)
                    .commit();
        }


        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(), SearchChatMainActivity.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View, String>(findViewById(R.id.fab),"transition_login");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(ChatlistActivityfragmentActivity.this,pairs);
                startActivity(intent,options.toBundle());


            }
        });

    }
}