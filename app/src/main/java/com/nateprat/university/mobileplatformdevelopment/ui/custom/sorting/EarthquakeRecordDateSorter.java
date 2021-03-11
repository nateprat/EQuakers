package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeDateComparator;

public class EarthquakeRecordDateSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Date";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordDateSorter() {
        super(KEY, iconPath, true, false, new EarthquakeDateComparator());
    }

}
