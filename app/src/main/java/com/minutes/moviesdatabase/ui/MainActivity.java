package com.minutes.moviesdatabase.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.minutes.moviesdatabase.R;
import com.minutes.moviesdatabase.ui.adapter.ViewPageAdapter;
import com.minutes.moviesdatabase.util.SlidingTabLayout;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPageAdapter mPageAdapter;
    private SlidingTabLayout mTabLayout;
    private CharSequence[] mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitles = getResources().getStringArray(R.array.tabs);
        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mPageAdapter = new ViewPageAdapter(getSupportFragmentManager(),mTitles,mTitles.length);
        mViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        mViewPager.setAdapter(mPageAdapter);

        mTabLayout = (SlidingTabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}

