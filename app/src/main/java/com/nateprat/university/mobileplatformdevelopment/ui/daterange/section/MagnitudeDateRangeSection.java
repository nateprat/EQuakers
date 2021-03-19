package com.nateprat.university.mobileplatformdevelopment.ui.daterange.section;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.MagnitudeCircle;

public class MagnitudeDateRangeSection extends DateRangeSection {

    private final MagnitudeCircle magnitudeCircle;

    public MagnitudeDateRangeSection(Context context, CardView cardView, TextView nameTextView, Button button) {
        super(context, cardView, nameTextView);
        this.magnitudeCircle = new MagnitudeCircle(context, button);
    }

    @Override
    public void setCurrentEarthquakeRecord(EarthquakeRecord currentEarthquakeRecord) {
        super.setCurrentEarthquakeRecord(currentEarthquakeRecord);
        if (currentEarthquakeRecord != null) {
            magnitudeCircle.setMagnitude(currentEarthquakeRecord.getEarthquake().getMagnitude());
        }
    }

}
