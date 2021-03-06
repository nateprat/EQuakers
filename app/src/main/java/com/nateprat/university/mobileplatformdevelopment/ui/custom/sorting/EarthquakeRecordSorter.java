package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;

import java.util.Comparator;

import static android.content.ContentValues.TAG;

public abstract class EarthquakeRecordSorter {

    private final String key;
    private final String iconPath;
    private volatile boolean enabled;
    private volatile boolean ascending;
    private final Comparator<EarthquakeRecord> comparator;

    protected EarthquakeRecordSorter(String key, String iconPath, boolean enabled, boolean ascending, Comparator<EarthquakeRecord> comparator) {
        this.key = key;
        this.iconPath = iconPath;
        this.enabled = enabled;
        this.ascending = ascending;
        this.comparator = comparator;
    }

    public EarthquakeRecordSorter(String key, String iconPath, Comparator<EarthquakeRecord> comparator) {
        this(key, iconPath, false, true, comparator);
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public Comparator<EarthquakeRecord> getComparator() {
        return comparator;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Comparator<EarthquakeRecord> getWorkingComparator() {
        Log.d(TAG, "getWorkingComparator: " + "calling working comparator for " + comparator.getClass().getSimpleName());
        if (isEnabled()) {
            return isAscending() ? comparator : comparator.reversed();
        } else return null;
    }
}
