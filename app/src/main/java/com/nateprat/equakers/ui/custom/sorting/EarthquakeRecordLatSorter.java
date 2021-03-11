package com.nateprat.equakers.ui.custom.sorting;

import com.nateprat.equakers.model.comparators.EarthquakeDepthComparator;
import com.nateprat.equakers.model.comparators.EarthquakeLatComparator;

public class EarthquakeRecordLatSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Latitude";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordLatSorter() {
        super(KEY, iconPath, new EarthquakeLatComparator());
    }

}
