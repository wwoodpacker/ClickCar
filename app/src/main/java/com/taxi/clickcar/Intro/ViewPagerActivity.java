package com.taxi.clickcar.Intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.taxi.clickcar.MainActivity;
import com.taxi.clickcar.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Назар on 24.03.2016.
 */
public class ViewPagerActivity extends FragmentActivity {
    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 3;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    ViewPager pager;
    PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);


        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        final CirclePageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);


        ViewPager.OnPageChangeListener mPageChangeListener=new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                if (position==2) mIndicator.setVisibility(View.GONE);
                   else mIndicator.setVisibility(View.VISIBLE);
                mIndicator.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        pager.setOnPageChangeListener(mPageChangeListener);
        mIndicator.setOnPageChangeListener(mPageChangeListener);
    }



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FirstFragment.newInstance(position);

                case 1:
                    return SecondFragment.newInstance(position);
                case 2:
                    return ThirdFragment.newInstance(position);
                default: return FirstFragment.newInstance(0);
            }

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
}
