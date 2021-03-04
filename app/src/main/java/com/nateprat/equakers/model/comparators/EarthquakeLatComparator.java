package com.nateprat.equakers.model.comparators;

import com.nateprat.equakers.model.EarthquakeRecord;

import java.util.Comparator;

public class EarthquakeLatComparator implements Comparator<EarthquakeRecord> {

    @Override
    public int compare(EarthquakeRecord o1, EarthquakeRecord o2) {
        return Double.compare(o1.getEarthquake().getLocation().getLatLng().latitude, o2.getEarthquake().getLocation().getLatLng().latitude);
    }

}
