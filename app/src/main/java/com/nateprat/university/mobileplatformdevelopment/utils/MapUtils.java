package com.nateprat.university.mobileplatformdevelopment.utils;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.nateprat.university.mobileplatformdevelopment.service.RedGreenInterpolationService;

public class MapUtils {

    private MapUtils() {}

    public static final LatLng DEFAULT_MAP_LAT_LNG = new LatLng(54.418929, -4.196777);
    public static final float DEFAULT_MAP_ZOOM = 5F;

    public static BitmapDescriptor getMarkerColourForMagnitude(double magnitude) {
        float[] hsv = new float[3];
        Color.colorToHSV(RedGreenInterpolationService.colourAtPoint((int)(magnitude * 10)), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


}
