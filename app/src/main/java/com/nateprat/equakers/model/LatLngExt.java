package com.nateprat.equakers.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

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
}
