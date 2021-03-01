package com.nateprat.equakers.service;

import android.util.Log;

import com.nateprat.equakers.api.APIUrl;
import com.nateprat.equakers.core.rss.RssParser;
import com.nateprat.equakers.core.rss.RssParserImpl;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.model.mapper.ElementEarthQuakeMapper;
import com.nateprat.equakers.model.mapper.Mapper;
import com.nateprat.equakers.utils.TagUtils;

import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

public class RssBGSEarthquakeCallable implements Callable<List<EarthquakeRecord>> {

    private static final Mapper<Element, EarthquakeRecord> mapper = new ElementEarthQuakeMapper();

    private final RssParser<EarthquakeRecord> rssParser;

    public RssBGSEarthquakeCallable() throws MalformedURLException {
        URL url = new URL(APIUrl.britishGeologicalSurveyEarthquakeRssFeed);
        this.rssParser = new RssParserImpl<>(url, mapper);
    }

    @Override
    public List<EarthquakeRecord> call() {
        return rssParser.parse();
    }

}
