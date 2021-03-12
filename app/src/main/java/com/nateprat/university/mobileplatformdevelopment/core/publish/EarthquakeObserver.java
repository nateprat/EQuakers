package com.nateprat.university.mobileplatformdevelopment.core.publish;

import android.util.Log;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class EarthquakeObserver implements Observer {

    private List<EarthquakeRecord> records;

    public List<EarthquakeRecord> getRecords() {
        return records;
    }

    private void setRecords(List<EarthquakeRecord> records) {
        this.records = records;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.i(TagUtils.getTag(this), "Update received from BGSEarthquakeFeed");
        this.setRecords((List<EarthquakeRecord>) arg);
    }

    public void requestUpdate() {
        Log.i(TagUtils.getTag(this), "Requesting manual update for BGSEarthquakeFeed");
        BGSEarthquakeFeed.getInstance().callNow();
    }
}
