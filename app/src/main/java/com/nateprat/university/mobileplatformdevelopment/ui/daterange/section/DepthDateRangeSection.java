package com.nateprat.university.mobileplatformdevelopment.ui.daterange.section;

import android.content.Context;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;

import java.util.Locale;

public class DepthDateRangeSection extends DateRangeSection {

    private TextView depthTextView;

    public DepthDateRangeSection(Context context, CardView cardView, TextView nameTextView, TextView depthTextView) {
        super(context, cardView, nameTextView);
        this.depthTextView = depthTextView;
    }

    @Override
    public void setCurrentEarthquakeRecord(EarthquakeRecord currentEarthquakeRecord) {
        super.setCurrentEarthquakeRecord(currentEarthquakeRecord);
        if (currentEarthquakeRecord != null) {
            depthTextView.setText(String.format(Locale.getDefault(), "%s", currentEarthquakeRecord.getEarthquake().getDepthString()));
        }
    }
}
