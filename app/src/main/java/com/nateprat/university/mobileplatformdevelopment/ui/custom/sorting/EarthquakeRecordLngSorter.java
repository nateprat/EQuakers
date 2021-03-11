package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeLngComparator;

public class EarthquakeRecordLngSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Longitude";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordLngSorter() {
        super(KEY, iconPath, new EarthquakeLngComparator());
    }

}
