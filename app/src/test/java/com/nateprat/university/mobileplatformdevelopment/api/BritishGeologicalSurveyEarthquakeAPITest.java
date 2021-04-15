package com.nateprat.university.mobileplatformdevelopment.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class BritishGeologicalSurveyEarthquakeAPITest {

    @Test
    public void getInstance() {
        assertTrue(BritishGeologicalSurveyEarthquakeAPI.getInstance() instanceof BritishGeologicalSurveyEarthquakeAPI);
    }
}