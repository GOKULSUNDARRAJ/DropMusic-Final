package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class splashActivity extends AppCompatActivity {

    // https://profreehost.com/
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView=findViewById(R.id.forget);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(splashActivity.this, MainActivity4.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_bottom,R.anim.slid_to_top);
                finish();
            }
        }, 1000);

        textView.startAnimation(AnimationUtils.loadAnimation(splashActivity.this,R.anim.slide_from_bottom2));

    }
}

