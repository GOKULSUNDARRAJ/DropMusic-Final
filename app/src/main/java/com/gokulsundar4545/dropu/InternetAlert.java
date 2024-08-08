package com.gokulsundar4545.dropu;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class InternetAlert {

    public static void showNoInternetDialog(View view) {
        Snackbar snackbar = Snackbar.make(view, "Please check your internet connection and try again.", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }




}
