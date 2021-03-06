package com.nateprat.university.mobileplatformdevelopment.core.publish;

import android.util.Log;

import com.nateprat.university.mobileplatformdevelopment.api.BritishGeologicalSurveyEarthquakeAPI;
import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class BGSEarthquakeFeed extends Observable implements Service {

    private static BGSEarthquakeFeed instance;
    private static final Object syncLock = new Object();
    private static boolean isStarted = false;
    private long delay = 1;
    private TimeUnit unit = TimeUnit.MINUTES;
    private List<EarthquakeRecord> records = new ArrayList<>();
    private final Runnable runnable = () -> {
        Log.i(BGSEarthquakeFeed.class.getSimpleName(), "Updating earthquake records from BritishGeologicalSurveyEarthquakeAPI");
        setRecords(BritishGeologicalSurveyEarthquakeAPI.getInstance().call());
    };

    public BGSEarthquakeFeed() { }

    public static BGSEarthquakeFeed getInstance() {
        if (instance == null) {
            synchronized (syncLock) {
                if (instance == null) {
                    instance = new BGSEarthquakeFeed();
                }
            }
        }
        return instance;
    }

    public List<EarthquakeRecord> getRecords() {
        return records;
    }

    public void updateRefreshTime(long delay, TimeUnit unit) {
        updateRefreshTime(delay, unit, true);
    }

    public void updateRefreshTime(long delay, TimeUnit unit, boolean restart) {
        Log.d(TagUtils.getTag(this), "Updating refresh time for BGSEarthquakeFeed. Old: " + formatRefreshTime(this.delay, this.unit) + ", New: " + formatRefreshTime(delay, unit));
        this.delay = delay;
        this.unit = unit;
        if (restart) {
            Log.d(TagUtils.getTag(this), "Restarting BGSEarthquakeService...");
            if (isStarted) {
                stop();
            }
            start();
        }
    }

    private String formatRefreshTime(long delay, TimeUnit unit) {
        return "delay: " + delay + "; unit: " + unit.toString();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        Log.d(TagUtils.getTag(this), "Adding observer to BGSEarthquakeFeed, id=" + o.toString());
        super.addObserver(o);
        // update when added
        o.update(this, records);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        Log.d(TagUtils.getTag(this), "Removing observer for BGSEarthquakeFeed, id=" + o.toString());
        super.deleteObserver(o);
    }

    public void setRecords(List<EarthquakeRecord> records) {
        if (!this.records.equals(records)) {
            this.records = records;
            this.setChanged();
            Log.d(TagUtils.getTag(this), " Notifying observers for BGSEarthquakeFeed for object changed.");
            this.notifyObservers(records);
        }
    }

    @Override
    public void start() {
        if (!isStarted) {
            Log.d(TagUtils.getTag(this), "Submitting BGSEarthquakeFeed runnable to ScheduledThreadPoolExecutor");
            ThreadPools.getInstance().scheduleWithFixedDelay(runnable, 0, delay, unit);
            isStarted = true;
        } else {
            Log.w(TagUtils.getTag(this), "BGSEarthquakeFeed is already started.");
        }
    }

    @Override
    public void stop() {
        if (isStarted) {
            Log.d(TagUtils.getTag(this), "Removing BGSEarthquakeFeed runnable from ScheduledThreadPoolExecutor");
            ThreadPools.getInstance().removeScheduledTask(runnable);
            isStarted = false;
        } else {
            Log.d(TagUtils.getTag(this), "BGSEarthquakeFeed is already stopped.");
        }
    }

    @Override
    public void callNow() {
        if (isStarted) {
            Log.d(TagUtils.getTag(this), "callNow() requested for BGSEarthquakeFeed runnable");
            ThreadPools.getInstance().submitTask(runnable);
        } else {
            Log.w(TagUtils.getTag(this), "BGSEarthquakeFeed is not started, could not complete request.");
        }
    }
}
