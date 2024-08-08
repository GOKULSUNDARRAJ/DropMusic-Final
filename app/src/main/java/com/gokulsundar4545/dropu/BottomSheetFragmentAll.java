package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// BottomSheetFragmentAll.java
public class BottomSheetFragmentAll extends BottomSheetDialogFragment {

    private String lyrics;

    public static BottomSheetFragmentAll newInstance(String lyrics) {
        BottomSheetFragmentAll fragment = new BottomSheetFragmentAll();
        Bundle args = new Bundle();
        args.putString("lyrics", lyrics);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve lyrics from arguments
            lyrics = getArguments().getString("lyrics");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // Retrieve the TextView
        TextView lyricsTextView = view.findViewById(R.id.lyricsTextView);

        // Set the lyrics text to the TextView
        lyricsTextView.setText(lyrics);

        return view;
    }
}
