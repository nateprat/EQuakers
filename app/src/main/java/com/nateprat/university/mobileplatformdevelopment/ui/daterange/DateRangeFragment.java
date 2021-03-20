package com.nateprat.university.mobileplatformdevelopment.ui.daterange;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.activity.EarthquakeRecordScrollingActivity;
import com.nateprat.university.mobileplatformdevelopment.core.listeners.DatePickerChip;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeDateComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeDepthComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeLatComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeLngComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeMagnitudeComparator;
import com.nateprat.university.mobileplatformdevelopment.ui.daterange.section.DepthDateRangeSection;
import com.nateprat.university.mobileplatformdevelopment.ui.daterange.section.MagnitudeDateRangeSection;
import com.nateprat.university.mobileplatformdevelopment.ui.daterange.section.MapDateRangeSection;

import org.apache.commons.lang3.Range;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DateRangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateRangeFragment extends Fragment {

    private static final String format = "yyyy/MM/dd";

    private DatePickerChip startDateChip;
    private DatePickerChip endDateChip;

    private MapDateRangeSection northernMapDateRangeSection;
    private MapDateRangeSection easternMapDateRangeSection;
    private MapDateRangeSection southernMapDateRangeSection;
    private MapDateRangeSection westernMapDateRangeSection;

    private MagnitudeDateRangeSection highestMagnitudeDateRangeSection;
    private MagnitudeDateRangeSection lowestMagnitudeDateRangeSection;

    private DepthDateRangeSection deepestDateRangeSection;
    private DepthDateRangeSection shallowestDateRangeSection;

    private EarthquakeObserver earthquakeObserver = new EarthquakeObserver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_range, container, false);
        BGSEarthquakeFeed.getInstance().addObserver(earthquakeObserver);
        startDateChip = new DatePickerChip(v.findViewById(R.id.startDateChip), format, onDateSetListener());
        endDateChip = new DatePickerChip(v.findViewById(R.id.endDateChip), format, onDateSetListener());
        startDateChip.setmChipAfter(endDateChip);
        endDateChip.setmChipBefore(startDateChip);

        MapView nMapView = v.findViewById(R.id.northernMapView);
        nMapView.onCreate(savedInstanceState);
        MapView eMapView = v.findViewById(R.id.easternMapView);
        eMapView.onCreate(savedInstanceState);
        MapView sMapView = v.findViewById(R.id.southernMapView);
        sMapView.onCreate(savedInstanceState);
        MapView wMapView = v.findViewById(R.id.westernMapView);
        wMapView.onCreate(savedInstanceState);

        northernMapDateRangeSection = new MapDateRangeSection(getContext(), v.findViewById(R.id.northernCardView), v.findViewById(R.id.northernRecord), nMapView, earthquakeObserver);
        easternMapDateRangeSection = new MapDateRangeSection(getContext(), v.findViewById(R.id.easternCardView), v.findViewById(R.id.easternRecord), eMapView, earthquakeObserver);
        southernMapDateRangeSection = new MapDateRangeSection(getContext(), v.findViewById(R.id.southernCardView), v.findViewById(R.id.southernRecord), sMapView, earthquakeObserver);
        westernMapDateRangeSection = new MapDateRangeSection(getContext(), v.findViewById(R.id.westernCardView), v.findViewById(R.id.westernRecord), wMapView, earthquakeObserver);

        highestMagnitudeDateRangeSection = new MagnitudeDateRangeSection(getContext(), v.findViewById(R.id.highMagnitudeCardView), v.findViewById(R.id.highMagnitudeRecord), v.findViewById(R.id.highMagnitudeButton));
        lowestMagnitudeDateRangeSection = new MagnitudeDateRangeSection(getContext(), v.findViewById(R.id.lowMagnitudeCardView), v.findViewById(R.id.lowestMagnitudeRecord), v.findViewById(R.id.lowMagnitudeButton));

        deepestDateRangeSection = new DepthDateRangeSection(getContext(), v.findViewById(R.id.deepCardView), v.findViewById(R.id.deepestRecord), v.findViewById(R.id.deepestRecordValue));
        shallowestDateRangeSection = new DepthDateRangeSection(getContext(), v.findViewById(R.id.shallowCardView), v.findViewById(R.id.shallowestRecord), v.findViewById(R.id.shallowestRecordValue));

        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setStartAndEndDate();
        }
        onDateSetListener().run();
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStartAndEndDate() {
        List<EarthquakeRecord> earthquakeRecords = BGSEarthquakeFeed.getInstance().getRecords();
        Date minDate = earthquakeRecords.parallelStream().min(new EarthquakeDateComparator()).get().getEarthquake().getDate();
        Date maxDate = earthquakeRecords.parallelStream().max(new EarthquakeDateComparator()).get().getEarthquake().getDate();
        startDateChip.setDate(minDate);
        endDateChip.setDate(maxDate);
    }

    private Runnable onDateSetListener() {
        return () -> {
            Range<Date> dateRange = Range.between(startDateChip.getmCalendar().getTime(), endDateChip.getmCalendar().getTime());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<EarthquakeRecord> validRecords = earthquakeObserver.getRecords().parallelStream()
                        .filter(r -> dateRange.contains(r.getEarthquake().getDate()))
                        .collect(Collectors.toList());
                northernMapDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .max(new EarthquakeLatComparator())
                        .orElse(null));
                southernMapDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .min(new EarthquakeLatComparator())
                        .orElse(null));
                easternMapDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .max(new EarthquakeLngComparator())
                        .orElse(null));
                westernMapDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .min(new EarthquakeLngComparator())
                        .orElse(null));

                highestMagnitudeDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .max(new EarthquakeMagnitudeComparator())
                        .orElse(null));
                lowestMagnitudeDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .min(new EarthquakeMagnitudeComparator())
                        .orElse(null));

                deepestDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .max(new EarthquakeDepthComparator())
                        .orElse(null));
                shallowestDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .min(new EarthquakeDepthComparator())
                        .orElse(null));
            }
        };
    }

    // TODO: Rename and change types and number of parameters
    public static DateRangeFragment newInstance() {
        DateRangeFragment fragment = new DateRangeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}