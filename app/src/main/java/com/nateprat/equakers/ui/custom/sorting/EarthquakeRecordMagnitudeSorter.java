package com.nateprat.equakers.ui.custom.sorting;

import com.nateprat.equakers.model.comparators.EarthquakeLngComparator;
import com.nateprat.equakers.model.comparators.EarthquakeMagnitudeComparator;

public class EarthquakeRecordMagnitudeSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Magnitude";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordMagnitudeSorter() {
        super(KEY, iconPath, new EarthquakeMagnitudeComparator());
    }

}
