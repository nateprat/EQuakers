package com.nateprat.equakers.core.task;

import android.content.Context;

import com.nateprat.equakers.activity.EarthquakeMapDisplayActivity;
import com.nateprat.equakers.model.EarthquakeRecord;

import java.util.ArrayList;
import java.util.List;

public class BottomNavBarMapTask extends Task {

    private static final ArrayList<EarthquakeRecord> earthquakeRecords = new ArrayList<>();

    public BottomNavBarMapTask(Context context) {
        super(() -> {
            if (!(context instanceof EarthquakeMapDisplayActivity)) {
                EarthquakeMapDisplayActivity.startActivity(context, earthquakeRecords);
            }
        });
    }

    public void updateRecords(List<EarthquakeRecord> earthquakeRecords) {
        BottomNavBarMapTask.earthquakeRecords.clear();
        BottomNavBarMapTask.earthquakeRecords.addAll(earthquakeRecords);
    }

}
