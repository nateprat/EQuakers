package com.nateprat.university.mobileplatformdevelopment.service;

import android.util.Log;

import com.nateprat.university.mobileplatformdevelopment.api.APIUrl;
import com.nateprat.university.mobileplatformdevelopment.core.rss.RssParserImpl;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper;
import com.nateprat.university.mobileplatformdevelopment.model.mapper.Mapper;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

public class RssBGSEarthquakeCallable implements Callable<List<EarthquakeRecord>> {

    private static final Mapper<Element, EarthquakeRecord> mapper = new ElementEarthQuakeMapper();

    private final RssParserImpl<EarthquakeRecord> rssParser;

    public RssBGSEarthquakeCallable() {
        URL url = null;
        try {
            url = new URL(APIUrl.britishGeologicalSurveyEarthquakeRssFeed);
        } catch (MalformedURLException e) {
            Log.e(TagUtils.getTag(this), "URL parsing error, unable to make connection to apiUrl: " + APIUrl.britishGeologicalSurveyEarthquakeRssFeed);
        }
        this.rssParser = new RssParserImpl<>(url, mapper);
    }

    @Override
    public List<EarthquakeRecord> call() {
        return rssParser.parseWeirdMethod();
    }

}
