package com.nateprat.equakers.ui.custom.sorting;

import com.nateprat.equakers.model.comparators.EarthquakeLatComparator;
import com.nateprat.equakers.model.comparators.EarthquakeLngComparator;

public class EarthquakeRecordLngSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Longitude";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordLngSorter() {
        super(KEY, iconPath, new EarthquakeLngComparator());
    }

}
