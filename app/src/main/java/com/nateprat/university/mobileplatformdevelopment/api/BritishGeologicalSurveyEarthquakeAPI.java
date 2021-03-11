package com.nateprat.university.mobileplatformdevelopment.api;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.service.RssBGSEarthquakeCallable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public final class BritishGeologicalSurveyEarthquakeAPI extends API<List<EarthquakeRecord>> {

    private static volatile BritishGeologicalSurveyEarthquakeAPI instance;
    private static final Object singletonLock = new Object();

    private BritishGeologicalSurveyEarthquakeAPI(Callable<List<EarthquakeRecord>> callable) {
        super(callable, Collections.emptyList());
    }

    public static BritishGeologicalSurveyEarthquakeAPI getInstance() {
        synchronized (singletonLock) {
            if (instance == null) {
                instance = new BritishGeologicalSurveyEarthquakeAPI(new RssBGSEarthquakeCallable());
            }
            return instance;
        }
    }

}
