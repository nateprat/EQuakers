package com.nateprat.equakers.model.comparators;

import com.nateprat.equakers.model.EarthquakeRecord;

import java.util.Comparator;

public class EarthquakeDefaultComparator implements Comparator<EarthquakeRecord> {

    @Override
    public int compare(EarthquakeRecord o1, EarthquakeRecord o2) {
        return o1.compareTo(o2);
    }

}
