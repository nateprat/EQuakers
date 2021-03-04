package com.nateprat.equakers.model.comparators;

import com.nateprat.equakers.model.EarthquakeRecord;

import java.util.Comparator;

public class EarthquakeDepthComparator implements Comparator<EarthquakeRecord> {

    @Override
    public int compare(EarthquakeRecord o1, EarthquakeRecord o2) {
        return Double.compare(o1.getEarthquake().getDepth(), o2.getEarthquake().getDepth());
    }

}
