package com.nateprat.university.mobileplatformdevelopment.model.builder;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nateprat.university.mobileplatformdevelopment.model.Earthquake;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.Location;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import org.apache.commons.lang3.builder.Builder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class EarthquakeRecordBuilder implements Builder<EarthquakeRecord> {

    private double latitude;
    private double longitude;
    private String locationName;
    private String locationCounty;
    private double depth;
    private double magnitude;
    private Date date;
    private String category;
    private URL url;

    public EarthquakeRecordBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public EarthquakeRecordBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public EarthquakeRecordBuilder setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public EarthquakeRecordBuilder setLocationCounty(String locationCounty) {
        this.locationCounty = locationCounty;
        return this;
    }

    public EarthquakeRecordBuilder setDepth(double depth) {
        this.depth = depth;
        return this;
    }

    public EarthquakeRecordBuilder setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        return this;
    }

    public EarthquakeRecordBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    public EarthquakeRecordBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public EarthquakeRecordBuilder setUrl(String url) throws MalformedURLException {
        this.url = new URL(url);
        return this;
    }

    @Override
    public String toString() {
        return "EarthquakeRecordBuilder{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", locationName='" + locationName + '\'' +
                ", locationCounty='" + locationCounty + '\'' +
                ", depth=" + depth +
                ", magnitude=" + magnitude +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", url=" + url +
                '}';
    }

    @Override
    public EarthquakeRecord build() {
        if (locationName != null && date != null && category != null && url != null) {
            LatLng latLngObj = new LatLng(latitude, longitude);
            Location locationObj = new Location(locationName, locationCounty, latLngObj);
            Earthquake earthquakeObj = new Earthquake(date, locationObj, depth, magnitude);
            return new EarthquakeRecord(category, url, earthquakeObj);
        } else {
            Log.e(TagUtils.getTag(this), "Failed to create EarthquakeRecord, see details: " + this.toString());
            return null;
        }
    }
}
