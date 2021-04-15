package com.nateprat.university.mobileplatformdevelopment.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.ui.daterange.DateRangeFragment;
import com.nateprat.university.mobileplatformdevelopment.ui.home.HomeFragment;
import com.nateprat.university.mobileplatformdevelopment.ui.map.MapFragment;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;
    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;

    private Fragment homeFragment;
    private Fragment mapFragment;
    private Fragment dateRangeFragment;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BGSEarthquakeFeed.getInstance().start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BGSEarthquakeFeed.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BGSEarthquakeFeed.getInstance().start();
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        dateRangeFragment = new DateRangeFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_list:
                    switchToHome();
                    return true;
                case R.id.bottom_navigation_map:
                    switchToMap();
                    return true;
                case R.id.bottom_navigation_date_range:
                    switchToDateRange();
                    return true;
            }
            return false;
        });
    }

    private void switchToHome() {
        Log.i(TagUtils.getTag(this), "Switching to home fragment in MainActivity");
        fm.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
    }

    private void switchToMap() {
        Log.i(TagUtils.getTag(this), "Switching to map fragment in MainActivity");
        fm.beginTransaction().replace(R.id.fragmentContainerView, mapFragment).commit();
    }

    private void switchToDateRange() {
        Log.i(TagUtils.getTag(this), "Switching to date_range fragment in MainActivity");
        fm.beginTransaction().replace(R.id.fragmentContainerView, dateRangeFragment).commit();
    }


}