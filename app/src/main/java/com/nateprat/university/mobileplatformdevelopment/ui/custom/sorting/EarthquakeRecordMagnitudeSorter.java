package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeMagnitudeComparator;

public class EarthquakeRecordMagnitudeSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Magnitude";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordMagnitudeSorter() {
        super(KEY, iconPath, new EarthquakeMagnitudeComparator());
    }

}
