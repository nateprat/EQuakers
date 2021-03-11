package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeLatComparator;

public class EarthquakeRecordLatSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Latitude";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordLatSorter() {
        super(KEY, iconPath, new EarthquakeLatComparator());
    }

}
