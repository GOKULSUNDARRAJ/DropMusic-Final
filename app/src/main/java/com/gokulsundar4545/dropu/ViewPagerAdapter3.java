package com.gokulsundar4545.dropu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter3 extends FragmentStateAdapter {

    public ViewPagerAdapter3(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainHomeFragment();
            case 1:
                return new ProfileActivityFragment();
            default:
                return new MainHomeFragment(); // Fallback
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of pages
    }
}
