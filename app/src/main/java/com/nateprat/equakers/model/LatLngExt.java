package com.nateprat.equakers.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Objects;

/**
 * Serializable LatLng
 */
public class LatLngExt implements Serializable {

    private final double latitude;
    private final double longitude;

    public LatLngExt(double v, double v1) {
        this.latitude = v;
        this.longitude = v1;
    }

    public LatLngExt(LatLng latLng) {
        this(latLng.latitude, latLng.longitude);
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    public LatLng asLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLngExt latLngExt = (LatLngExt) o;
        return Double.compare(latLngExt.latitude, latitude) == 0 &&
                Double.compare(latLngExt.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
