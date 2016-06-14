package com.minutes.moviesdatabase.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.minutes.moviesdatabase.ui.GeneralFragment;
import com.minutes.moviesdatabase.ui.GenreFragment;


/**
 * Created by barikos on 09.06.16.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {

    CharSequence[] mTitles;
    int mNumOfTabs;

    public ViewPageAdapter(FragmentManager fm, CharSequence[] titles, int numOfTabs) {
        super(fm);
        mTitles = titles;
        mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new GeneralFragment();
        }else {
            return new GenreFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
