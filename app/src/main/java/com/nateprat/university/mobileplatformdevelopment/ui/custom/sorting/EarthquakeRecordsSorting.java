package com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EarthquakeRecordsSorting {

    private static final Map<Integer, EarthquakeRecordSorter> sorterMap = new ConcurrentHashMap<Integer, EarthquakeRecordSorter>() {{
        put(0, new EarthquakeRecordDateSorter());
        put(1, new EarthquakeRecordMagnitudeSorter());
        put(2, new EarthquakeRecordDepthSorter());
        put(3, new EarthquakeRecordLatSorter());
        put(4, new EarthquakeRecordLngSorter());
    }};

    public static Map<Integer, EarthquakeRecordSorter> getSorterMap() {
        return sorterMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<Comparator<EarthquakeRecord>> getWorkingComparator() {
        return sorterMap.values().stream()
                .map(EarthquakeRecordSorter::getWorkingComparator)
                .filter(Objects::nonNull)
                .reduce(Comparator::thenComparing);
    }

}
