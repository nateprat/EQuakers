package com.nateprat.university.mobileplatformdevelopment.model.comparators;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;

import java.util.Comparator;

public class EarthquakeLngComparator implements Comparator<EarthquakeRecord> {

    @Override
    public int compare(EarthquakeRecord o1, EarthquakeRecord o2) {
        return Double.compare(o1.getEarthquake().getLocation().getLatLng().longitude, o2.getEarthquake().getLocation().getLatLng().longitude);
    }

}
