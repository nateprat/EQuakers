package com.nateprat.university.mobileplatformdevelopment.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Earthquake implements Serializable, Comparable<Earthquake> {

    private final Date date;
    private final Location location;
    private final double depth;
    private final double magnitude;
    private static final String depthUnit = "Km";
    private static final String magnitudeUnit = "ML";

    public Earthquake(Date date, Location location, double depth, double magnitude) {
        this.date = date;
        this.location = location;
        this.depth = depth;
        this.magnitude = magnitude;
    }

    public Date getDate() {
        return date;
    }

    public double getDepth() {
        return depth;
    }

    public String getDepthString() {
        return depth + " " + depthUnit;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getMagnitudeString() {
        return magnitude + " " + magnitudeUnit;
    }

    public Location getLocation() {
        return location;
    }

    public static String getDepthUnit() {
        return depthUnit;
    }

    public static String getMagnitudeUnit() {
        return magnitudeUnit;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "dateTime=" + date +
                ", location=" + location +
                ", depth=" + getDepthString() +
                ", magnitude=" + magnitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Earthquake that = (Earthquake) o;
        return Double.compare(that.depth, depth) == 0 &&
                Double.compare(that.magnitude, magnitude) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, location, depth, magnitude);
    }

    @Override
    public int compareTo(Earthquake o) {
        return this.getDate().compareTo(o.getDate());
    }
}
