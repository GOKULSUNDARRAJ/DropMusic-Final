package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class CustomDialogClassfoeaddlogin extends Dialog {

    private Context context;

    public CustomDialogClassfoeaddlogin(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logindialog);



        ImageView updateButton=findViewById(R.id.editTextDialogInput);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(getContext(),MainActivity4.class));
                ((Activity) context).finish();
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
