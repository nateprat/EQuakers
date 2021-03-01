package com.nateprat.equakers.model;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

public class EarthquakeRecord implements Serializable {

    private final String category;
    private final URL url;
    private final Earthquake earthquake;

    public EarthquakeRecord(String category, URL url, Earthquake earthquake) {
        this.category = category;
        this.url = url;
        this.earthquake = earthquake;
    }


    public String getCategory() {
        return category;
    }

    public URL getUrl() {
        return url;
    }

    public Earthquake getEarthquake() {
        return earthquake;
    }

    public String getDetails() {
        return earthquake.getMagnitudeString() + " in " + earthquake.getLocation().getLocationString() + " (" +
                earthquake.getLocation().getLatLng().toString() + ") at depth " + earthquake.getDepthString() +
                ". Link: " + url;
    }

    @Override
    public String toString() {
        return "EarthquakeItem{" +
                ", category='" + category + '\'' +
                ", link=" + url +
                ", earthquake=" + earthquake +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EarthquakeRecord record = (EarthquakeRecord) o;
        return Objects.equals(category, record.category) &&
                Objects.equals(url, record.url) &&
                Objects.equals(earthquake, record.earthquake);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, url, earthquake);
    }
}
