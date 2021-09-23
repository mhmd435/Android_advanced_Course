package com.example.androidadvancedcourse.HomeFragment.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidadvancedcourse.HomeFragment.TopGainLoseFrag;

public class TopGainLosersAdapter extends FragmentStateAdapter {

    public TopGainLosersAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = new TopGainLoseFrag();

        Bundle args = new Bundle();
        args.putInt("pos",position);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
