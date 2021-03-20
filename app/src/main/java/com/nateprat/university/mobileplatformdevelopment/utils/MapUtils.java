package com.nateprat.university.mobileplatformdevelopment.utils;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.Location;
import com.nateprat.university.mobileplatformdevelopment.service.RedGreenInterpolationService;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    private MapUtils() {}

    public static final LatLng DEFAULT_MAP_LAT_LNG = new LatLng(54.418929, -4.196777);
    public static final float DEFAULT_MAP_ZOOM = 5F;

    public static BitmapDescriptor getMarkerColourForMagnitude(double magnitude) {
        float[] hsv = new float[3];
        Color.colorToHSV(RedGreenInterpolationService.colourAtPoint((int)(magnitude * 10)), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public static void moveMapToDefault(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(DEFAULT_MAP_LAT_LNG).zoom(DEFAULT_MAP_ZOOM).build()));
    }

}
