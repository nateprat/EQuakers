package com.nateprat.equakers.model.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nateprat.equakers.R;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.ui.custom.MagnitudeCircle;
import com.nateprat.equakers.utils.DateTimeUtils;


public class EarthquakeRecordViewHolder extends RecyclerView.ViewHolder implements CustomViewHolder<EarthquakeRecord> {

    private final TextView location;
    private final TextView date;
    private final MagnitudeCircle magnitudeCircle;

    public EarthquakeRecordViewHolder(@NonNull View view) {
        super(view);
        location = view.findViewById(R.id.locationValue);
        date = view.findViewById(R.id.dateValue);
        magnitudeCircle = new MagnitudeCircle(view.findViewById(R.id.magnitudeValue));
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void bind(EarthquakeRecord record) {
        location.setText(record.getEarthquake().getLocation().getLocationString());
        date.setText(DateTimeUtils.yyyyMMdd.format(record.getEarthquake().getDate()));
        magnitudeCircle.setMagnitude(record.getEarthquake().getMagnitude());
    }

}
