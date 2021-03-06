package com.nateprat.equakers.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nateprat.equakers.R;
import com.nateprat.equakers.api.BritishGeologicalSurveyEarthquakeAPI;
import com.nateprat.equakers.core.listeners.CustomSwipeRefreshListener;
import com.nateprat.equakers.core.task.BottomNavBarMapTask;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.model.comparators.EarthquakeDefaultComparator;
import com.nateprat.equakers.model.holders.EarthquakeRecordAdapter;
import com.nateprat.equakers.ui.custom.BottomNavigationBar;
import com.nateprat.equakers.utils.BuildUtils;
import com.nateprat.equakers.utils.TagUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Activity {

    private RecyclerView earthquakeList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EarthquakeRecordAdapter earthquakeListAdapter;
    private BottomNavigationBar bottomNavigationBar;
    private List<EarthquakeRecord> earthquakeRecords = new ArrayList<>();

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
        initialiseUI();
        earthquakeRecords = BritishGeologicalSurveyEarthquakeAPI.getInstance().call();
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

    private List<EarthquakeRecord> retrieveLatestRecords() {
        return BritishGeologicalSurveyEarthquakeAPI.getInstance().call();
    }

    private void submitRecords(List<EarthquakeRecord> records) {
        earthquakeListAdapter.submitList(records);
    }

    // Initialisation

    @Override
    public void initialiseUI() {
        initialiseBottomNavigationBar();
        initialiseEarthquakeList();
        initialiseEarthquakeSwipeLayout();
    }

    private void initialiseEarthquakeList() {
        earthquakeList = findViewById(R.id.mainActivityEarthquakeList);
        earthquakeList.setLayoutManager(new LinearLayoutManager(this));
        earthquakeListAdapter = new EarthquakeRecordAdapter(this);
        earthquakeList.setAdapter(earthquakeListAdapter);

    }

    private void initialiseBottomNavigationBar() {
        bottomNavigationBar = new BottomNavigationBar(findViewById(R.id.bottom_navigation), this);
        BottomNavBarMapTask mapTask = (BottomNavBarMapTask) bottomNavigationBar.getTaskForItemId(R.id.bottom_navigation_map);
        mapTask.updateRecords(earthquakeRecords);
    }

    private void initialiseEarthquakeSwipeLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        SwipeRefreshLayout.OnRefreshListener listener = new CustomSwipeRefreshListener(swipeRefreshLayout) {
            @Override
            protected boolean run() {
                List<EarthquakeRecord> newRecords = retrieveLatestRecords();
                boolean isEqual = newRecords.equals(earthquakeRecords);
                if (isEqual) {
                    Log.d(TagUtils.getTag(this), "Existing earthquakeRecords list is the same as previous, ignoring update.");
                } else {
                    earthquakeRecords = newRecords;
                    BottomNavBarMapTask mapTask = (BottomNavBarMapTask)bottomNavigationBar.getTaskForItemId(R.id.bottom_navigation_map);
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
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        listener.onRefresh();
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationBar.getBottomNavigationView().getSelectedItemId() == R.id.bottom_navigation_list) {
            super.onBackPressed();
        } else {
            bottomNavigationBar.getBottomNavigationView().setSelectedItemId(R.id.bottom_navigation_list);
        }
    }

}