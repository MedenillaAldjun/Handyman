package com.group.trylang;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {


    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Plumbing();
            case 1:
                return new Carpentry();
            case 2:
                return new Masonry();
            case 3:
                return new Construction();
            default:
                return new Plumbing();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }



}

