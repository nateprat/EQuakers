package com.nateprat.equakers.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nateprat.equakers.R;
import com.nateprat.equakers.api.BritishGeologicalSurveyEarthquakeAPI;
import com.nateprat.equakers.core.concurrency.ThreadPools;
import com.nateprat.equakers.core.listeners.CustomSwipeRefreshListener;
import com.nateprat.equakers.core.task.BottomNavBarListTask;
import com.nateprat.equakers.core.task.BottomNavBarMapTask;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.model.comparators.EarthquakeDefaultComparator;
import com.nateprat.equakers.model.holders.EarthquakeRecordAdapter;
import com.nateprat.equakers.service.RssBGSEarthquakeCallable;
import com.nateprat.equakers.ui.custom.BottomNavigationBar;
import com.nateprat.equakers.utils.BuildUtils;
import com.nateprat.equakers.utils.TagUtils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private RecyclerView earthquakeList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EarthquakeRecordAdapter earthquakeListAdapter;
    private BottomNavigationBar bottomNavigationView;
    private List<EarthquakeRecord> earthquakeRecords = new ArrayList<>();
    private BritishGeologicalSurveyEarthquakeAPI api = new BritishGeologicalSurveyEarthquakeAPI();

    private final ReentrantLock updateLock = new ReentrantLock();

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        submitRecords(getLatestRecords());
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
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        earthquakeList = (RecyclerView) findViewById(R.id.earthquakeList2);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        earthquakeList.setLayoutManager(new LinearLayoutManager(this));
        earthquakeListAdapter = new EarthquakeRecordAdapter(this);
        earthquakeList.setAdapter(earthquakeListAdapter);
        bottomNavigationView = new BottomNavigationBar(findViewById(R.id.bottom_navigation));
        bottomNavigationView.addItemRunnable(R.id.page_1, new BottomNavBarListTask(this));
        BottomNavBarMapTask mapTask = new BottomNavBarMapTask(this);
        mapTask.updateRecords(earthquakeRecords);
        bottomNavigationView.addItemRunnable(R.id.page_2, mapTask);

        swipeRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshListener(swipeRefreshLayout) {
            @Override
            protected boolean run() {
                List<EarthquakeRecord> newRecords = getLatestRecords();
                boolean isEqual = newRecords.equals(earthquakeRecords);
                if (isEqual) {
                    Log.d(TagUtils.getTag(this), "Existing earthquakeRecords list is the same as previous, ignoring update.");
                } else {
                    earthquakeRecords = newRecords;
                    mapTask.updateRecords(earthquakeRecords);
                }
                return !isEqual;
            }

            @Override
            protected void onSuccess() {
                Log.i(TagUtils.getTag(this), "Mapping earthquake list (size=" + earthquakeRecords.size() + ") to scroller view.");
                MainActivity.this.runOnUiThread(() -> submitRecords(earthquakeRecords));
            }

            @Override
            protected void onFailure() {
                // Nada
            }
        });
        swipeRefreshLayout.refreshDrawableState();
        Log.e("MyTag","after startButton");
        // More Code goes here
    }

    private void sortList(Comparator<EarthquakeRecord>... comparators) {
        if (BuildUtils.supportsAPI(Build.VERSION_CODES.N)) {
            Comparator<EarthquakeRecord> combinedComparator = Arrays.stream(comparators)
                    .reduce(Comparator::thenComparing)
                    .orElse(new EarthquakeDefaultComparator());
            earthquakeRecords.sort(combinedComparator);
        } else {

        }
    }

    private List<EarthquakeRecord> getLatestRecords() {
        return api.call();
    }

    private void submitRecords(List<EarthquakeRecord> records) {
        earthquakeListAdapter.submitList(records);
    }

}