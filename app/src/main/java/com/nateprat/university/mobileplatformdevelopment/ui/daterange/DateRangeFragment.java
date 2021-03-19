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
import com.nateprat.university.mobileplatformdevelopment.ui.daterange.section.DepthDateRangeSelection;
import com.nateprat.university.mobileplatformdevelopment.ui.daterange.section.MagnitudeDateRangeSection;

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

    private MagnitudeDateRangeSection highestMagnitudeDateRangeSection;
    private MagnitudeDateRangeSection lowestMagnitudeDateRangeSection;

    private DepthDateRangeSelection deepestDateRangeSelection;
    private DepthDateRangeSelection shallowestDateRangeSelection;

    // Earthquake Records
    private EarthquakeRecord mostNorthernEarthquake;
    private EarthquakeRecord mostEasternEarthquake;
    private EarthquakeRecord mostSouthernEarthquake;
    private EarthquakeRecord mostWesternEarthquake;

    private TextView northernTextView;
    private TextView easternTextView;
    private TextView southernTextView;
    private TextView westernTextView;

    private CardView northernCardView;
    private CardView easternCardView;
    private CardView southernCardView;
    private CardView westernCardView;

    private EarthquakeObserver earthquakeObserver = new EarthquakeObserver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_range, container, false);
        startDateChip = new DatePickerChip(v.findViewById(R.id.startDateChip), format, onDateSetListener());
        endDateChip = new DatePickerChip(v.findViewById(R.id.endDateChip), format, onDateSetListener());
        startDateChip.setmChipAfter(endDateChip);
        endDateChip.setmChipBefore(startDateChip);

        highestMagnitudeDateRangeSection = new MagnitudeDateRangeSection(getContext(), v.findViewById(R.id.highMagnitudeCardView), v.findViewById(R.id.highMagnitudeRecord), v.findViewById(R.id.highMagnitudeButton));
        lowestMagnitudeDateRangeSection = new MagnitudeDateRangeSection(getContext(), v.findViewById(R.id.lowMagnitudeCardView), v.findViewById(R.id.lowestMagnitudeRecord), v.findViewById(R.id.lowMagnitudeButton));

        deepestDateRangeSelection = new DepthDateRangeSelection(getContext(), v.findViewById(R.id.deepCardView), v.findViewById(R.id.deepestRecord), v.findViewById(R.id.deepestRecordValue));
        shallowestDateRangeSelection = new DepthDateRangeSelection(getContext(), v.findViewById(R.id.shallowCardView), v.findViewById(R.id.shallowestRecord), v.findViewById(R.id.shallowestRecordValue));

        northernTextView = v.findViewById(R.id.northernRecord);
        easternTextView = v.findViewById(R.id.easternRecord);
        southernTextView = v.findViewById(R.id.southernRecord);
        westernTextView = v.findViewById(R.id.westernRecord);


        northernCardView = v.findViewById(R.id.northenCardView);
        easternCardView = v.findViewById(R.id.easternCardView);
        southernCardView = v.findViewById(R.id.southernCardView);
        westernCardView = v.findViewById(R.id.westernCardView);

        BGSEarthquakeFeed.getInstance().addObserver(earthquakeObserver);
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

    private void setUpListeners() {
        northernCardView.setOnClickListener(openEarthquakeRecordActivity(mostNorthernEarthquake));
        easternCardView.setOnClickListener(openEarthquakeRecordActivity(mostEasternEarthquake));
        southernCardView.setOnClickListener(openEarthquakeRecordActivity(mostSouthernEarthquake));
        westernCardView.setOnClickListener(openEarthquakeRecordActivity(mostWesternEarthquake));

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

                highestMagnitudeDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .max(new EarthquakeMagnitudeComparator())
                        .orElse(null));
                lowestMagnitudeDateRangeSection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .min(new EarthquakeMagnitudeComparator())
                        .orElse(null));

                deepestDateRangeSelection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .max(new EarthquakeDepthComparator())
                        .orElse(null));
                shallowestDateRangeSelection.setCurrentEarthquakeRecord(validRecords.parallelStream()
                        .min(new EarthquakeDepthComparator())
                        .orElse(null));
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