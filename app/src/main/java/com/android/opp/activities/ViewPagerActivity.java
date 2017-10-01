package com.android.opp.activities;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.android.opp.R;
import com.android.opp.fragments.ViewPagerFragment;

import java.util.ArrayList;
/**
 * Created by camilo on 3/6/17.
 */

public class ViewPagerActivity extends FragmentActivity {

    private ArrayList<String> imagesArray = new ArrayList<>();
    private ViewPager page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);


        Bundle extras = getIntent().getExtras();
        imagesArray = (ArrayList<String>) extras.get("images");
        Log.v("imagesaqui", imagesArray.toString());
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        page = (ViewPager) findViewById(R.id.pager);
        page.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (page.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            page.setCurrentItem(page.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ViewPagerFragment fragment = new ViewPagerFragment();
            fragment.setAsset(imagesArray.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return imagesArray.size();
        }
    }


}