package com.nateprat.university.mobileplatformdevelopment.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.ui.home.HomeFragment;
import com.nateprat.university.mobileplatformdevelopment.ui.map.MapFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;
    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;

    private Fragment homeFragment;
    private Fragment mapFragment;


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_list:
                    switchToHome();
                    return true;
                case R.id.bottom_navigation_map:
                    switchToMap();
                    return true;
            }
            return false;
        });
    }

    private void switchToHome() {
        fm.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
    }

    private void switchToMap() {
        fm.beginTransaction().replace(R.id.fragmentContainerView, mapFragment).commit();
    }


}