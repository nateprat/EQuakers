package com.nateprat.equakers.api;

import android.util.Log;

import com.nateprat.equakers.core.concurrency.ThreadPools;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.service.RssBGSEarthquakeCallable;
import com.nateprat.equakers.utils.TagUtils;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BritishGeologicalSurveyEarthquakeAPI extends API<List<EarthquakeRecord>> {

    @Override
    public List<EarthquakeRecord> call(Object... args) {
        Log.i(TagUtils.getTag(this), "Calling " + APIUrl.britishGeologicalSurveyEarthquakeRssFeed);
        try {
            return ThreadPools.getInstance().submitTask(new RssBGSEarthquakeCallable(), 15L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | MalformedURLException e) {
            Log.e(BritishGeologicalSurveyEarthquakeAPI.class.getSimpleName(), e.getMessage());
            return Collections.emptyList();
        }
    }


}
