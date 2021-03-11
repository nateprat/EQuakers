package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeDepthComparator;

public class EarthquakeRecordDepthSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Depth";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordDepthSorter() {
        super(KEY, iconPath, new EarthquakeDepthComparator());
    }

}
