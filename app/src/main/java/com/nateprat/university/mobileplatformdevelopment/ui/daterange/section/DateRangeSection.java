package com.nateprat.university.mobileplatformdevelopment.ui.daterange.section;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.activity.EarthquakeRecordScrollingActivity;
import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;

public abstract class DateRangeSection {

    private final Context context;
    private final CardView cardView;
    private final TextView nameTextView;
    private EarthquakeRecord currentEarthquakeRecord;

    public DateRangeSection(Context context, CardView cardView, TextView nameTextView) {
        this.context = context;
        this.cardView = cardView;
        this.nameTextView = nameTextView;
    }

    public void setCurrentEarthquakeRecord(EarthquakeRecord currentEarthquakeRecord) {
        this.currentEarthquakeRecord = currentEarthquakeRecord;
        ThreadPools.getInstance().submitTask(() -> {
            if (currentEarthquakeRecord != null) {
                this.cardView.setOnClickListener(openEarthquakeRecordActivity(currentEarthquakeRecord));
            } else {
                this.cardView.setOnClickListener(null);
            }
            updateTextViews(nameTextView, currentEarthquakeRecord);
        });
    }

    private View.OnClickListener openEarthquakeRecordActivity(EarthquakeRecord record) {
        return v -> EarthquakeRecordScrollingActivity.startActivity(context, record);
    }

    private void updateTextViews(TextView textView, EarthquakeRecord earthquakeRecord) {
        if (earthquakeRecord != null) {
            textView.setText(earthquakeRecord.getEarthquake().getLocation().getLocationString());
        } else {
            textView.setText(context.getResources().getString(R.string.date_range_no_earthquakes_available));
        }
    }

}
