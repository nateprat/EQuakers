package com.nateprat.equakers.ui.custom.sorting;

import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.model.comparators.EarthquakeDateComparator;

import java.util.Comparator;

public class EarthquakeRecordDateSorter extends EarthquakeRecordSorter {

    public static final String KEY = "Date";
    private static final String iconPath = "@drawable/ic_menu_view";

    public EarthquakeRecordDateSorter() {
        super(KEY, iconPath, true, false, new EarthquakeDateComparator());
    }

}
