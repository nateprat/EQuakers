package com.nateprat.university.mobileplatformdevelopment.ui.daterange;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.Chip;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.activity.EarthquakeRecordScrollingActivity;
import com.nateprat.university.mobileplatformdevelopment.core.listeners.DatePickerChip;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.Earthquake;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.LatLngExt;
import com.nateprat.university.mobileplatformdevelopment.model.Location;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeDepthComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeLatComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeLngComparator;
import com.nateprat.university.mobileplatformdevelopment.model.comparators.EarthquakeMagnitudeComparator;

import org.apache.commons.lang3.Range;

import java.util.Comparator;
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

    // Earthquake Records
    private EarthquakeRecord mostNorthernEarthquake;
    private EarthquakeRecord mostEasternEarthquake;
    private EarthquakeRecord mostSouthernEarthquake;
    private EarthquakeRecord mostWesternEarthquake;

    private EarthquakeRecord highestMagnitudeEarthquake;
    private EarthquakeRecord lowestMagnitudeEarthquake;

    private EarthquakeRecord shallowestEarthquake;
    private EarthquakeRecord deepestEarthquake;

    private TextView northernTextView;
    private TextView easternTextView;
    private TextView southernTextView;
    private TextView westernTextView;

    private TextView highMagnitudeTextView;
    private TextView lowMagnitudeTextView;

    private TextView deepTextView;
    private TextView shallowTextView;

    
    private CardView northernCardView;
    private CardView easternCardView;
    private CardView southernCardView;
    private CardView westernCardView;

    private CardView highMagnitudeCardView;
    private CardView lowMagnitudeCardView;

    private CardView deepCardView;
    private CardView shallowCardView;

    private EarthquakeObserver earthquakeObserver = new EarthquakeObserver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_range, container, false);
        startDateChip = new DatePickerChip(v.findViewById(R.id.startDateChip), format, onDateSetListener());
        endDateChip = new DatePickerChip(v.findViewById(R.id.endDateChip), format, onDateSetListener());
        startDateChip.setmChipAfter(endDateChip);
        endDateChip.setmChipBefore(startDateChip);

        northernTextView = v.findViewById(R.id.northernRecord);
        easternTextView = v.findViewById(R.id.easternRecord);
        southernTextView = v.findViewById(R.id.southernRecord);
        westernTextView = v.findViewById(R.id.westernRecord);

        highMagnitudeTextView = v.findViewById(R.id.highMagnitudeRecord);
        lowMagnitudeTextView = v.findViewById(R.id.lowestMagnitudeRecord);

        deepTextView = v.findViewById(R.id.deepestRecord);
        shallowTextView = v.findViewById(R.id.shallowestRecord);


        northernCardView = v.findViewById(R.id.northenCardView);
        easternCardView = v.findViewById(R.id.easternCardView);
        southernCardView = v.findViewById(R.id.southernCardView);
        westernCardView = v.findViewById(R.id.westernCardView);

        highMagnitudeCardView = v.findViewById(R.id.highMagnitudeCardView);
        lowMagnitudeCardView = v.findViewById(R.id.lowMagnitudeCardView);

        deepCardView = v.findViewById(R.id.deepCardView);
        shallowCardView = v.findViewById(R.id.shallowCardView);

        BGSEarthquakeFeed.getInstance().addObserver(earthquakeObserver);
        onDateSetListener().run();
        // Inflate the layout for this fragment
        return v;
    }

    private void setUpListeners() {
        northernCardView.setOnClickListener(openEarthquakeRecordActivity(mostNorthernEarthquake));
        easternCardView.setOnClickListener(openEarthquakeRecordActivity(mostEasternEarthquake));
        southernCardView.setOnClickListener(openEarthquakeRecordActivity(mostSouthernEarthquake));
        westernCardView.setOnClickListener(openEarthquakeRecordActivity(mostWesternEarthquake));

        highMagnitudeCardView.setOnClickListener(openEarthquakeRecordActivity(highestMagnitudeEarthquake));
        lowMagnitudeCardView.setOnClickListener(openEarthquakeRecordActivity(lowestMagnitudeEarthquake));

        deepCardView.setOnClickListener(openEarthquakeRecordActivity(deepestEarthquake));
        shallowCardView.setOnClickListener(openEarthquakeRecordActivity(shallowestEarthquake));
    }

    private View.OnClickListener openEarthquakeRecordActivity(EarthquakeRecord record) {
        return v -> {
            EarthquakeRecordScrollingActivity.startActivity(getContext(), record);
        };
    }

    private Runnable onDateSetListener() {
        return () -> {
            Range<Date> dateRange = Range.between(startDateChip.getmCalendar().getTime(), endDateChip.getmCalendar().getTime());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<EarthquakeRecord> validRecords = earthquakeObserver.getRecords().parallelStream()
                        .filter(r -> dateRange.contains(r.getEarthquake().getDate()))
                        .collect(Collectors.toList());
                mostNorthernEarthquake = validRecords.parallelStream()
                        .max(new EarthquakeLatComparator())
                        .orElse(null);
                mostSouthernEarthquake = validRecords.parallelStream()
                        .min(new EarthquakeLatComparator())
                        .orElse(null);

                mostEasternEarthquake = validRecords.parallelStream()
                        .max(new EarthquakeLngComparator())
                        .orElse(null);
                mostWesternEarthquake = validRecords.parallelStream()
                        .min(new EarthquakeLngComparator())
                        .orElse(null);

                highestMagnitudeEarthquake = validRecords.parallelStream()
                        .max(new EarthquakeMagnitudeComparator())
                        .orElse(null);
                lowestMagnitudeEarthquake = validRecords.parallelStream()
                        .min(new EarthquakeMagnitudeComparator())
                        .orElse(null);

                deepestEarthquake = validRecords.parallelStream()
                        .max(new EarthquakeDepthComparator())
                        .orElse(null);
                shallowestEarthquake = validRecords.parallelStream()
                        .min(new EarthquakeDepthComparator())
                        .orElse(null);
                updateRecordTextViews();
                setUpListeners();
            }
        };
    }
    

    private void updateRecordTextViews() {
        updateText(northernTextView, mostNorthernEarthquake);
        updateText(easternTextView, mostEasternEarthquake);
        updateText(southernTextView, mostSouthernEarthquake);
        updateText(westernTextView, mostWesternEarthquake);

        updateText(highMagnitudeTextView, highestMagnitudeEarthquake);
        updateText(lowMagnitudeTextView, lowestMagnitudeEarthquake);

        updateText(deepTextView, deepestEarthquake);
        updateText(shallowTextView, shallowestEarthquake);
    }

    private void updateText(TextView textView, EarthquakeRecord earthquakeRecord) {
        if (earthquakeRecord != null) {
            textView.setText(earthquakeRecord.getEarthquake().getLocation().getLocationString());
        } else {
            textView.setText(getResources().getString(R.string.date_range_no_earthquakes_available));
        }
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