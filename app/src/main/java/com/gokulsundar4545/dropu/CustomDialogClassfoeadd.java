package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

public class CustomDialogClassfoeadd extends Dialog {

    private Context context;

    public CustomDialogClassfoeadd(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_logout);

        ImageView clear = findViewById(R.id.claer);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView updateButton=findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView editTextDialogInput = findViewById(R.id.editTextDialogInput);
        editTextDialogInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log out the user
                FirebaseAuth.getInstance().signOut();
                // Start LoginActivity
                Intent intent = new Intent(context, LoginActivity.class);

                context.startActivity(intent);
                ((Activity) context).finish();

                // Optionally, you can add transition animation here
                // Note: overridePendingTransition() should be called from an Activity
                // context.overridePendingTransition(R.anim.slid_from_top, R.anim.slid_to_bottom);
                // Dismiss the dialog
                dismiss();
            }
        });

    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }

    @Override
    public void onBackPressed() {
        // Do nothing on back press
    }
}
