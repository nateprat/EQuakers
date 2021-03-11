package com.nateprat.university.mobileplatformdevelopment.model.comparators;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;

import java.util.Comparator;

public class EarthquakeDateComparator implements Comparator<EarthquakeRecord> {

    @Override
    public int compare(EarthquakeRecord o1, EarthquakeRecord o2) {
        return o1.compareTo(o2);
    }

}
