package com.example.mayankagarwal.thechatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mayank Agarwal on 17-01-2018.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            case 1:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        switch (position) {

            case 0:
                return "SEARCH";
            case 1 :
                return "REQUESTS";
            default:
                return null;
        }

    }
}
