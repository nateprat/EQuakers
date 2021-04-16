package com.nateprat.university.mobileplatformdevelopment.service;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nateprat.university.mobileplatformdevelopment.core.listeners.CustomSwipeRefreshListener;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.holders.EarthquakeRecordAdapter;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting.EarthquakeRecordsSorting;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import java.util.List;

public class EarthquakeListService {

    private Activity activity;
    private EarthquakeRecordAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private EarthquakeObserver earthquakeObserver;

    public EarthquakeListService(Activity activity, EarthquakeRecordAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.activity = activity;
        this.adapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.earthquakeObserver = new EarthquakeObserver();
    }

    public void refresh() {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    public void init() {
        BGSEarthquakeFeed.getInstance().addObserver(earthquakeObserver);
        onRefreshListener = new CustomSwipeRefreshListener(swipeRefreshLayout) {
            @Override
            protected boolean run() {
                try {
                    earthquakeObserver.requestUpdate();
                } catch (Exception e) {
                    Log.d(TagUtils.getTag(this), "Failed to update earthquake list");
                    return false;
                }
                return true;
            }

            @Override
            protected void onSuccess() {
                List<EarthquakeRecord> records = earthquakeObserver.getRecords();
                Log.i(TagUtils.getTag(this), "Mapping earthquake list (size=" + records.size() + ") to scroller view.");
                activity.runOnUiThread(() -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        EarthquakeRecordsSorting.getWorkingComparator().ifPresent(records::sort);
                    }
                    if (adapter.getCurrentList().size() != records.size()) {
                        Toast.makeText(activity.getApplicationContext(), "Updated earthquake list", Toast.LENGTH_SHORT).show();
                    }
                   adapter.submitList(records);
                });
            }

            @Override
            protected void onFailure() {
                Toast.makeText(activity.getApplicationContext(), "Failed to update earthquake list", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void uninit() {
        BGSEarthquakeFeed.getInstance().deleteObserver(earthquakeObserver);
        swipeRefreshLayout.setOnRefreshListener(null);
    }

}
